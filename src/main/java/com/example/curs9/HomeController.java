package com.example.curs9;

import com.example.curs9.*;
import com.example.curs9.LoginController;
import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
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
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeController implements Observer {
    NetworkService networkService;
    ObservableList<User> modelUsersTable = FXCollections.observableArrayList();
    ObservableList<User> modelFriendsTable = FXCollections.observableArrayList();
    ObservableList<Friendship> modelFrom = FXCollections.observableArrayList();
    ObservableList<Friendship> modelTo = FXCollections.observableArrayList();

    Stage currentStage;

    User currentUser;
    String welcomeGreeting;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldFriend;

    @FXML
    Text textText;

    @FXML
    Text fullUser;

    @FXML
    Button unfriendButton;

    @FXML
    Button acceptButton;

    @FXML
    Button rejectButton;

    @FXML
    private Label labelUhOh;

    @FXML
    private Label labelLookWho;

    @FXML
    private CheckBox checkBoxActive;

    @FXML
    TableView<User> tableView;

    @FXML
    TableView<User> tableFriends;

    @FXML
    TableView<Friendship> tableFriendRequestsFrom;

    @FXML
    TableView<Friendship> tableFriendRequestsTo;

    @FXML
    TableColumn<User, String> tableColumnNumeFriend;

    @FXML
    TableColumn<User, String> tableColumnPrenumeFriend;

    @FXML
    TableColumn<User, String> tableColumnEmailFriend;

    @FXML
    TableColumn<User, String> tableColumnNume;

    @FXML
    TableColumn<User, String> tableColumnPrenume;

    @FXML
    TableColumn<User, String> tableColumnEmail;

    @FXML
    TableColumn<Friendship, String> tableColumnFrom;

    @FXML
    TableColumn<Friendship, LocalDateTime> tableColumnData;

    @FXML
    TableColumn<Friendship, String> tableColumnStatus;

    @FXML
    TableColumn<Friendship, String> tableColumnTo;

    @FXML
    TableColumn<Friendship, LocalDateTime> tableColumnData1;

    @FXML
    TableColumn<Friendship, String> tableColumnStatus1;

    @FXML
    ImageView searchIcon;

    @FXML
    ImageView refreshIcon;

    @FXML
    ImageView logoutIcon;

    public void setService(NetworkService networkService1, Stage currentStage1) throws UserRepoValidator, FriendshipRepoValidator {
        networkService = networkService1;
        networkService.addObserver(this);
        currentUser = networkService.getCurrentUser();
        currentStage = currentStage1;

        labelLookWho.setVisible(false);
        labelUhOh.setVisible(false);

        initFriends();
        initModel1();

        greetings();
    }

    private void greetings() {

        welcomeGreeting = "Great to see you, " + currentUser.getFirstName() + "!";
        textText.setText(welcomeGreeting);

        fullUser.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    }

    private Comparator<User> sortNames () {
        Comparator<User> compareLastName = Comparator.comparing(User::getLastName);
        Comparator<User> compareFirstName = Comparator.comparing(User::getFirstName);
        Comparator<User> compareAll = compareLastName.thenComparing(compareFirstName);

        return compareAll;
    }

    private Comparator<Friendship> sortDatesReversedForFriendships () {
        Comparator<Friendship> compareDate = Comparator.comparing(Friendship::getDateOfFriendshipRequest).reversed();
        Comparator<Friendship> compareAll = compareDate;

        return compareAll;
    }

    private void initModel1() throws FriendshipRepoValidator, UserRepoValidator {
        Iterable<Friendship> messages = networkService.transformListReceivedReuqests(currentUser.getId());
        List<Friendship> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .sorted(sortDatesReversedForFriendships())
                .collect(Collectors.toList());
        modelFrom.setAll(messageTaskList);

        if (messageTaskList.isEmpty()) {
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            labelLookWho.setVisible(true);
        }
        else {
            acceptButton.setVisible(true);
            rejectButton.setVisible(true);
            labelLookWho.setVisible(false);
        }
    }

    private void initModel2() throws FriendshipRepoValidator, UserRepoValidator {
        Iterable<Friendship> messages = networkService.transformListSentReuqests(currentUser.getId());
        List<Friendship> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .sorted(sortDatesReversedForFriendships())
                .collect(Collectors.toList());
        modelTo.setAll(messageTaskList);
    }

    private void initModel() throws UserRepoValidator {
        modelUsersTable.setAll(getUsersList());
    }

    private void initFriends() throws UserRepoValidator, FriendshipRepoValidator {
        var x = getFriendsOfList(currentUser);

        modelFriendsTable.setAll(x);

        if  (x.size() == 0) {
            unfriendButton.setVisible(false);
            textFieldFriend.setVisible(false);
            searchIcon.setVisible(false);
            labelUhOh.setVisible(true);
        }
        else {
            unfriendButton.setVisible(true);
            textFieldFriend.setVisible(true);
            searchIcon.setVisible(true);
            labelUhOh.setVisible(false);
        }
    }

    private List<User> getUsersList() throws UserRepoValidator {
        Iterable<User> users = networkService.getUsers();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .sorted(sortNames())
                .collect(Collectors.toList());
        return usersList;
    }

    private List<User> getFriendsOfList(User currentUser1) throws UserRepoValidator, FriendshipRepoValidator {
        Iterable<User> users = networkService.getFriendsOf(currentUser1);
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .sorted(sortNames())
                .collect(Collectors.toList());
        return usersList;
    }

    @FXML
    public void initialize() {
        tableColumnNumeFriend.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnPrenumeFriend.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnEmailFriend.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        tableFriends.setItems(modelFriendsTable);

        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<Friendship, String>("numeid1"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<Friendship, LocalDateTime>("dateOfFriendshipRequest"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Friendship, String>("status"));
        tableFriendRequestsFrom.setItems(modelFrom);

        textFieldFriend.textProperty().addListener(o -> {
            try {
                handleFilterFriends(modelFriendsTable, textFieldFriend);
            } catch (UserRepoValidator e) {
                e.printStackTrace();
            } catch (FriendshipRepoValidator e) {
                e.printStackTrace();
            }
        });
    }

    private void handleFilter(ObservableList<User> modelCurent, TextField textField) throws UserRepoValidator {
        TextField currentTextField = textField;

        Predicate<User> firstNameFilter = n -> n.getFirstName().contains(currentTextField.getText());
//        Predicate<User> firstNameFilter = n -> n.getFirstName().contains(textFieldName.getCharacters());
        Predicate<User> lastNameFilter = n -> n.getLastName().contains(currentTextField.getText());
//        Predicate<User> lastNameFilter = n -> n.getLastName().contains(textFieldName.getCharacters());
        Predicate<User> firstNameSmallFilter = n -> n.getFirstName().toLowerCase().contains(currentTextField.getText());
        Predicate<User> firstNameCAPSFilter = n -> n.getFirstName().toUpperCase().contains(currentTextField.getText());
        Predicate<User> lastNameSmallFilter = n -> n.getLastName().toLowerCase().contains(currentTextField.getText());
        Predicate<User> lastNameCAPSFilter = n -> n.getLastName().toUpperCase().contains(currentTextField.getText());

        Predicate<User> allPredicates = firstNameFilter.
                or(lastNameFilter).
                or(firstNameCAPSFilter).
                or(firstNameSmallFilter).
                or(lastNameSmallFilter).
                or(lastNameCAPSFilter);

        modelCurent.setAll(getUsersList()
                .stream()
                .filter(allPredicates)
                .collect(Collectors.toList()));
    }

    private void handleFilterFriends(ObservableList<User> modelCurent, TextField textField) throws UserRepoValidator, FriendshipRepoValidator {
        TextField currentTextField = textField;

        Predicate<User> firstNameFilter = n -> n.getFirstName().contains(currentTextField.getText());
//        Predicate<User> firstNameFilter = n -> n.getFirstName().contains(textFieldName.getCharacters());
        Predicate<User> lastNameFilter = n -> n.getLastName().contains(currentTextField.getText());
//        Predicate<User> lastNameFilter = n -> n.getLastName().contains(textFieldName.getCharacters());
        Predicate<User> firstNameSmallFilter = n -> n.getFirstName().toLowerCase().contains(currentTextField.getText());
        Predicate<User> firstNameCAPSFilter = n -> n.getFirstName().toUpperCase().contains(currentTextField.getText());
        Predicate<User> lastNameSmallFilter = n -> n.getLastName().toLowerCase().contains(currentTextField.getText());
        Predicate<User> lastNameCAPSFilter = n -> n.getLastName().toUpperCase().contains(currentTextField.getText());

        Predicate<User> allPredicates = firstNameFilter.
                or(lastNameFilter).
                or(firstNameCAPSFilter).
                or(firstNameSmallFilter).
                or(lastNameSmallFilter).
                or(lastNameCAPSFilter);

        modelCurent.setAll(getFriendsOfList(currentUser)
                .stream()
                .filter(allPredicates)
                .collect(Collectors.toList()));
    }

    @FXML
    public void handleAddNewFriend(ActionEvent ev) {
        // TODO
        User selectedUser = tableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.makeRequest(networkService.getCurrentUser().getId(), selectedUser.getId());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend", selectedUser.getFirstName() + " " + selectedUser.getLastName() + " has just received a friend request from you!");
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
            }

            currentStage.getScene().setCursor(Cursor.DEFAULT);
        }
        else {
            MessageAlert.showErrorMessage(null, "There is no selected user!");
        }
    }

    @FXML
    public void handleRemoveFriend(ActionEvent ev) {
        // TODO
        User selectedUser = tableFriends.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.deleteFriendship(currentUser.getId(), selectedUser.getId());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Remove friend", "Your friendship with " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + " has just been deleted!");
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
            }
            currentStage.getScene().setCursor(Cursor.DEFAULT);

        }
        else {
            MessageAlert.showErrorMessage(null, "There is no selected user!");
        }
    }

    @FXML
    public void handleAddNewFriendship(ActionEvent actionEvent) throws UserRepoValidator {
        Friendship selectedFriendship = tableFriendRequestsFrom.getSelectionModel().getSelectedItem();

        if (selectedFriendship != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.answerRequest(currentUser.getId(), selectedFriendship.getId1(), "approved");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend", "Your friendships with " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " has just been confirmed!");
            } catch (Exception e) {
                //MessageAlert.showErrorMessage(null, e.getMessage());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend", "Your friendships with " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " has just been confirmed!");
                currentStage.getScene().setCursor(Cursor.DEFAULT);
            } finally {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend", "Your friendships with " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " has just been confirmed!");
                currentStage.getScene().setCursor(Cursor.DEFAULT);
            }
            currentStage.getScene().setCursor(Cursor.DEFAULT);

        }
        else {
            MessageAlert.showErrorMessage(null, "There is no selected friendship request!");
        }
    }

    @FXML
    public void handleRejectNewFriendship(ActionEvent actionEvent) throws UserRepoValidator {
        Friendship selectedFriendship = tableFriendRequestsFrom.getSelectionModel().getSelectedItem();

        if (selectedFriendship != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.answerRequest(currentUser.getId(), selectedFriendship.getId1(), "rejected");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend",  networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + "'s friend request has just been removed!");
            } catch (Exception e) {
//                MessageAlert.showErrorMessage(null, e.getMessage());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend",  networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + "'s friend request has just been removed!");
                currentStage.getScene().setCursor(Cursor.DEFAULT);
            } finally {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Add friend",  networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + "'s friend request has just been removed!");
                currentStage.getScene().setCursor(Cursor.DEFAULT);
            }
            currentStage.getScene().setCursor(Cursor.DEFAULT);

        }
        else {
            MessageAlert.showErrorMessage(null, "There is no selected friendship request!");
        }
    }

    @FXML
    public void handleRejectSentFriendship(ActionEvent actionEvent) {
        Friendship selectedFriendship = tableFriendRequestsTo.getSelectionModel().getSelectedItem();

        if (selectedFriendship != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.answerRequest(selectedFriendship.getId2(), currentUser.getId(), "rejected");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Adaugare prieten", "Cererea de prietenie catre " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " a fost stearsa!");
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
            }
            currentStage.getScene().setCursor(Cursor.DEFAULT);

        }
        else {
            MessageAlert.showErrorMessage(null, "Nu exista o prietenie selectata!");
        }
    }

    @FXML
    public void handleRefreshAllTables() throws UserRepoValidator, FriendshipRepoValidator {
        currentStage.getScene().setCursor(Cursor.WAIT);

        initFriends();
        initModel1();
        try {
            initModel1();
        } catch (FriendshipRepoValidator e) {
            e.printStackTrace();
        } catch (UserRepoValidator e) {
            e.printStackTrace();
        }

        currentStage.getScene().setCursor(Cursor.DEFAULT);
        //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Refresh", "Tabele actualizate cu succes!");
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
    public void handleMessages(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("message-view.fxml"));

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

        MessageController messageController = fxmlLoader.getController();
        messageController.setService(networkService, stage);
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
    public void handleExit(ActionEvent actionEvent){
        currentStage.close();
    }

    @Override
    public void update() {
        try {
            initModel();
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
        try {
            initModel1();
        } catch (FriendshipRepoValidator e) {
            e.printStackTrace();
        } catch (UserRepoValidator e) {
            e.printStackTrace();
        }
        try {
            initModel2();
        } catch (FriendshipRepoValidator e) {
            e.printStackTrace();
        } catch (UserRepoValidator e) {
            e.printStackTrace();
        }
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
}