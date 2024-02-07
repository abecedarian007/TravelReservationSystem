package backend.service;

import backend.dao.ReservationDetailDAO;
import backend.entity.ReservationDetail;
import backend.entity.ResponseBody;
import backend.exception.ReservationDetailNotFoundException;
import backend.exception.StartLocationNotExist;
import backend.model.TravelGraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TravelService {
    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);

    private final ReservationDetailDAO reservationDetailDAO;

    public TravelService() {
        this.reservationDetailDAO = new ReservationDetailDAO();
    }

    /**
     * 查询客户从某个起始地点开始的旅途路线
     * @param customerName 客户的姓名
     * @param startLocation 起始地点
     * @return 所有旅途路线
     */
    public ResponseBody<List<List<String>>> getTravelRoute(String customerName, String startLocation){
        List<String> startLocations = new ArrayList<>();
        startLocations.add(startLocation);
        return getTravelRoute(customerName, startLocations);
    }

    /**
     * 查询客户从所有起始地点开始的旅途路线
     * @param customerName 客户姓名
     * @param startLocations 所有起始地点
     * @return 所有旅途路线
     */
    public ResponseBody<List<List<String>>> getTravelRoute(String customerName, List<String> startLocations){
        try {
            List<ReservationDetail> reservationInfo = reservationDetailDAO.query(customerName);
            if (reservationInfo.isEmpty()) {
                logger.warn("The customer's reservation is null");
                return new ResponseBody<>(false, "The customer's reservation is null.", null);
            }
            TravelGraph travelGraph = new TravelGraph();
            List<List<String>> routes = travelGraph.extractRoutes(reservationInfo, startLocations);
            if (routes != null && !routes.isEmpty()) {
                logger.info("Travelling route retrieved successfully");
                return new ResponseBody<>(true, "Travelling route retrieved successfully.", routes);
            } else {
                logger.warn("Travelling route retrieved unsuccessfully");
                return new ResponseBody<>(false, "Travelling route retrieved unsuccessfully.", null);
            }
        } catch (ReservationDetailNotFoundException e) {
            logger.error("Travelling route retrieved unsuccessfully because reservation detail does not exist");
            return new ResponseBody<>(false, "Travelling route retrieved unsuccessfully because reservation detail does not exist.", null);
        } catch (StartLocationNotExist e) {
            logger.error(e.getMessage());
            return new ResponseBody<>(false, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error while retrieving customer {}'s travelling route", customerName, e);
            return new ResponseBody<>(false, "Error while retrieving customer " + customerName + "'s travelling route", null);
        }
    }

    /**
     * 检查客户从某出发地的预订路线的完整性
     * @param customerName 客户姓名
     * @param startLocation 出发地
     * @return 预订路线是否完整
     */
    public ResponseBody<Boolean> checkTravelRouteIntegrality(String customerName, String startLocation){
        List<String> startLocations = new ArrayList<>();
        startLocations.add(startLocation);
        return checkTravelRouteIntegrality(customerName, startLocations);
    }

    /**
     * 检查客户从所有出发地的预订路线的完整性
     * @param customerName 客户姓名
     * @param startLocations 所有出发地
     * @return 预订路线是否完整
     */
    public ResponseBody<Boolean> checkTravelRouteIntegrality(String customerName, List<String> startLocations){
        try {
            List<ReservationDetail> reservationInfo = reservationDetailDAO.query(customerName);
            if (reservationInfo.isEmpty()) {
                logger.warn("The customer's reservation is null");
                return new ResponseBody<>(false, "The customer's reservation is null.", null);
            }
            TravelGraph travelGraph = new TravelGraph();
            boolean isTravelRouteComplete = travelGraph.isRoutesComplete(reservationInfo, startLocations);
            if (isTravelRouteComplete){
                logger.info("The customer's booked route is complete!");
                return new ResponseBody<>(true, "The customer's booked route is complete!", Boolean.TRUE);
            } else {
                logger.info("The customer's booked route is incomplete!");
                return new ResponseBody<>(true, "The customer's booked route is incomplete!", Boolean.FALSE);
            }
        } catch (ReservationDetailNotFoundException e) {
            logger.error("Checking route integrality unsuccessfully because reservation detail does not exist");
            return new ResponseBody<>(false, "Checking route integrality unsuccessfully because reservation detail does not exist.", null);
        } catch (StartLocationNotExist e) {
            logger.error(e.getMessage());
            return new ResponseBody<>(false, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error while checking customer {}'s travelling route integrality", customerName, e);
            return new ResponseBody<>(false, "Error while checking customer " + customerName + "'s travelling route integrality.", null);
        }
    }
}
