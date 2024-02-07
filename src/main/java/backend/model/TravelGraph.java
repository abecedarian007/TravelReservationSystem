package backend.model;

import backend.entity.ObjectType;
import backend.entity.ReservationDetail;
import backend.exception.StartLocationNotExist;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TravelGraph {
    private final Map<String, List<String>> graph;
    private final List<List<String>> allRoutes;
    private final List<String> visited;

    public TravelGraph() {
        graph = new HashMap<>();
        allRoutes = new ArrayList<>();
        visited = new ArrayList<>();
    }

    private void buildGraph(@NotNull List<ReservationDetail> reservationDetails) {
        for (ReservationDetail reservation : reservationDetails) {
            if (reservation.getResvType() == ObjectType.FLIGHT) {
                graph.putIfAbsent(reservation.getLocationFrom(), new ArrayList<>());
                graph.get(reservation.getLocationFrom()).add(reservation.getLocationTo());
            } else if (reservation.getResvType() == ObjectType.HOTEL || reservation.getResvType() == ObjectType.BUS) {
                graph.putIfAbsent(reservation.getResvKey(), new ArrayList<>());
            }
        }
    }

    /**
     * 根据预定信息和多个起始地（列表），找到所有起始地开始的所有路线
     * <p> 可以处理以下特殊情况： </p>
     * <p> - 分叉 </p>
     * <p> - 交叉汇合 </p>
     * <p> - 环状路径 </p>
     * @param reservationDetails 预定信息
     * @param startLocations 多个起始地
     * @return 所有起始地开始的所有路线
     */
    public List<List<String>> extractRoutes(List<ReservationDetail> reservationDetails, @NotNull List<String> startLocations) throws StartLocationNotExist {
        buildGraph(reservationDetails);
//        System.out.println(graph);

        List<String> originList = new ArrayList<>(startLocations);
        removeUnknownStartLocation(originList);

        for (String startLocation : originList) {
            visited.clear();
            routeDFS(startLocation, new ArrayList<>());
        }

        visited.clear();

//        System.out.println(allRoutes);
        return removeDuplicateCycles( filterSubsets( allRoutes ) );
    }

    private void routeDFS(String node, @NotNull List<String> currentRoute) {
        visited.add(node);
        currentRoute.add(node);

//        System.out.println("current node: " + node);
//        System.out.println("current routes: " + currentRoute);

        if (graph.containsKey(node) && !graph.get(node).isEmpty()) {
//            System.out.println("center: " + node);
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
//                    System.out.println("neighbor: " + neighbor);
                    routeDFS(neighbor, new ArrayList<>(currentRoute));
                } else {
                    // Handle cycle
//                    int idx = currentRoute.indexOf(neighbor);
                    currentRoute.add(neighbor); // 在路线结尾显示起点，说明是环形路线
                    allRoutes.add(currentRoute.subList(0, currentRoute.size()));
                }
            }
        } else {
            allRoutes.add(currentRoute);
//            System.out.println("all routes: " + allRoutes);
        }

        visited.remove(node);
    }

    /**
     * 根据预定信息和多个起始地（列表），判断用户所有起始地点开始的路线完整性
     * @param reservationDetails 预定信息
     * @param startLocations 多个起始地
     * @return 预定路线是否完整
     */
    public boolean isRoutesComplete(List<ReservationDetail> reservationDetails, @NotNull List<String> startLocations) throws StartLocationNotExist {
        buildGraph(reservationDetails);

        List<String> originList = new ArrayList<>(startLocations);
        removeUnknownStartLocation(originList);

        Set<String> visitedEdges = new HashSet<>();
        Set<String> visitedNodes = new HashSet<>();

        for (String startLocation : originList) {
            AtomicBoolean isComplete = new AtomicBoolean(false);
            routeCompleteDFS(startLocation, visitedEdges, visitedNodes, isComplete);
        }

        // Check if all edges have been visited
        for (String node : graph.keySet()) {
            if (graph.containsKey(node) && !graph.get(node).isEmpty()) {
                for (String destination : graph.get(node)) {
                    String edge = node + "->" + destination;
                    if (!visitedEdges.contains(edge)) {
                        return false;  // Edge not visited, not a complete route
                    }
                }
            }
        }
        // Check if all nodes have been visited
        for (String node : graph.keySet()) {
            if (!visitedNodes.contains(node)) {
                return false;  // Node not visited, not a complete route
            }
        }

        return true;
    }

    private void routeCompleteDFS(String node, Set<String> visitedEdges, @NotNull Set<String> visitedNodes, AtomicBoolean  isComplete) {
        visitedNodes.add(node);
//        System.out.println("isComplete: " + isComplete);
//        System.out.println("current node: " + node);

        if (graph.containsKey(node) && !graph.get(node).isEmpty()) {
//            System.out.println("center node: " + node);
            for (String neighbor : graph.get(node)) {
                String edge = node + "->" + neighbor;
//                System.out.println("current edge: " + edge);
                if (!visitedEdges.contains(edge)) {
                    visitedEdges.add(edge);
//                    System.out.println("add edge: " + edge);
                    routeCompleteDFS(neighbor, visitedEdges, visitedNodes, isComplete);
                    if (isComplete.get()) return;
                }
            }
        }
        isComplete.set(true);
    }

    private boolean graphContainsStartLocation(String location) {
        return graph.values().stream().anyMatch(list -> list.contains(location));
    }

    private void removeUnknownStartLocation(@NotNull List<String> startLocations) throws StartLocationNotExist {
        // 使用新的列表保存有效的起始位置
        List<String> validStartLocations = new ArrayList<>();

        for (String location : startLocations) {
            // 如果图中包含该起始位置，并且该起始位置在相应的邻接列表中存在
            if (graph.containsKey(location) || graphContainsStartLocation(location)) {
                validStartLocations.add(location);
            }
        }

        if (validStartLocations.isEmpty()) {
            throw new StartLocationNotExist("All of the start locations are not existing.");
        }

        // 清空原始列表，将有效的起始位置添加回去
        startLocations.clear();
        startLocations.addAll(validStartLocations);
    }

    @Contract("_ -> new")
    private static @NotNull List<List<String>> filterSubsets(List<List<String>> routes) {
        Set<List<String>> uniqueRoutes = new HashSet<>(routes);
        Set<List<String>> nonSubsetRoutes = new HashSet<>(uniqueRoutes);

        for (List<String> route : uniqueRoutes) {
            for (List<String> otherRoute : uniqueRoutes) {
//                System.out.println("route: " + route);
//                System.out.println("otherRoute: " + otherRoute);
//                System.out.println(nonSubsetRoutes);
                if (route != otherRoute && isSubset(route, otherRoute)) {
                    nonSubsetRoutes.remove(route);
//                    System.out.println("remove " + route);
                    break;
                }
            }
        }

        return new ArrayList<>(nonSubsetRoutes);
    }

    private static boolean isSubset(@NotNull List<String> potentialSubset, @NotNull List<String> potentialSuperset) {
        int subsetSize = potentialSubset.size();
        int supersetSize = potentialSuperset.size();

        if (subsetSize == 0) {
            return true;
        }
        if (subsetSize > supersetSize) {
            return false;
        }

        // Iterate through the superset
        for (int i = 0; i <= supersetSize - subsetSize; i++) {
            // Check if the current position in the superset matches the first element of the subset
            if (potentialSuperset.get(i).equals(potentialSubset.get(0))) {
                // Check if the remaining elements of the superset match the subset
                boolean match = true;
                for (int j = 1; j < subsetSize; j++) {
                    if (!potentialSuperset.get(i + j).equals(potentialSubset.get(j))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;  // Found a match, subset is a subset of the superset
                }
            }
        }

        // No match found
        return false;
    }

    @Contract("_ -> new")
    public static @NotNull List<List<String>> removeDuplicateCycles(@NotNull List<List<String>> routes) {
        Set<List<String>> uniqueRoutes = new HashSet<>();

        for (List<String> route : routes) {
            if (isCycle(route)) {
                List<String> normalizedRoute = normalizeCycle(route);
                uniqueRoutes.add(normalizedRoute);
            } else {
                uniqueRoutes.add(route);
            }
        }

        return new ArrayList<>(uniqueRoutes);
    }

    private static boolean isCycle(@NotNull List<String> route) {
        return route.size() > 1 && route.get(0).equals(route.get(route.size() - 1));
    }

    private static @NotNull List<String> normalizeCycle(@NotNull List<String> cycle) {
        if (cycle.size() <= 1 || !cycle.get(0).equals(cycle.get(cycle.size() - 1))) {
            return new ArrayList<>(cycle);
        }

        // Remove the last element (tail) to avoid duplication
        List<String> cycleWithoutTail = new ArrayList<>(cycle);
        cycleWithoutTail.remove(cycle.size() - 1);

        // Find the index of the minimum element in the cycle (without the tail)
        int minIndex = 0;
        for (int i = 1; i < cycleWithoutTail.size(); i++) {
            if (cycleWithoutTail.get(i).compareTo(cycleWithoutTail.get(minIndex)) < 0) {
                minIndex = i;
            }
        }

        // Create a new list starting from the minimum element
        List<String> normalizedCycle = new ArrayList<>();
        for (int i = 0; i < cycleWithoutTail.size(); i++) {
            int index = (minIndex + i) % cycleWithoutTail.size();
            normalizedCycle.add(cycleWithoutTail.get(index));
        }

        // Add the head (first node) back to the normalized cycle
        normalizedCycle.add(normalizedCycle.get(0));

        return normalizedCycle;
    }


}

