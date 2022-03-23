package com.example.curs9.ubbcluj.map.MyRepos;

import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageDbRepo extends MessageRepo{
    private String url;
    private String username;
    private String password;
    private String tableName;
    private MessageValidator messageValidator;

    public MessageDbRepo(String url, String username, String password, String tableName) throws Exception {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        this.messageValidator = new MessageValidator();
        testData();
    }

    private void testData() throws Exception {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String ID = resultSet.getString("id");
                String id_from = resultSet.getString("expeditor");
                String id_to = resultSet.getString("destinatar");
                String mesaj=resultSet.getString("mesaj");
                String data=resultSet.getString("dataa");
                String id_reply=resultSet.getString("id_reply");

                Message message=new Message(ID,id_from,id_to,mesaj,data,id_reply);
                messageValidator.validateMessage(message);
            }

        } catch(SQLException e) {
            throw new MessageRepoValidator(e.getMessage());
        }

    }

    @Override
    public void add(Message message) throws MessageRepoValidator {

        String sql = "insert into " + tableName + "  ( id, expeditor, destinatar,mesaj,dataa,id_reply) values ( ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, message.getId_mesaj());
            preparedStatement.setString(2, message.getId_From());
            preparedStatement.setString(3, message.getId_To());
            preparedStatement.setString(4, message.getMesaj());
            preparedStatement.setString(5, message.getData());
            preparedStatement.setString(6, message.getReply());


            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new MessageRepoValidator(e.getMessage());
        }

        //super.add(user);
    }

    @Override
    public void delete(String ID) throws MessageRepoValidator {

        String sql = "delete from " + tableName + " where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ID);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new MessageRepoValidator(e.getMessage());
        }

        //super.delete(ID);
    }

    @Override
    public Message getMessage(String ID) throws MessageRepoValidator {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String id = resultSet.getString("id");
                String from = resultSet.getString("expeditor");
                String to = resultSet.getString("destinatar");
                String mesaj = resultSet.getString("mesaj");
                String date = resultSet.getString("dataa");
                String reply = resultSet.getString("id_reply");

                Message message=new Message(id,from,to,mesaj,date,reply);

                if (ID.equals(id)) {
                    return message;
                }
            }

        } catch(SQLException e) {
            throw new MessageRepoValidator(e.getMessage());
        }
        return null;
        //return super.getUser(ID);
    }

    @Override
    public ArrayList<Message> getAll() throws MessageRepoValidator {
        ArrayList<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String id = resultSet.getString("id");
                String from = resultSet.getString("expeditor");
                String to = resultSet.getString("destinatar");
                String mesaj = resultSet.getString("mesaj");
                String date = resultSet.getString("dataa");
                String reply = resultSet.getString("id_reply");

                Message message=new Message(id,from,to,mesaj,date,reply);

                messages.add(message);

            }

        } catch(SQLException e) {
            throw new MessageRepoValidator(e.getMessage());
        }

        return messages;
        //return super.getAll();
    }

    @Override
    public ArrayList<Message> getConversation(List<String> ids) throws MessageRepoValidator {
        ArrayList<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String id = resultSet.getString("id");
                String from = resultSet.getString("expeditor");
                String to = resultSet.getString("destinatar");
                String mesaj = resultSet.getString("mesaj");
                String date = resultSet.getString("dataa");
                String reply = resultSet.getString("id_reply");

                Message message=new Message(id,from,to,mesaj,date,reply);

                String id1=from+" "+to;
                String[] s=id1.split(" ");


                    List<String> list = new ArrayList<>();
                    for(var y:s){
                        list.add(y);
                    }
                    if(ids.size() ==2){
                        if((from.equals(ids.get(0)) && to.equals(ids.get(1))) ||(from.equals(ids.get(1)) && to.equals(ids.get(0)))){
                            messages.add(message);
                        }
                    }
                    else{
                        int nrElemListaIds=ids.size();
                        int nrElemCurrent=0;
                        for(var z:list){
                            for(var a:ids){
                                if(z.equals(a))
                                    nrElemCurrent++;
                            }
                        }
                        if(nrElemCurrent==nrElemListaIds)
                            messages.add(message);
                    }


            }

        } catch(SQLException e) {
            throw new MessageRepoValidator(e.getMessage());
        }

        Comparator<Message> compareByDate =
                (Message o1, Message o2) -> o1.getData().compareTo( o2.getData() );

        messages.sort(compareByDate);
        return messages;
        //return super.getAll();
    }

    @Override
    public ArrayList<Message> getMessagesToRespond(String id1) throws MessageRepoValidator {
        ArrayList<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String id = resultSet.getString("id");
                String from = resultSet.getString("expeditor");
                String to = resultSet.getString("destinatar");
                String mesaj = resultSet.getString("mesaj");
                String date = resultSet.getString("dataa");
                String reply = resultSet.getString("id_reply");

                Message message=new Message(id,from,to,mesaj,date,reply);
                if(message.getId_To().contains(" ")){
                    String s = message.getId_To();
                    String[] tokens = s.split(" ");
                    for (String t : tokens)
                        if(t.equals(id1))
                            messages.add(message);
                }
                else
                    if(message.getId_To().equals(id1) && reply.equals("fara raspuns"))
                        messages.add(message);
            }

        } catch(SQLException e) {
            throw new MessageRepoValidator(e.getMessage());
        }
        Comparator<Message> compareByDate =
                (Message o1, Message o2) -> o1.getData().compareTo( o2.getData() );

        messages.sort(compareByDate);
        return messages;
        //return super.getAll();
    }
}
