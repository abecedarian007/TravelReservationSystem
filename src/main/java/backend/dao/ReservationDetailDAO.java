package backend.dao;

import backend.entity.ObjectType;
import backend.exception.ReservationDetailDAOException;
import backend.exception.ReservationDetailNotFoundException;
import backend.exception.ReservationNotFoundException;
import backend.util.DBController;
import backend.entity.ReservationDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDetailDAO {
    private static final Logger logger = LoggerFactory.getLogger(ReservationDetailDAO.class);

    public List<ReservationDetail> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<ReservationDetail> reservationList = new ArrayList<>();

        String sql = "SELECT * FROM reservation_details";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservationList.add(new ReservationDetail(
                        resultSet.getInt("resvID"),
                        resultSet.getString("custName"),
                        ObjectType.valueOf(resultSet.getInt("resvType")),
                        resultSet.getString("resvKey"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("availability"),
                        resultSet.getString("locationFrom"),
                        resultSet.getString("locationTo"),
                        resultSet.getInt("price")
                ));
            }

            logger.info("Query all reservation details successful");
            return reservationList;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query all reservation details", e);
            throw new ReservationDetailDAOException("IllegalArgument [ReservationType] Error during query all reservation details", e);
        } catch (SQLException e) {
            logger.error("SQL Error during query all reservations details", e);
            throw new ReservationDetailDAOException("SQL Error during query all reservation", e);
        } catch (Exception e) {
            logger.error("Error during query all reservation details", e);
            throw new ReservationDetailDAOException("Error during query all reservation details", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public ReservationDetail query(int resvID) throws Exception {
        Connection connection = DBController.getConnection();
        ReservationDetail reservation = new ReservationDetail();

        String sql = "SELECT * FROM reservation_details WHERE resvID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, resvID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation.setResvID(resultSet.getInt("resvID"));
                reservation.setCustName(resultSet.getString("custName"));
                reservation.setResvType(ObjectType.valueOf(resultSet.getInt("resvType")));
                reservation.setResvKey(resultSet.getString("resvKey"));
                reservation.setQuantity(resultSet.getInt("quantity"));
                reservation.setAvailability(resultSet.getInt("availability"));
                reservation.setLocationFrom(resultSet.getString("locationFrom"));
                reservation.setLocationTo(resultSet.getString("locationTo"));
                reservation.setPrice(resultSet.getInt("price"));
                logger.info("Query operation successful for resvID: {}", resvID);
            } else {
                logger.warn("No reservation detail found for resvID: {}", resvID);
                throw new ReservationDetailNotFoundException("No reservation detail found for resvID: " + resvID);
            }

            return reservation;
        } catch (ReservationDetailNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDetailDAOException("IllegalArgument [ReservationType] Error during query operation for resvID: " + resvID, e);
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDetailDAOException("SQL Error during query operation for resvID: " + resvID, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for resvID: {}", resvID, e);
            throw new ReservationDetailDAOException("Other Error during query operation for resvID: " + resvID, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public List<ReservationDetail> query(String customerName) throws Exception {
        Connection connection = DBController.getConnection();
        List<ReservationDetail> reservationList = new ArrayList<>();

        String sql = "SELECT * FROM reservation_details WHERE custName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservationList.add(new ReservationDetail(
                        resultSet.getInt("resvID"),
                        resultSet.getString("custName"),
                        ObjectType.valueOf(resultSet.getInt("resvType")),
                        resultSet.getString("resvKey"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("availability"),
                        resultSet.getString("locationFrom"),
                        resultSet.getString("locationTo"),
                        resultSet.getInt("price")
                ));
            }

            if (reservationList.isEmpty()) {
                logger.warn("No reservation detail found for customer: {}", customerName);
                throw new ReservationDetailNotFoundException("No reservation detail found for customer: " + customerName);
            }

            logger.info("Query operation successful for customerName: {}", customerName);
            return reservationList;
        } catch (ReservationDetailNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgument [ReservationType] Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDetailDAOException("IllegalArgument [ReservationType] Error during query operation for customerName: " + customerName, e);
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDetailDAOException("SQL Error during query operation for customerName: " + customerName, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for customerName: {}", customerName, e);
            throw new ReservationDetailDAOException("Other Error during query operation for customerName: " + customerName, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}
