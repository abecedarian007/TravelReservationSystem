package backend.dao;

import backend.exception.BusNotFoundException;
import backend.exception.CustomerDAOException;
import backend.exception.CustomerNotFoundException;
import backend.util.DBController;
import backend.entity.Customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    public boolean insert(String custName, int custID) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO CUSTOMERS (custName, custID) VALUES (?, ?)" +
                " ON DUPLICATE KEY UPDATE custID = VALUES(custID)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, custName);
            preparedStatement.setInt(2, custID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Insert operation successful for custName: {}", custName);
                return true;
            } else {
                connection.rollback();
                logger.warn("Insert operation failed for custName: {}", custName);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during insert operation for custName: {}", custName, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during insert operation for custName: {}", custName, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public boolean update(String custName, int custID) throws Exception {
        Connection connection = DBController.getConnection();
        connection.setAutoCommit(false);

        String sql = "UPDATE CUSTOMERS " +
                " SET custID = ?" +
                " WHERE custName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, custID);
            preparedStatement.setString(2, custName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                logger.info("Update operation successful for custName: {}", custName);
                return true;
            } else {
                connection.rollback();
                logger.warn("Update operation failed for custName: {}", custName);
                return false;
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error("SQL Error during update operation for custName: {}", custName, e);
            return false;
        } catch (Exception e) {
            connection.rollback();
            logger.error("Other Error during update operation for custName: {}", custName, e);
            return false;
        } finally {
            connection.setAutoCommit(true);
            DBController.closeConnection(connection);
        }
    }

    public List<Customer> queryAll() throws Exception {
        Connection connection = DBController.getConnection();
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM CUSTOMERS";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(new Customer(
                        resultSet.getString("custName"),
                        resultSet.getInt("custID")
                ));
            }

            logger.info("Query all customers successful");
            return customers;
        } catch (SQLException e) {
            logger.error("SQL Error during query all customers", e);
            throw new CustomerDAOException("SQL Error during query all customers", e);
        } catch (Exception e) {
            logger.error("Other Error during query all customers", e);
            throw new CustomerDAOException("Other Error during query all customers", e);
        } finally {
            DBController.closeConnection(connection);
        }
    }

    public Customer query(String custName) throws Exception {
        Connection connection = DBController.getConnection();
        Customer customer = new Customer();

        String sql = "SELECT * FROM CUSTOMERS WHERE custName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, custName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer.setCustName(resultSet.getString("custName"));
                customer.setCustID(resultSet.getInt("custID"));
                logger.info("Query operation successful for custName: {}", custName);
            } else {
                logger.warn("No customer found for custName: {}", custName);
                throw new CustomerNotFoundException("No customer found for custName: " + custName);
            }

            return customer;
        } catch (CustomerNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("SQL Error during query operation for custName: {}", custName, e);
            throw new CustomerDAOException("SQL Error during query operation for custName: " + custName, e);
        } catch (Exception e) {
            logger.error("Other Error during query operation for custName: {}", custName, e);
            throw new CustomerDAOException("Other Error during query operation for custName: " + custName, e);
        } finally {
            DBController.closeConnection(connection);
        }
    }
}