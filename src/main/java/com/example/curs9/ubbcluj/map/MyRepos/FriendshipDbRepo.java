package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipValidator;

import java.sql.*;
import java.util.ArrayList;

public class FriendshipDbRepo extends FriendshipRepo {
    private String url;
    private String username;
    private String password;
    private String tableName;
    private FriendshipValidator friendshipValidator;

    public FriendshipDbRepo(String url, String username, String password, String tableName) throws Exception {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        this.friendshipValidator = new FriendshipValidator();
        testData();
    }

    private void testData() throws Exception {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID1 = resultSet.getString("id1");
                String ID2 = resultSet.getString("id2");
                Date date = resultSet.getDate("date_of");
                String status = resultSet.getString("status");

                Friendship friendship = new Friendship(ID1, ID2, date, status);
                friendshipValidator.validateFriendship(friendship);
            }

        } catch(SQLException e) {
            throw new FriendshipRepoValidator(e.getMessage());
        }

    }

    @Override
    public void add(Friendship friendship) throws FriendshipRepoValidator {

        var x = getFriendship(friendship.getId1(), friendship.getId2());
        var y = getFriendship(friendship.getId2(), friendship.getId1());

        if (!(x == null && y == null)) {
            if (!(x == null)) {
                if (x.isStatus_Pending()) {
                    throw new FriendshipRepoValidator("Cererea de prietenie exista deja!");
                }
                else {
                    throw new FriendshipRepoValidator("Prietenia exista deja!");
                }
            }
            else
            {
                if (y.isStatus_Pending()) {
                    throw new FriendshipRepoValidator("Cererea de prietenie exista deja!");
                }
                else {
                    throw new FriendshipRepoValidator("Prietenia exista deja!");
                }
            }
        }

        String sql = "insert into " + tableName + "  (id1, id2, date_of, status) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, friendship.getId1());
            preparedStatement.setString(2, friendship.getId2());
            preparedStatement.setDate(3, friendship.getDateOfFriendshipRequest());
            preparedStatement.setString(4, friendship.getStatus());


            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new FriendshipRepoValidator(e.getMessage());
        }

        //super.add(user);
    }

    @Override
    public void delete(String ID1, String ID2) throws FriendshipRepoValidator {

        int t = 1, t1 = 1;

        String sql = "delete from " + tableName + " where (id1, id2) = (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ID1);
            preparedStatement.setString(2, ID2);

            t = preparedStatement.executeUpdate();

        } catch (SQLException e){
            // throw new FriendshipRepoValidator(e.getMessage());
        }

        String sql1 = "delete from " + tableName + " where (id2, id1) = (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql1)) {

            preparedStatement.setString(1, ID1);
            preparedStatement.setString(2, ID2);

            t1 = preparedStatement.executeUpdate();

        } catch (SQLException e){
            //throw new FriendshipRepoValidator(e.getMessage());
        }

        if (t == 0 && t1 == 0) {
            throw new FriendshipRepoValidator("\nPrietenia nu exista!\n");
        }

        //super.delete(ID);
    }

    public Friendship update(Friendship friendshipVechi, Friendship friendshipNou) throws FriendshipRepoValidator {

        String sql = "update " + tableName + " set prenume = ?, nume = ? where id = ?";

        delete(friendshipVechi.getId1(), friendshipVechi.getId2());
        add(friendshipNou);

        return null;
    }

    public Friendship getFriendship(String ID1, String ID2) throws FriendshipRepoValidator {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String Id1 = resultSet.getString("id1");
                String Id2 = resultSet.getString("id2");
                Date date = resultSet.getDate("date_of");
                String status = resultSet.getString("status");

                Friendship friendship = new Friendship(Id1, Id2, date, status);

                if ((friendship.getId1().equals(ID1) && friendship.getId2().equals(ID2)) || (friendship.getId1().equals(ID2) && friendship.getId2().equals(ID1))) {
                    return friendship;
                }
            }

        } catch(SQLException e) {
            throw new FriendshipRepoValidator(e.getMessage());
        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public ArrayList<Friendship> getAll() throws FriendshipRepoValidator {
        ArrayList<Friendship> friendships = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID1 = resultSet.getString("id1");
                String ID2 = resultSet.getString("id2");
                Date date = resultSet.getDate("date_of");
                String status = resultSet.getString("status");

                Friendship friendship = new Friendship(ID1, ID2, date, status);

                friendships.add(friendship);

            }

        } catch(SQLException e) {
            throw new FriendshipRepoValidator(e.getMessage());
        }

        return friendships;
        //return super.getAll();
    }

    @Override
    public void deleteFlagged(String ID) throws FriendshipRepoValidator {
        ArrayList<Friendship> friendships1 = new ArrayList<>();
        for (var x : getAll()) {
            if ((x.hasUserByID(ID))) {
                friendships1.add(x);
            }
        }

        for (var y : friendships1) {
            delete(y.getId1(), y.getId2());
        }

        //super.deleteFlagged(ID);
    }

    @Override
    public ArrayList<Friendship> getAllOfUserID(String userID) throws FriendshipRepoValidator {
        var allFriendships = getAll();

        ArrayList<Friendship> friendshipsOfUser = new ArrayList<>();

        for (var y : allFriendships) {
            if (y.hasUserByID(userID)) {
                friendshipsOfUser.add(y);
            }
        }

        return friendshipsOfUser;

        //return super.getAllOf(user);
    }
}
