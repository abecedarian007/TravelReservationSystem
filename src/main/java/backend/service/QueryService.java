package backend.service;

import backend.dao.*;
import backend.entity.*;

import backend.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QueryService {
    private static final Logger logger = LoggerFactory.getLogger(QueryService.class);

    private final FlightDAO flightDAO;
    private final HotelDAO hotelDAO;
    private final BusDAO busDAO;
    private final CustomerDAO customerDAO;
    private final ReservationDAO reservationDAO;
    private final ReservationDetailDAO reservationDetailDAO;

    public QueryService() {
        this.flightDAO = new FlightDAO();
        this.hotelDAO = new HotelDAO();
        this.busDAO = new BusDAO();
        this.customerDAO = new CustomerDAO();
        this.reservationDAO = new ReservationDAO();
        this.reservationDetailDAO = new ReservationDetailDAO();
    }



    /**
     * 查询航班、酒店、大巴、客户、预定的所有信息
     * @param objectType 需要查询信息的系统对象：航班、酒店、大巴、客户
     * @return 查询结果，即查询对象的所有信息，以列表形式返回
     * @param <T> 查询对象的类型：航班、酒店、大巴、客户
     */
    public <T> ResponseBody<List<T>> getAllInfo(ObjectType objectType) {
        try {
            List<T> entities = retrieveAll(objectType);
            logger.info("{} information retrieved successfully", objectType.name());
            return new ResponseBody<>(true, objectType.name() + " data retrieved successfully", entities);
        } catch (Exception e) {
            logger.error("Error while retrieving {} information", objectType.name(), e);
            return new ResponseBody<>(false, "Error while retrieving " + objectType.name() + " information", null);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> retrieveAll(ObjectType objectType) throws Exception {
        switch (objectType) {
            case FLIGHT:
                List<Flight> flights = flightDAO.queryAll();
                return (List<T>) flights;
            case HOTEL:
                List<Hotel> hotels = hotelDAO.queryAll();
                return (List<T>) hotels;
            case BUS:
                List<Bus> buses = busDAO.queryAll();
                return (List<T>) buses;
            case CUSTOMER:
                List<Customer> customers = customerDAO.queryAll();
                return (List<T>) customers;
            case RESERVATION:
                List<Reservation> reservations = reservationDAO.queryAll();
                return (List<T>) reservations;
            case RESERVATION_DETAIL:
                List<ReservationDetail> reservationDetails = reservationDetailDAO.queryAll();
                return (List<T>) reservationDetails;
            default:
                throw new IllegalArgumentException("Unsupported object type");
        }
    }

    /**
     * 根据主键查询系统（航班、酒店、大巴、客户）信息。
     * <p> - 根据航班号查询航班信息 </p>
     * <p> - 根据地区查询酒店的信息 </p>
     * <p> - 根据地区查询大巴的信息 </p>
     * <p> - 根据客户名查询客户信息 </p>
     * @param objectType 需要查询信息的系统对象：航班、酒店、大巴、客户
     * @param key 需要查询信息的系统对象的主键：航班-航班号、酒店-地区、大巴-地区、客户-客户名
     * @param clazz 查询结果的返回对象的类型：航班-Flight.class、酒店-Hotel.class、大巴-Bus.class、客户-Customer.class
     * @return 查询结果，这里由于是根据主键查询，所以返回的都是单条数据，即一个查询对象
     * @param <T> 查询对象的类型：航班、酒店、大巴、客户
     */
    public <T> ResponseBody<T> getSysInfo(ObjectType objectType, String key, Class<T> clazz){
        try {
            switch (objectType) {
                case FLIGHT:
                    Flight flightInfo = flightDAO.query(key);
                    logger.info("Flight information retrieved successfully");
                    return new ResponseBody<>(true, "Flight information retrieved successfully", clazz.cast(flightInfo));
                case HOTEL:
                    Hotel hotelInfo = hotelDAO.query(key);
                    logger.info("Hotel information retrieved successfully");
                    return new ResponseBody<>(true, "Hotel information retrieved successfully", clazz.cast(hotelInfo));
                case BUS:
                    Bus busInfo = busDAO.query(key);
                    logger.info("Bus information retrieved successfully");
                    return new ResponseBody<>(true, "Bus information retrieved successfully", clazz.cast(busInfo));
                case CUSTOMER:
                    Customer customerInfo = customerDAO.query(key);
                    logger.info("Customer information retrieved successfully");
                    return new ResponseBody<>(true, "Customer information retrieved successfully", clazz.cast(customerInfo));
                default:
                    logger.error("Invalid ObjectType");
                    return new ResponseBody<>(false, "Invalid ObjectType", null);
            }
        } catch (FlightNotFoundException e) {
            logger.warn("Flight not found for flight number: {}", key, e);
            return new ResponseBody<>(false, "Flight not found for flight number: " + key, null);
        } catch (HotelNotFoundException e) {
            logger.warn("Hotel not found for location: {}", key, e);
            return new ResponseBody<>(false, "Hotel not found for location: " + key, null);
        } catch (BusNotFoundException e) {
            logger.warn("Bus not found for location: {}", key, e);
            return new ResponseBody<>(false, "Bus not found for location: " + key, null);
        } catch (CustomerNotFoundException e) {
            logger.warn("Customer not found for customer : {}", key, e);
            return new ResponseBody<>(false, "Customer not found for customer: " + key, null);
        } catch (Exception e) {
            logger.error("Error while retrieving {} information", objectType.name(), e);
            return new ResponseBody<>(false, "Error while retrieving " + objectType.name() + " information", null);
        }
    }

    /**
     * 根据客户名查询其预定信息，可选择查询基础信息还是详细信息
     * @param customerName 待查询信息的客户名
     * @param isDetailed 是否查询详细信息
     * @return 查询结果，由于一个用户可能不止一条预定信息，故返回一个列表
     * @param <T> 查询结果的对象，List<Reservation> 或 List<ReservationDetail>
     */
    public <T> ResponseBody<List<T>> getReservationInfo(String customerName, boolean isDetailed){
        try {
            List<T> reservationInfo = retrieveReservation(customerName, isDetailed);
            logger.info("Reservation information retrieved successfully");
            return new ResponseBody<>(true, "Reservation information retrieved successfully", reservationInfo);
        } catch (ReservationNotFoundException e) {
            logger.warn("Reservation not found for customer: {}", customerName, e);
            return new ResponseBody<>(false, "Reservation not found for customer: " + customerName, null);
        } catch (ReservationDetailNotFoundException e) {
            logger.warn("Reservation detail not found for customer: {}", customerName, e);
            return new ResponseBody<>(false, "Reservation detail not found for customer: " + customerName, null);
        } catch (Exception e) {
            logger.error("Error while retrieving reservation information", e);
            return new ResponseBody<>(false, "Error while retrieving reservation information", null);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> retrieveReservation(String customerName, boolean isDetailed) throws Exception {
        if(!isDetailed) {
            List<Reservation> reservation = reservationDAO.query(customerName);
            return (List<T>) reservation;
        } else {
            List<ReservationDetail> reservationDetail = reservationDetailDAO.query(customerName);
            return (List<T>) reservationDetail;
        }
    }

}
