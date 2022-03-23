package com.example.curs9;

import com.example.curs9.ubbcluj.map.MyRepos.*;
import com.example.curs9.ubbcluj.map.domain.MyModels.Group;
import com.example.curs9.ubbcluj.map.domain.MyUtils.PdfCreator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.curs9.ubbcluj.map.service.NetworkService;

import java.io.IOException;

public class StarterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, UserRepoValidator, FriendshipRepoValidator, InterruptedException, MessageRepoValidator{

        FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 350);
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        /*

        String url = "jdbc:postgresql://localhost:5432/Andreii";
        String user = "postgres";
        String pass = "Trafic11";
        String usersTableName = "\"users\"";
        String friendshipsTableName = "\"friendships\"";
        String messagesTableName= "\"message\"";
*/

        /**/
        String url = "jdbc:postgresql://localhost:5432/lab4";
        String user = "postgres";
        String pass = "1234";
        String usersTableName = "\"Users\"";
        String friendshipsTableName = "\"Friendships\"";
        String messagesTableName = "\"Messages\"";
        String groupTableName = "\"Groups\"";



        /*
        String url = "jdbc:postgresql://localhost:5432/Andreii";
        String user = "postgres";
        String pass = "postgres";
        String usersTableName = "\"users\"";
        String friendshipsTableName = "\"friendships\"";
        String messagesTableName = "\"message\"";
        String groupTableName = "\"groups\"";*/



        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator=new MessageValidator();

        //UserRepo userRepo = new UserRepo();

        UserDbRepo userDbRepo = null;
        try {
            userDbRepo = new UserDbRepo(url, user, pass, usersTableName);
        } catch (UserRepoValidator e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        FriendshipRepo friendshipRepo = new FriendshipRepo();

        FriendshipDbRepo friendshipDbRepo = null;
        try {
            friendshipDbRepo = new FriendshipDbRepo(url, user, pass, friendshipsTableName);
        } catch (FriendshipRepoValidator e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        MessageRepo messageRepo=new MessageRepo();
        MessageDbRepo messageDbRepo = null;
        try {
            messageDbRepo = new MessageDbRepo(url, user, pass, messagesTableName);
        } catch (MessageRepoValidator e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        GroupDbRepo grupDbRepo=null;
        try{
            grupDbRepo=new GroupDbRepo(url,user,pass,groupTableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetworkService networkService = new NetworkService(userValidator, friendshipValidator, userDbRepo, friendshipDbRepo, messageValidator, messageDbRepo,  grupDbRepo);
        networkService.getUsers().forEach(System.out::println);
        //networkService.getFriendships().forEach(System.out::println);

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(networkService, stage);
    }

    public static void main(String[] args) {
        launch();
    }

    /*
    <?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.CheckBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<VBox alignment="CENTER" prefHeight="139.0" prefWidth="152.0" spacing="20.0" xmlns:fx="http://javafx.com/fxml/1" xmlns="http://javafx.com/javafx/17" fx:controller="com.example.curs9.HelloController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0" />
    </padding>

    <Label fx:id="welcomeText" />
   <CheckBox fx:id="checkBoxActive" mnemonicParsing="false" onAction="#onActionActive" text="CheckBoxActive" />
    <Button onAction="#onHelloButtonClick" text="Hello!" />
</VBox>


     */
}