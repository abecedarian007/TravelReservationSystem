package backend.dao;

import backend.exception.CustomerNotFoundException;
import backend.exception.FlightNotFoundException;
import backend.exception.HotelDAOException;
import backend.exception.HotelNotFoundException;
import backend.util.DBController;
import backend.entity.Hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {
    private static final Logger logger = LoggerFactory.getLogger(HotelDAO.class);

    public boolean insert(String location, int price, int numRooms, int numAvail) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO HOTELS (location, price, numRooms, numAvail) VALUES (?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE price = VALUES(price), numRooms = VALUES(numRooms), numAvail = VALUES(numAvail)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, location);
            preparedStatement.setInt(2, price);
            preparedStatement.setInt(3, numRooms);
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

    public boolean update(String location, int price, int numRooms, int numAvail) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "UPDATE HOTELS " +
                " SET price = ?, numRooms = ?, numAvail = ?" +
                " WHERE location = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, numRooms);
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

    public List<Hotel> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<Hotel> hotels = new ArrayList<>();

        String sql = "SELECT * FROM HOTELS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                hotels.add(new Hotel(
                        resultSet.getString("location"),
                        resultSet.getInt("price"),
                        resultSet.getInt("numRooms"),
                        resultSet.getInt("numAvail")
                ));
            }

            logger.info("Query all hotels successful");
            return hotels;
        } catch (SQLException e) {
            logger.error("SQL Error during query all hotels", e);
            throw new HotelDAOException("SQL Error during query all hotels", e);
        } catch (Exception e) {
            logger.error("Other Error during query all hotels", e);
            throw new HotelDAOException("Other Error during query all hotels", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public Hotel query(String location) throws Exception {
        Connection connection = DBController.getConnection();
        Hotel hotel = new Hotel();

        String sql = "SELECT * FROM HOTELS WHERE location = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, location);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                hotel.setLocation(resultSet.getString("location"));
                hotel.setPrice(resultSet.getInt("price"));
                hotel.setNumRooms(resultSet.getInt("numRooms"));
                hotel.setNumAvail(resultSet.getInt("numAvail"));
                logger.info("Query operation successful for location: {}", location);
            } else {
                logger.warn("No hotel found for location: {}", location);
                throw new HotelNotFoundException("No hotel found for location: " + location);
            }

            return hotel;
        } catch (HotelNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for location: {}", location, e);
            throw new HotelDAOException("SQL Error during query operation for location: " + location, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for location: {}", location, e);
            throw new HotelDAOException("Other Error during query operation for location: " + location, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}
