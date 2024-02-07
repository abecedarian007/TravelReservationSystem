package backend.dao;

import backend.exception.FlightDAOException;
import backend.exception.FlightNotFoundException;
import backend.util.DBController;
import backend.entity.Flight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {
    private static final Logger logger = LoggerFactory.getLogger(FlightDAO.class);

    public boolean insert(String flightNum, int price, int numSeats, int numAvail, String fromCity, String arivCity) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO FLIGHTS (flightNum, price, numSeats, numAvail, FromCity, ArivCity) VALUES (?, ?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE price = VALUES(price), numSeats = VALUES(numSeats), numAvail = VALUES(numAvail)," +
                " FromCity = VALUES(FromCity), ArivCity = VALUES(ArivCity)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, flightNum);
            preparedStatement.setInt(2, price);
            preparedStatement.setInt(3, numSeats);
            preparedStatement.setInt(4, numAvail);
            preparedStatement.setString(5, fromCity);
            preparedStatement.setString(6, arivCity);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Insert operation successful for flightNum: {}", flightNum);
                return true;
            } else {
                connection.rollback();
                logger.warn("Insert operation failed for flightNum: {}", flightNum);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during insert operation for flightNum: {}", flightNum, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during insert operation for flightNum: {}", flightNum, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public boolean update(String flightNum, int price, int numSeats, int numAvail, String fromCity, String arivCity) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "UPDATE FLIGHTS " +
                " SET price = ?, numSeats = ?, numAvail = ?, FromCity = ?, ArivCity = ?" +
                " WHERE flightNum = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, numSeats);
            preparedStatement.setInt(3, numAvail);
            preparedStatement.setString(4, fromCity);
            preparedStatement.setString(5, arivCity);
            preparedStatement.setString(6, flightNum);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Update operation successful for flightNum: {}", flightNum);
                return true;
            } else {
                connection.rollback();
                logger.warn("Update operation failed for flightNum: {}", flightNum);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during update operation for flightNum: {}", flightNum, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during update operation for flightNum: {}", flightNum, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public List<Flight> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<Flight> flights = new ArrayList<>();

        String sql = "SELECT * FROM FLIGHTS";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                flights.add(new Flight(
                        resultSet.getString("flightNum"),
                        resultSet.getInt("price"),
                        resultSet.getInt("numSeats"),
                        resultSet.getInt("numAvail"),
                        resultSet.getString("FromCity"),
                        resultSet.getString("ArivCity")
                ));
            }

            logger.info("Query all flights successful");
            return flights;
        } catch (SQLException e) {
            logger.error("SQL Error during query all flights", e);
            throw new FlightDAOException("SQL Error during query all flights", e);
        } catch (Exception e) {
            logger.error("Other Error during query all flights", e);
            throw new FlightDAOException("Other Error during query all flights", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public Flight query(String flightNum) throws Exception {
        Connection connection = DBController.getConnection();
        Flight flight = new Flight();

        String sql = "SELECT * FROM FLIGHTS WHERE flightNum = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, flightNum);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                flight.setFlightNum(resultSet.getString("flightNum"));
                flight.setPrice(resultSet.getInt("price"));
                flight.setNumSeats(resultSet.getInt("numSeats"));
                flight.setNumAvail(resultSet.getInt("numAvail"));
                flight.setFromCity(resultSet.getString("FromCity"));
                flight.setArivCity(resultSet.getString("ArivCity"));
                logger.info("Query operation successful for flightNum: {}", flightNum);
            } else {
                logger.warn("No flight found for flightNum: {}", flightNum);
                throw new FlightNotFoundException("No flight found for flightNum: " + flightNum);
            }

            return flight;
        } catch (FlightNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for flightNum: {}", flightNum, e);
            throw new FlightDAOException("SQL Error during query operation for flightNum: " + flightNum, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for flightNum: {}", flightNum, e);
            throw new FlightDAOException("Other Error during query operation for flightNum: " + flightNum, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}
