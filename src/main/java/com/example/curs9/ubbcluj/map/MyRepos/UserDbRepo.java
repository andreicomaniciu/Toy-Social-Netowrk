package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserValidator;

import java.sql.*;
import java.util.ArrayList;

public class UserDbRepo extends UserRepo {
    private String url;
    private String username;
    private String password;
    private String tableName;
    private UserValidator userValidator;

    public UserDbRepo(String url, String username, String password, String tableName) throws Exception {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        this.userValidator = new UserValidator();
        testData();
    }

    private void testData() throws Exception {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String firstName = resultSet.getString("prenume");
                String lastName = resultSet.getString("nume");
                String email = resultSet.getString("email");

//                User user = new User(ID, firstName, lastName);
                User user = new User(ID, firstName, lastName, email);
                userValidator.validateUser(user);
            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }

    }

    @Override
    public void add(User user) throws UserRepoValidator {

        String sql = "insert into " + tableName + "  (id, prenume, nume, email) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new UserRepoValidator(e.getMessage());
        }

        //super.add(user);
    }

    @Override
    public void delete(String ID) throws UserRepoValidator {

        String sql = "delete from " + tableName + " where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ID);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new UserRepoValidator(e.getMessage());
        }

        //super.delete(ID);
    }

    public User update(User user) throws UserRepoValidator {

        String sql = "update " + tableName + " set prenume = ?, nume = ?, email = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new UserRepoValidator(e.getMessage());
        }

        return null;
    }

    @Override
    public User getUser(String ID) throws UserRepoValidator {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID1 = resultSet.getString("id");
                String firstName = resultSet.getString("prenume");
                String lastName = resultSet.getString("nume");
                String email = resultSet.getString("email");

                User user = new User(ID, firstName, lastName, email);

                if (ID.equals(ID1)) {
                    return user;
                }
            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public String getSaltByEmail(String emailCurrent) throws UserRepoValidator {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String email = resultSet.getString("email");
                String salt = resultSet.getString("salt");
                String hash = resultSet.getString("hash");

                if (email.equals(emailCurrent)) {
                    return salt;
                }
            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public String getHashByEmail(String emailCurrent) throws UserRepoValidator {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String email = resultSet.getString("email");
                String salt = resultSet.getString("salt");
                String hash = resultSet.getString("hash");

                if (email.equals(emailCurrent)) {
                    return hash;
                }
            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public ArrayList<User> getAll() throws UserRepoValidator {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String firstName = resultSet.getString("prenume");
                String lastName = resultSet.getString("nume");
                String email = resultSet.getString("email");

                User user = new User(ID, firstName, lastName, email);

                users.add(user);

            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }

        return users;
        //return super.getAll();
    }
}
