package com.example.curs9;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyUtils.MyObserver.Observer;
import com.example.curs9.ubbcluj.map.domain.MyUtils.PdfCreator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.service.NetworkService;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppController implements Observer {
    NetworkService networkService;
    ObservableList<User> modelUsersTable = FXCollections.observableArrayList();
    ObservableList<User> modelFriendsTable = FXCollections.observableArrayList();
    ObservableList<Friendship> model1 = FXCollections.observableArrayList();
    ObservableList<Friendship> model2 = FXCollections.observableArrayList();

    Stage currentStage;

    User currentUser;
    String welcomeGreeting;


    @FXML DatePicker datePickerinceput;

    @FXML DatePicker datePickerfinal;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldFriend;

    @FXML
    Text textText;

    @FXML
    private Label welcomeText;

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
    Text fullUser;

    @FXML
    ImageView logoutIcon;

    @FXML
    ImageView refreshIcon;

    public void setService(NetworkService networkService1, Stage currentStage1) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        networkService = networkService1;
        networkService.addObserver(this);
        currentUser = networkService.getCurrentUser();
        currentStage = currentStage1;
        //initModel();
        initFriends();
        //initModel1();
        //initModel2();



        greetings();
    }

    private void initNrGroups() throws MessageRepoValidator {
        networkService.setCurrentNrGroups();
    }

    private void greetings() {

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
        model1.setAll(messageTaskList);
    }

    private void initModel2() throws FriendshipRepoValidator, UserRepoValidator {
        Iterable<Friendship> messages = networkService.transformListSentReuqests(currentUser.getId());
        List<Friendship> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .sorted(sortDatesReversedForFriendships())
                .collect(Collectors.toList());
        model2.setAll(messageTaskList);
    }

    private void initModel() throws UserRepoValidator {
        modelUsersTable.setAll(getUsersList());
    }

    private void initFriends() throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        modelFriendsTable.setAll(getFriendsOfList(currentUser));
    }

    private List<User> getUsersList() throws UserRepoValidator {
        Iterable<User> users = networkService.getUsers();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .sorted(sortNames())
                .collect(Collectors.toList());
        return usersList;
    }

    private List<User> getFriendsOfList(User currentUser1) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        initNrGroups();
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

        textFieldFriend.textProperty().addListener(o -> {
            try {
                handleFilterFriends(modelFriendsTable, textFieldFriend);
            } catch (UserRepoValidator e) {
                e.printStackTrace();
            } catch (FriendshipRepoValidator e) {
                e.printStackTrace();
            } catch (MessageRepoValidator e) {
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

    private void handleFilterFriends(ObservableList<User> modelCurent, TextField textField) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
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
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void handleAddNewFriend(ActionEvent ev) {
        // TODO
        User selectedUser = tableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.makeRequest(networkService.getCurrentUser().getId(), selectedUser.getId());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Adaugare prieten", selectedUser.getFirstName() + " " + selectedUser.getLastName() + " tocmai a primit o cerere de prietenie din partea ta!");
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
            }

            currentStage.getScene().setCursor(Cursor.DEFAULT);
        }
        else {
            MessageAlert.showErrorMessage(null, "Nu exista un user selectat!");
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
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Stergere prieten", "Prietenia cu " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + " tocmai a fost eliminata!");
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } finally {
            }
            currentStage.getScene().setCursor(Cursor.DEFAULT);

        }
        else {
            MessageAlert.showErrorMessage(null, "Nu exista un user selectat!");
        }
    }

    public void onActionActive(ActionEvent actionEvent) {
        if (checkBoxActive.isSelected()) {
            welcomeText.setText("Ego este activ!");
        }
        else
        {
            welcomeText.setText("Popescu nu este activ!");
        }
    }

    @FXML
    public void handleAddNewFriendship(ActionEvent actionEvent) {
        Friendship selectedFriendship = tableFriendRequestsFrom.getSelectionModel().getSelectedItem();

        if (selectedFriendship != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.answerRequest(currentUser.getId(), selectedFriendship.getId1(), "approved");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Adaugare prieten", "Prietenia cu " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " tocmai a fost adaugata!");
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
    public void handleRejectNewFriendship(ActionEvent actionEvent) {
        Friendship selectedFriendship = tableFriendRequestsFrom.getSelectionModel().getSelectedItem();

        if (selectedFriendship != null) {
            currentStage.getScene().setCursor(Cursor.WAIT);

            try {
                networkService.answerRequest(currentUser.getId(), selectedFriendship.getId1(), "rejected");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Adaugare prieten", "Cererea de prietenie de la " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName() + " " + networkService.getUserByID(selectedFriendship.getId2()).getFirstName()  + " a fost stearsa!");
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
    public void handleRefreshAllTables(ActionEvent actionEvent) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        currentStage.getScene().setCursor(Cursor.WAIT);

        initModel();
        initFriends();
        try {
            initModel1();
        } catch (FriendshipRepoValidator e) {
            e.printStackTrace();
        } catch (UserRepoValidator e) {
            e.printStackTrace();
        }
        initModel2();

        currentStage.getScene().setCursor(Cursor.DEFAULT);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Refresh", "Tabele actualizate cu succes!");
    }

    @FXML
    public void handleStatistics1(ActionEvent actionEvent) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator, IOException {
        PdfCreator pdfCreator=new PdfCreator(networkService);
        pdfCreator.createPDFStats(String.valueOf(datePickerinceput.getValue()),String.valueOf(datePickerfinal.getValue()));
        datePickerfinal.setValue(LocalDate.parse("2022-01-15"));
        datePickerinceput.setValue(LocalDate.parse("2022-01-15"));
    }

    @FXML
    public void handleStatistics2(ActionEvent actionEvent) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator, IOException {
        PdfCreator pdfCreator=new PdfCreator(networkService);
        List<String> user=new ArrayList<>();
        User user1 = tableFriends.getSelectionModel().getSelectedItem();
        user.add(currentUser.getId());
        user.add(user1.getId());
        pdfCreator.createPDFConversation(String.valueOf(datePickerinceput.getValue()),String.valueOf(datePickerfinal.getValue()),user);
        datePickerfinal.setValue(LocalDate.parse("2022-01-15"));
        datePickerinceput.setValue(LocalDate.parse("2022-01-15"));
        //textFielDataFinal.setText("");
        //textFieldDataInceput.setText("");
    }

    @FXML
    public void handleLogOut(ActionEvent actionEvent) throws InterruptedException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
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
        stage.show();

        currentStage.close();

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(networkService, stage);
    }

    @FXML
    public void handeFriends(ActionEvent actionEvent) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator {
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
        stage.show();

        currentStage.close();

        MessageController messageController = fxmlLoader.getController();
        messageController.setService(networkService, stage);
    }

    @FXML
    public void handeHome( ) throws MessageRepoValidator, UserRepoValidator, FriendshipRepoValidator {
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
        stage.setResizable(false);
        stage.show();

        currentStage.close();

        HomeController homeController = fxmlLoader.getController();
        homeController.setService(networkService, stage);
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
        } catch (MessageRepoValidator e) {
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