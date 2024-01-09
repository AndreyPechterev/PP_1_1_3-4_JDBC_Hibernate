package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS users (\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    name VARCHAR(255),\n" +
                "    lastName VARCHAR(255),\n" +
                "    age SMALLINT\n" +
                ");";
        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(createTable);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Fail to create table");
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }


    }

    public void dropUsersTable() throws SQLException {
        String dropTable = "DROP TABLE IF EXISTS users";
        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(dropTable);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Fail to drop table");
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String saveUser = "INSERT INTO users(name,lastname,age) values(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(saveUser)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Fail to create table");
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void removeUserById(long id) throws SQLException {
        String removeUser = "DELETE FROM users WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(removeUser)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Fail to delete user with id " + id);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> listUsers = new ArrayList<>();
        String getAll = "SELECT * FROM users";
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(getAll);
            while (rs.next()){
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                listUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Fail to create table");
            connection.rollback();
        }
        return listUsers;
    }

    public void cleanUsersTable() throws SQLException {
        String cleanTable = "TRUNCATE TABLE users";
        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(cleanTable);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Fail to clean table");
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
