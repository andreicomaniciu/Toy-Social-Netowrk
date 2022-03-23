package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Group;
import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDbRepo extends GroupRepo{
    private String url;
    private String username;
    private String password;
    private String tableName;


    public GroupDbRepo(String url, String username, String password, String tableName) throws Exception {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        testData();
    }

    private void testData() throws Exception {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String lastName = resultSet.getString("nume");
                String useri = resultSet.getString("useri");
                String[] s=useri.split(" ");
                List<String> list = new ArrayList<>();
                for(var x:s){
                    list.add(x);
                }

                Group grup=new Group(list,ID);
                grup.setNume(lastName);
            }

        } catch(SQLException e) {
            throw new UserRepoValidator(e.getMessage());
        }

    }

    @Override
    public void add(Group grup){

        String sql = "insert into " + tableName + "  (id, nume, useri) values ( ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String list="";
            for(var  x:grup.getId()){
                if(list.equals(""))
                    list=x;
                else
                    list=list+" "+x;
            }


            preparedStatement.setString(1, grup.getId_grup());
            preparedStatement.setString(2, grup.getNume());
            preparedStatement.setString(3, list);

            preparedStatement.executeUpdate();

        } catch (SQLException e){

        }

        //super.add(user);
    }

    @Override
    public Group getGroup(String ID){

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String lastName = resultSet.getString("nume");
                String useri = resultSet.getString("useri");
                String[] s=useri.split(" ");
                List<String> list = new ArrayList<>();
                for(var x:s){
                    list.add(x);
                }

                Group grup=new Group(list,id);
                grup.setNume(lastName);

                if(ID.equals(id)){
                    return grup;
                }
            }
        } catch(SQLException e) {

        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public ArrayList<Group> getAll(){
        ArrayList<Group> groups = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String lastName = resultSet.getString("nume");
                String useri = resultSet.getString("useri");
                String[] s=useri.split(" ");
                List<String> list = new ArrayList<>();
                for(var x:s){
                    list.add(x);
                }

                Group grup=new Group(list,ID);
                grup.setNume(lastName);

                groups.add(grup);

            }

        } catch(SQLException e) {
        }

        return groups;
        //return super.getAll();
    }

    @Override
    public ArrayList<Group> getAllFromCurrent(String id){
        ArrayList<Group> groups = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String lastName = resultSet.getString("nume");
                String useri = resultSet.getString("useri");
                String[] s=useri.split(" ");
                List<String> list = new ArrayList<>();
                for(var x:s){
                    list.add(x);
                }

                Group grup=new Group(list,ID);
                grup.setNume(lastName);
                if(grup.getId().contains(id))
                    groups.add(grup);
            }

        } catch(SQLException e) {
        }

        return groups;
        //return super.getAll();
    }
}
