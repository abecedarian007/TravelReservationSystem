package backend.service;

import backend.dao.ReservationDAO;
import backend.entity.Response;
import backend.entity.ObjectType;
import backend.exception.ReservationDAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReserveService {
    private static final Logger logger = LoggerFactory.getLogger(ReserveService.class);

    private final ReservationDAO reservationDAO;

    public ReserveService() {
        this.reservationDAO = new ReservationDAO();
    }

    /**
     * 预定航班
     * @param customerName 客户姓名
     * @param flightNumber 航班号
     * @return 预定是否成功的消息
     */
    public Response reserveFlight(String customerName, String flightNumber) {
        try {
            boolean reservationResult = reservationDAO.insert(customerName, ObjectType.FLIGHT, flightNumber);
            if (reservationResult) {
                logger.info("Flight reserved successfully for customer {} on flight {}.", customerName, flightNumber);
                return new Response(true, "Flight reserved successfully");
            } else {
                logger.warn("Flight reserved unsuccessfully for customer {} on flight {}.", customerName, flightNumber);
                return new Response(false, "Flight reserved unsuccessfully");
            }
        } catch (ReservationDAOException e) {
            logger.error("Error in ReservationDAO while reserving flight for customer {} on flight {}.", customerName, flightNumber, e);
            return new Response(false, "Error in FlightDAO while reserving flight");
        } catch (Exception e) {
            logger.error("Error while reserving flight for customer {} on flight {}.", customerName, flightNumber, e);
            return new Response(false, "Error while reserving flight");
        }
    }

    /**
     * 预定酒店
     * @param customerName 客户姓名
     * @param location 酒店地点
     * @return 预定是否成功的消息
     */
    public Response reserveHotel(String customerName, String location) {
        try {
            boolean reservationResult = reservationDAO.insert(customerName, ObjectType.HOTEL, location);
            if (reservationResult) {
                logger.info("Hotel reserved successfully for customer {} on location {}.", customerName, location);
                return new Response(true, "Hotel reserved successfully");
            } else {
                logger.warn("Hotel reserved unsuccessfully for customer {} on location {}.", customerName, location);
                return new Response(false, "Hotel reserved unsuccessfully");
            }
        } catch (ReservationDAOException e) {
            logger.error("Error in ReservationDAO while reserving hotel for customer {} on location {}.", customerName, location, e);
            return new Response(false, "Error in HotelDAO while reserving hotel");
        } catch (Exception e) {
            logger.error("Error while reserving hotel for customer {} on location {}.", customerName, location, e);
            return new Response(false, "Error while reserving hotel");
        }
    }

    /**
     * 预定大巴
     * @param customerName 客户姓名
     * @param location 大巴地点
     * @return 预定是否成功的消息
     */
    public Response reserveBus(String customerName, String location) {
        try {
            boolean reservationResult = reservationDAO.insert(customerName, ObjectType.BUS, location);
            if (reservationResult) {
                logger.info("Bus reserved successfully for customer {} on location {}.", customerName, location);
                return new Response(true, "Bus reserved successfully");
            } else {
                logger.warn("Bus reserved unsuccessfully for customer {} on location {}.", customerName, location);
                return new Response(false, "Bus reserved unsuccessfully");
            }
        } catch (ReservationDAOException e) {
            logger.error("Error in ReservationDAO while reserving bus for customer {} on location {}.", customerName, location, e);
            return new Response(false, "Error in BusDAO while reserving bus");
        } catch (Exception e) {
            logger.error("Error while reserving bus for customer {} on location {}.", customerName, location, e);
            return new Response(false, "Error while reserving bus");
        }
    }
}
