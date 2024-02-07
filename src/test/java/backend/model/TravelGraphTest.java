package backend.model;

import backend.entity.ObjectType;
import backend.entity.ReservationDetail;
import backend.exception.StartLocationNotExist;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TravelGraphTest {

    @org.junit.jupiter.api.Test
    void extractRoutes() {
        TravelGraph travelGraph = new TravelGraph();

        List<ReservationDetail> reservationDetails = Arrays.asList(
                // 酒店和大巴数据
                new ReservationDetail(3, "ykm", ObjectType.HOTEL, "A", 100, 99, null, null, 100),
                new ReservationDetail(4, "ykm", ObjectType.BUS, "E", 100, 99, null, null, 100),
                // 倒Y字型路线
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "A", "B", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "B", "C", 100),
//                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "B", "D", 100),
                // Y字型路线
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "E", "G", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "F", "G", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "G", "H", 100),
                // 环型路线
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "I", "J", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "J", "K", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "K", "I", 100)
        );


//        List<String> startLocations = Collections.singletonList("I");
        List<String> startLocations = Arrays.asList("I", "J");


        try {
            List<List<String>> routes = travelGraph.extractRoutes(reservationDetails, startLocations);
            System.out.println(routes);
        } catch (StartLocationNotExist e) {
            e.printStackTrace();
        }

    }

    @org.junit.jupiter.api.Test
    void isRoutesComplete() {
        TravelGraph travelGraph = new TravelGraph();

        List<ReservationDetail> reservationDetails = Arrays.asList(
                new ReservationDetail(3, "ykm", ObjectType.HOTEL, "A", 100, 99, null, null, 100),
                new ReservationDetail(4, "ykm", ObjectType.BUS, "C", 100, 99, null, null, 100),
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "A", "B", 100),
                new ReservationDetail(2, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "B", "C", 100),
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "C", "D", 100),
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "D", "B", 100),
                new ReservationDetail(1, "ykm", ObjectType.FLIGHT, "AU007", 100, 99, "E", "D", 100)
        );

//        List<String> startLocations = Collections.singletonList("A");
        List<String> startLocations = Arrays.asList("A", "B");

        try {
            boolean isComplete = travelGraph.isRoutesComplete(reservationDetails, startLocations);
            System.out.println(isComplete);
        } catch (StartLocationNotExist e) {
            e.printStackTrace();
        }
    }
}