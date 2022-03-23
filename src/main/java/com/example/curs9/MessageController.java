package com.example.curs9;

import com.example.curs9.ubbcluj.map.domain.MyModels.Group;
import com.example.curs9.ubbcluj.map.domain.MyModels.Message;
import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyUtils.MyObserver.Observer;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.service.NetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageController implements Observer{
    NetworkService networkService;
    ObservableList<Group> modelGroupsTable = FXCollections.observableArrayList();
    ObservableList<Message> chatMessages = FXCollections.observableArrayList();
    Group currentGroup;

    Stage currentStage;

    User currentUser;

    @FXML
    TableView<Group> tableGroups;

    @FXML
    TableColumn<Group, String> tableColumnNume;

    @FXML
    private ListView<Message> lvChatWindow;

    @FXML
    private TextField tfUser1;

    @FXML
    private TextField tfUser2;

    @FXML
    Text fullUser;

    @FXML
    ImageView logoutIcon;

    @FXML
    ImageView refreshIcon;

    private void initNrMessages() throws MessageRepoValidator {
        networkService.setCurrentNrMessages();
    }

    public void setService(NetworkService networkService1, Stage currentStage1) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        networkService = networkService1;
        //networkService.addObserver(this);
        currentUser = networkService.getCurrentUser();
        currentStage = currentStage1;
        initFriends();
        initNrMessages();

        greetings();
    }

    private void greetings() {

        fullUser.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    }

    private void initMessages() throws MessageRepoValidator, UserRepoValidator {
        chatMessages.setAll(getConversation(currentUser));
    }

    private void initFriends() throws UserRepoValidator, FriendshipRepoValidator {
        modelGroupsTable.setAll(getGroupsOfList());
    }

    private List<Group> getGroupsOfList() throws UserRepoValidator, FriendshipRepoValidator {
        Iterable<Group> groups = networkService.getAllGroupsFromCurrentUser();
        List<Group> groupsList = StreamSupport.stream(groups.spliterator(), false)
                .collect(Collectors.toList());
        return groupsList;
    }

    private List<Message> getConversation(User currentUser) throws MessageRepoValidator, UserRepoValidator {
        Group selectedGroup = tableGroups.getSelectionModel().getSelectedItem();
        Iterable<Message> messages=networkService.getConversation(selectedGroup.getId());
        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());

        return messageList;
    }

    @FXML
    public void handleShowConversation() {

        lvChatWindow.setItems(chatMessages);


    }

    @FXML
    public void initialize() {
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Group, String>("nume"));
        tableGroups.setItems(modelGroupsTable);

        tableGroups.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                initMessages();

                lvChatWindow.setItems(chatMessages);
                lvChatWindow.setCellFactory(param -> {
                    ListCell<Message> cell = new ListCell<Message>(){
                        Label lblUserLeft = new Label();
                        Label lblTextLeft = new Label();
                        HBox hBoxLeft = new HBox(lblUserLeft, lblTextLeft);

                        Label lblUserRight = new Label();
                        Label lblTextRight = new Label();
                        HBox hBoxRight = new HBox(lblTextRight, lblUserRight);

                        {
                            hBoxLeft.setAlignment(Pos.CENTER_LEFT);
                            hBoxLeft.setSpacing(5);
                            hBoxRight.setAlignment(Pos.CENTER_RIGHT);
                            hBoxRight.setSpacing(5);
                        }
                        @Override
                        protected void updateItem(Message item, boolean empty) {
                            super.updateItem(item, empty);

                            if(empty)
                            {
                                setText(null);
                                setGraphic(null);
                            }
                            else{
                                //System.out.println(item.getUser());
                                if(item.getId_To().equals(currentUser.getId()) || !item.getId_From().equals(currentUser.getId()))
                                {
                                    lblUserLeft.setText(item.getUser_from().getFirstName()+":");
                                    lblTextLeft.setText(item.getMesaj());
                                    setGraphic(hBoxLeft);
                                }
                                else{
                                    lblUserRight.setText("");
                                    lblTextRight.setText(item.getMesaj());
                                    setGraphic(hBoxRight);
                                }
                            }
                        }

                    };

                    return cell;
                });


            } catch (MessageRepoValidator e) {
                e.printStackTrace();
            } catch (UserRepoValidator e) {
                e.printStackTrace();
            }
        });



    }

    @FXML
    private void handleUser1SubmitMessage(ActionEvent event) throws Exception {
        networkService.increaseNrMessages();
        Group selectedGroup = tableGroups.getSelectionModel().getSelectedItem();
        String id_to="";
        for(var x:selectedGroup.getId()){
            if(!x.equals(currentUser.getId()))
                if(id_to=="")
                    id_to=x;
                else
                    id_to=id_to+" "+x;
        }
        networkService.addNewMessage(String.valueOf(networkService.getNrMessages()),networkService.getCurrentUser().getId(),id_to,tfUser1.getText());
        tfUser1.setText("");
    }

    @FXML
    public void handleRefresh(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        currentStage.getScene().setCursor(Cursor.WAIT);

        initMessages();

        currentStage.getScene().setCursor(Cursor.DEFAULT);
        //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Refresh", "Tabele actualizate cu succes!");
    }

    @FXML
    public void handleRefreshGroups(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        currentStage.getScene().setCursor(Cursor.WAIT);
        initFriends();
        currentStage.getScene().setCursor(Cursor.DEFAULT);
        //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Refresh", "Tabele actualizate cu succes!");
    }

    @Override
    public void update() {
        try {
            initMessages();
        } catch (MessageRepoValidator e) {
            e.printStackTrace();
        } catch (UserRepoValidator e) {
            e.printStackTrace();
        }
            try {
                initFriends();
            } catch (UserRepoValidator e) {
                e.printStackTrace();
            } catch (FriendshipRepoValidator e) {
                e.printStackTrace();
            }
    }


    @FXML
    public void handleHome(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("home-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("app-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.show();

        currentStage.close();

        HomeController homeController = fxmlLoader.getController();
        homeController.setService(networkService, stage);
    }

    @FXML
    public void handleCreateConv(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CreateMessages-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("app-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.show();

        //currentStage.close();

        CreateMessageController createMessageController = fxmlLoader.getController();
        createMessageController.setService(networkService, stage);
    }

    @FXML
    public void handeFriends( ) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("friend-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("app-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        currentStage.close();

        FriendsController friendsController = fxmlLoader.getController();
        friendsController.setService(networkService, stage);
    }

    @FXML
    public void handleLogOut() throws InterruptedException, IOException {
        Stage stage = new Stage();

        /*FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("login-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("app-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 530, 350);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spicee");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();*/

        FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 350);
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        currentStage.close();

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(networkService, stage);
    }

    @FXML
    public void handleReports(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("report-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(StarterApplication.class.getResource("app-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Spicee");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        currentStage.close();

        AppController appController = fxmlLoader.getController();
        appController.setService(networkService, stage);
    }

    @FXML
    public void handleLogoAnimationRefresh() {
        Glow glow = new Glow();
        glow.setLevel(0.77);
        refreshIcon.setEffect(glow);
    }

    @FXML
    public void handleLogoAnimationRefreshOff() {
        Glow glow = new Glow();
        glow.setLevel(0);
        refreshIcon.setEffect(glow);
    }

    @FXML
    public void handleLogoAnimationLogout() {
        Glow glow = new Glow();
        glow.setLevel(0.77);
        logoutIcon.setEffect(glow);
    }

    @FXML
    public void handleLogoAnimationLogoutOff() {
        Glow glow = new Glow();
        glow.setLevel(0);
        logoutIcon.setEffect(glow);
    }

    @FXML
    public void handleRefreshAllTables() throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        currentStage.getScene().setCursor(Cursor.WAIT);

        initFriends();
        initNrMessages();

        currentStage.getScene().setCursor(Cursor.DEFAULT);
        //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Refresh", "Tabele actualizate cu succes!");
    }

    @FXML
    public void handleExit(ActionEvent actionEvent){
        currentStage.close();
    }
}
