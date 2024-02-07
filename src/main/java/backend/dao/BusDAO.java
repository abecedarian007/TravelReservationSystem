package backend.dao;

import backend.exception.BusNotFoundException;
import backend.exception.CustomerNotFoundException;
import backend.util.DBController;
import backend.entity.Bus;
import backend.exception.BusDAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BusDAO {
    private static final Logger logger = LoggerFactory.getLogger(BusDAO.class);

    public boolean insert(String location, int price, int numBus, int numAvail) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO BUS (location, price, numBus, numAvail) VALUES (?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE price = VALUES(price), numBus = VALUES(numBus), numAvail = VALUES(numAvail)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, location);
            preparedStatement.setInt(2, price);
            preparedStatement.setInt(3, numBus);
            preparedStatement.setInt(4, numAvail);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Insert operation successful for location: {}", location);
                return true;
            } else {
                connection.rollback();
                logger.warn("Insert operation failed for location: {}", location);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during insert operation for location: {}", location, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during insert operation for location: {}", location, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public boolean update(String location, int price, int numBus, int numAvail) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "UPDATE BUS " +
                " SET price = ?, numBus = ?, numAvail = ?" +
                " WHERE location = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, numBus);
            preparedStatement.setInt(3, numAvail);
            preparedStatement.setString(4, location);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Update operation successful for location: {}", location);
                return true;
            } else {
                connection.rollback();
                logger.warn("Update operation failed for location: {}", location);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during update operation for location: {}", location, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during update operation for location: {}", location, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public List<Bus> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<Bus> buses = new ArrayList<>();

        String sql = "SELECT * FROM BUS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                buses.add(new Bus(
                        resultSet.getString("location"),
                        resultSet.getInt("price"),
                        resultSet.getInt("numBus"),
                        resultSet.getInt("numAvail")
                ));
            }

            logger.info("Query all buses successful");
            return buses;
        } catch (SQLException e) {
            logger.error("SQL Error during query all buses", e);
            throw new BusDAOException("SQL Error during query all buses", e);
        } catch (Exception e) {
            logger.error("Other Error during query all buses", e);
            throw new BusDAOException("Other Error during query all buses", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public Bus query(String location) throws Exception {
        Connection connection = DBController.getConnection();
        Bus bus = new Bus();

        String sql = "SELECT * FROM BUS WHERE location = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, location);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bus.setLocation(resultSet.getString("location"));
                bus.setPrice(resultSet.getInt("price"));
                bus.setNumBus(resultSet.getInt("numBus"));
                bus.setNumAvail(resultSet.getInt("numAvail"));
                logger.info("Query operation successful for location: {}", location);
            } else {
                logger.warn("No bus found for location: {}", location);
                throw new BusNotFoundException("No bus found for location: " + location);
            }

            return bus;
        } catch (BusNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for location: {}", location, e);
            throw new BusDAOException("SQL Error during query operation for location: " + location, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for location: {}", location, e);
            throw new BusDAOException("Other Error during query operation for location: " + location, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}
