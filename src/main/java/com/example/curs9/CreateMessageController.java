package com.example.curs9;

import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.MessageRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.service.NetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CreateMessageController {
    NetworkService networkService;
    ObservableList<User> modelFriendsTable = FXCollections.observableArrayList();
    Stage currentStage;
    User currentUser;

    ArrayList<String> addedUsers=new ArrayList<>();

    @FXML
    TableView<User> tableFriends;

    @FXML
    TableColumn<User, String> tableColumnNumeFriend;

    @FXML
    TableColumn<User, String> tableColumnPrenumeFriend;

    @FXML
    private TextField tfNume;

    private void initNrGroups() throws MessageRepoValidator {
        networkService.setCurrentNrGroups();
    }

    public void setService(NetworkService networkService1, Stage currentStage1) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        networkService = networkService1;
        //networkService.addObserver(this);
        currentUser = networkService.getCurrentUser();
        currentStage = currentStage1;

        initFriends();
    }

    private void initFriends() throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        modelFriendsTable.setAll(getFriendsOfList(currentUser));
    }

    private List<User> getFriendsOfList(User currentUser1) throws UserRepoValidator, FriendshipRepoValidator, MessageRepoValidator {
        initNrGroups();
        Iterable<User> users = networkService.getFriendsOf(currentUser1);
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        return usersList;
    }

    @FXML
    public void initialize() {
        tableColumnNumeFriend.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnPrenumeFriend.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableFriends.setItems(modelFriendsTable);
    }

    @FXML
    public void handleAddUser(){
        User  selectedUser=tableFriends.getSelectionModel().getSelectedItem();
        addedUsers.add(selectedUser.getId());
    }

    @FXML
    public void handleCreateConversation(){
        networkService.increaseNrGroups();
        String  ids=currentUser.getId();
        for(var x:addedUsers){
                ids=ids+" "+x;
        }

        networkService.AddGroup(String.valueOf(networkService.getNrGroups()),tfNume.getText(),ids);
        currentStage.close();


    }

}
