package backend.dao;

import backend.entity.Reservation;
import backend.entity.ObjectType;
import backend.exception.HotelNotFoundException;
import backend.exception.ReservationDAOException;
import backend.exception.ReservationNotFoundException;
import backend.util.DBController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final Logger logger = LoggerFactory.getLogger(ReservationDAO.class);

    public boolean insert(String customerName, ObjectType resvType, String resvKey) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO RESERVATIONS (custName, resvType, resvKey) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, resvType.getValue());
            preparedStatement.setString(3, resvKey);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Insert operation successful for customerName: {}", customerName);
                return true;
            } else {
                connection.rollback();
                logger.warn("Insert operation failed for customerName: {}", customerName);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during insert operation for customerName: {}", customerName, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during insert operation for customerName: {}", customerName, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public List<Reservation> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<Reservation> reservationList = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservationList.add(new Reservation(
                        resultSet.getInt("resvID"),
                        resultSet.getString("custName"),
                        ObjectType.valueOf(resultSet.getInt("resvType")),
                        resultSet.getString("resvKey")
                ));
            }

            logger.info("Query all reservations successful");
            return reservationList;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query all reservation", e);
            throw new ReservationDAOException("IllegalArgument [ReservationType] Error during query all reservation", e);
        } catch (SQLException e) {
            logger.error("SQL Error during query all reservations", e);
            throw new ReservationDAOException("SQL Error during query all reservation", e);
        } catch (Exception e) {
            logger.error("Other Error during query all reservation", e);
            throw new ReservationDAOException("Other Error during query all reservation", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public Reservation query(int resvID) throws Exception {
        Connection connection = DBController.getConnection();
        Reservation reservation = new Reservation();

        String sql = "SELECT * FROM RESERVATIONS WHERE resvID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, resvID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation.setResvID(resultSet.getInt("resvID"));
                reservation.setCustName(resultSet.getString("custName"));
                reservation.setResvType(ObjectType.valueOf(resultSet.getInt("resvType")));
                reservation.setResvKey(resultSet.getString("resvKey"));
                logger.info("Query operation successful for resvID: {}", resvID);
            } else {
                logger.warn("No reservation found for resvID: {}", resvID);
                throw new ReservationNotFoundException("No reservation found for resvID: " + resvID);
            }

            return reservation;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDAOException("IllegalArgument [ReservationType] Error during query operation for resvID: " + resvID, e);
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDAOException("SQL Error during query operation for resvID: " + resvID, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDAOException("Other Error during query operation for resvID: " + resvID, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public List<Reservation> query(String customerName) throws Exception {
        Connection connection = DBController.getConnection();
        List<Reservation> reservationList = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS WHERE custName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservationList.add(new Reservation(
                        resultSet.getInt("resvID"),
                        resultSet.getString("custName"),
                        ObjectType.valueOf(resultSet.getInt("resvType")),
                        resultSet.getString("resvKey")
                ));
            }

            if (reservationList.isEmpty()) {
                logger.warn("No reservation found for customer: {}", customerName);
                throw new ReservationNotFoundException("No reservation found for customer: " + customerName);
            }

            logger.info("Query operation successful for customerName: {}", customerName);
            return reservationList;
        } catch (ReservationNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDAOException("IllegalArgument [ReservationType] Error during query operation for customerName: " + customerName, e);
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDAOException("SQL Error during query operation for customerName: " + customerName, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDAOException("Other Error during query operation for customerName: " + customerName, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}
