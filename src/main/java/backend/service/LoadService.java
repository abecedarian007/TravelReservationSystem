package backend.service;

import backend.dao.FlightDAO;
import backend.dao.BusDAO;
import backend.dao.CustomerDAO;
import backend.dao.HotelDAO;
import backend.entity.Response;
import backend.exception.BusDAOException;
import backend.exception.CustomerDAOException;
import backend.exception.FlightDAOException;
import backend.exception.HotelDAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LoadService {
    private static final Logger logger = LoggerFactory.getLogger(LoadService.class);

    private final FlightDAO flightDAO;
    private final HotelDAO hotelDAO;
    private final BusDAO busDAO;
    private final CustomerDAO customerDAO;

    public LoadService() {
        this.flightDAO = new FlightDAO();
        this.hotelDAO = new HotelDAO();
        this.busDAO = new BusDAO();
        this.customerDAO = new CustomerDAO();
    }

    /**
     * 入库航班信息
     * @param flightNum 航班号
     * @param price 航班价格
     * @param numSeats 座位总数
     * @param numAvail 空座位数
     * @param fromCity 始发地
     * @param arivCity 到达地
     * @return 信息入库是否成功的消息
     */
    public Response loadFlightData(String flightNum, int price, int numSeats, int numAvail, String fromCity, String arivCity) {
        try {
            boolean loadDataResult = flightDAO.insert(flightNum, price, numSeats, numAvail, fromCity, arivCity);
            if (loadDataResult) {
                logger.info("Flight data loaded successfully for flight {}.", flightNum);
                return new Response(true, "Flight data loaded successfully");
            } else {
                logger.warn("Flight data loading failed for flight {}.", flightNum);
                return new Response(false, "Flight data loading failed");
            }
        } catch (FlightDAOException e) {
            logger.error("Error in FlightDAO while loading flight data: {}", flightNum, e);
            return new Response(false, "Error in FlightDAO while loading flight data");
        } catch (Exception e) {
            logger.error("Error while loading flight data: {}", flightNum, e);
            return new Response(false, "Error while loading flight data");
        }
    }

    /**
     * 入库酒店信息
     * @param location 酒店地点
     * @param price 酒店价格
     * @param numRooms 酒店总房间数
     * @param numAvail 酒店空房间数
     * @return 信息入库是否成功的消息
     */
    public Response loadHotelData(String location, int price, int numRooms, int numAvail) {
        try {
            boolean loadDataResult = hotelDAO.insert(location, price, numRooms, numAvail);
            if (loadDataResult) {
                logger.info("Hotel data loaded successfully for location {}.", location);
                return new Response(true, "Hotel data loaded successfully");
            } else {
                logger.warn("Hotel data loading failed for location {}.", location);
                return new Response(false, "Hotel data loading failed");
            }
        } catch (HotelDAOException e) {
            logger.error("Error in HotelDAO while loading hotel data: {}", location, e);
            return new Response(false, "Error in HotelDAO while loading hotel data");
        } catch (Exception e) {
            logger.error("Error while loading hotel data: {}", location, e);
            return new Response(false, "Error while loading hotel data");
        }
    }

    /**
     * 入库大巴信息
     * @param location 大巴地点
     * @param price 大巴价格
     * @param numBus 大巴总数
     * @param numAvail 空大巴数
     * @return 信息入库是否成功的消息
     */
    public Response loadBusData(String location, int price, int numBus, int numAvail) {
        try {
            boolean loadDataResult = busDAO.insert(location, price, numBus, numAvail);
            if (loadDataResult) {
                logger.info("Bus data loaded successfully for location {}.", location);
                return new Response(true, "Bus data loaded successfully");
            } else {
                logger.warn("Bus data loading failed for location {}.", location);
                return new Response(false, "Bus data loading failed");
            }
        } catch (BusDAOException e) {
            logger.error("Error in BusDAO while loading bus data: {}", location, e);
            return new Response(false, "Error in BusDAO while loading bus data");
        } catch (Exception e) {
            logger.error("Error while loading bus data: {}", location, e);
            return new Response(false, "Error while loading bus data");
        }
    }

    /**
     * 入库客户信息
     * @param custName 客户姓名
     * @param custID 客户ID
     * @return 信息入库是否成功的消息
     */
    public Response loadCustomerData(String custName, int custID) {
        try {
            boolean loadDataResult = customerDAO.insert(custName, custID);
            if (loadDataResult) {
                logger.info("Customer data loaded successfully for customer {}.", custName);
                return new Response(true, "Customer data loaded successfully");
            } else {
                logger.warn("Customer data loading failed for customer {}.", custName);
                return new Response(false, "Customer data loading failed");
            }
        } catch (CustomerDAOException e) {
            logger.error("Error in CustomerDAO while loading customer data: {}", custName, e);
            return new Response(false, "Error in CustomerDAO while loading customer data");
        } catch (Exception e) {
            logger.error("Error while loading customer data: {}", custName, e);
            return new Response(false, "Error while loading customer data");
        }
    }

    /**
     * 更新航班信息
     * @param flightNum 航班号
     * @param price 航班价格
     * @param numSeats 座位总数
     * @param numAvail 空座位数
     * @param fromCity 始发地
     * @param arivCity 到达地
     * @return 信息更新是否成功的消息
     */
    public Response updateFlightData(String flightNum, int price, int numSeats, int numAvail, String fromCity, String arivCity) {
        try {
            boolean updateResult = flightDAO.update(flightNum, price, numSeats, numAvail, fromCity, arivCity);
            if (updateResult) {
                logger.info("Flight data updated successfully for flight {}.", flightNum);
                return new Response(true, "Flight data updated successfully");
            } else {
                logger.warn("Flight data update failed for flight {}.", flightNum);
                return new Response(false, "Flight data update failed");
            }
        } catch (FlightDAOException e) {
            logger.error("Error in FlightDAO while updating flight data: {}", flightNum, e);
            return new Response(false, "Error in FlightDAO while updating flight data");
        } catch (Exception e) {
            logger.error("Error while updating flight data: {}", flightNum, e);
            return new Response(false, "Error while updating flight data");
        }
    }

    /**
     * 更新酒店信息
     * @param location 酒店地点
     * @param price 酒店价格
     * @param numRooms 酒店总房间数
     * @param numAvail 酒店空房间数
     * @return 信息更新是否成功的消息
     */
    public Response updateHotelData(String location, int price, int numRooms, int numAvail) {
        try {
            boolean updateResult = hotelDAO.update(location, price, numRooms, numAvail);
            if (updateResult) {
                logger.info("Hotel data updated successfully for location {}.", location);
                return new Response(true, "Hotel data updated successfully");
            } else {
                logger.warn("Hotel data update failed for location {}.", location);
                return new Response(false, "Hotel data update failed");
            }
        } catch (HotelDAOException e) {
            logger.error("Error in HotelDAO while updating hotel data: {}", location, e);
            return new Response(false, "Error in HotelDAO while updating hotel data");
        } catch (Exception e) {
            logger.error("Error while updating hotel data: {}", location, e);
            return new Response(false, "Error while updating hotel data");
        }
    }

    /**
     * 更新大巴信息
     * @param location 大巴地点
     * @param price 大巴价格
     * @param numBus 大巴总数
     * @param numAvail 空大巴数
     * @return 信息更新是否成功的消息
     */
    public Response updateBusData(String location, int price, int numBus, int numAvail) {
        try {
            boolean updateResult = busDAO.update(location, price, numBus, numAvail);
            if (updateResult) {
                logger.info("Bus data updated successfully for location {}.", location);
                return new Response(true, "Bus data updated successfully");
            } else {
                logger.warn("Bus data update failed for location {}.", location);
                return new Response(false, "Bus data update failed");
            }
        } catch (BusDAOException e) {
            logger.error("Error in BusDAO while updating bus data: {}", location, e);
            return new Response(false, "Error in BusDAO while updating bus data");
        } catch (Exception e) {
            logger.error("Error while updating bus data: {}", location, e);
            return new Response(false, "Error while updating bus data");
        }
    }

    /**
     * 更新客户信息
     * @param custName 客户姓名
     * @param custID 客户ID
     * @return 信息更新是否成功的消息
     */
    public Response updateCustomerData(String custName, int custID) {
        try {
            boolean updateResult = customerDAO.update(custName, custID);
            if (updateResult) {
                logger.info("Customer data updated successfully for customer {}.", custName);
                return new Response(true, "Customer data updated successfully");
            } else {
                logger.warn("Customer data update failed for customer {}.", custName);
                return new Response(false, "Customer data update failed");
            }
        } catch (CustomerDAOException e) {
            logger.error("Error in CustomerDAO while updating customer data: {}", custName, e);
            return new Response(false, "Error in CustomerDAO while updating customer data");
        } catch (Exception e) {
            logger.error("Error while updating customer data: {}", custName, e);
            return new Response(false, "Error while updating customer data");
        }
    }

}
