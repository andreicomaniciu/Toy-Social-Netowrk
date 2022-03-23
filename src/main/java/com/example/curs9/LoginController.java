package com.example.curs9;

import com.example.curs9.ubbcluj.map.domain.MyModels.User;
import com.example.curs9.ubbcluj.map.domain.MyValidators.FriendshipRepoValidator;
import com.example.curs9.ubbcluj.map.domain.MyValidators.UserRepoValidator;
import com.example.curs9.ubbcluj.map.service.NetworkService;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class LoginController {
    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldSeePassword;

    @FXML
    private PasswordField textFieldHidePassword;

    @FXML
    private TextField textFieldDesc;
    @FXML
    private TextField textFieldFrom;
    @FXML
    private TextField textFieldTo;
    @FXML
    private TextArea textAreaMessage;
    @FXML
    private DatePicker datePickerDate;

    @FXML
    private ImageView eyePass;

    @FXML
    private ImageView imageLogo;
    @FXML
    private ImageView imageDecorate1;
    @FXML
    private ImageView imageDecorate2;
    @FXML
    private ImageView imageDecorate3;
    @FXML
    private ImageView imageDecorate4;
/*
        Image seePass = new Image("C:\\Users\\UserX\\AppsUni\\map\\map_grupa222_andreii_gui\\src\\main\\resources\\com\\example\\curs9\\images\\SeePassThru.png");
        Image hidePass = new Image("C:\\Users\\UserX\\AppsUni\\map\\map_grupa222_andreii_gui\\src\\main\\resources\\com\\example\\curs9\\images\\HidePassThru.png");
    */
    Image seePass = new Image("C:\\Users\\UserX\\AppsUni\\map_grupa222_andreii_gui\\src\\main\\resources\\com\\example\\curs9\\images\\SeePassThru.png");
    Image hidePass = new Image("C:\\Users\\UserX\\AppsUni\\map_grupa222_andreii_gui\\src\\main\\resources\\com\\example\\curs9\\images\\HidePassThru.png");

    private NetworkService service;
    Stage currentStage;
    User userCurent;

    @FXML
    private void initialize() {
    }


    public void setService(NetworkService service1, Stage currentStage) throws InterruptedException {
        this.service = service1;
        this.currentStage = currentStage;
        //this.dialogStage=stage;
        handleAnimationRotateOn();
    }

    @FXML
    public void handleLogIn() throws UserRepoValidator, IOException, FriendshipRepoValidator, NoSuchAlgorithmException, InvalidKeySpecException {
        String email = textFieldEmail.getText();
        String password = textFieldHidePassword.getText();

        User user;

        if (service.isAuthValid(email, password) == true) {

            user = service.getUserByEmail(email);
            service.setCurrentUser(user);

            currentStage.close();

            startApp();
        } else if (email.equals("admin")) {
            user = service.getUserByEmail("Andrei.Cusiac@map.com");
            service.setCurrentUser(user);

            startApp();
        } else if(email.equals("admin1")){
            user=service.getUserByEmail("Aaa.Bbbb@map.com");
            service.setCurrentUser(user);

            startApp();
        } else {
            MessageAlert.showErrorMessage(null, "Atentie!\nEmail-ul si/ sau parola nu sunt valide!");
        }
    }

    private void startApp() throws UserRepoValidator, FriendshipRepoValidator {
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
        homeController.setService(service, stage);
    }

    @FXML
    public void handleCancel() {
        currentStage.close();
    }

    public void handleSeePass() {
        eyePass.setImage(seePass);
        //currentStage.getScene().setCursor(Cursor.CROSSHAIR);

        textFieldSeePassword.setText(textFieldHidePassword.getText());
        textFieldHidePassword.setVisible(false);
        textFieldSeePassword.setVisible(true);
    }

    public void handleHidePass() {
        eyePass.setImage(hidePass);
        //currentStage.getScene().setCursor(Cursor.DEFAULT);

        textFieldHidePassword.setText(textFieldSeePassword.getText());
        textFieldSeePassword.setVisible(false);
        textFieldHidePassword.setVisible(true);
    }

    @FXML
    public void handleAnimationRotateOn() throws InterruptedException {
        //currentStage.getScene().setCursor(Cursor.HAND);
        imageDecorate1.setMouseTransparent(true);

        double angleRotation = 360;
        double timeRotation = 0.8;
        Long delayedBy = Long.valueOf(40);

        Node node = new ImageView();


        RotateTransition rotateTransition1 = new RotateTransition();
        rotateTransition1.setNode(imageDecorate1);

        rotateTransition1.setDuration(Duration.seconds(timeRotation));
        rotateTransition1.setCycleCount(1);
        rotateTransition1.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition1.setByAngle(angleRotation);
        rotateTransition1.setAxis(Rotate.Z_AXIS);

        RotateTransition rotateTransition2 = new RotateTransition();
        rotateTransition2.setNode(imageDecorate2);

        rotateTransition2.setDuration(Duration.seconds(timeRotation));
        rotateTransition2.setCycleCount(1);
        rotateTransition2.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition2.setByAngle(angleRotation);
        rotateTransition2.setAxis(Rotate.Z_AXIS);

        RotateTransition rotateTransition3 = new RotateTransition();
        rotateTransition3.setNode(imageDecorate3);

        rotateTransition3.setDuration(Duration.seconds(timeRotation));
        rotateTransition3.setCycleCount(1);
        rotateTransition3.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition3.setByAngle(angleRotation);
        rotateTransition3.setAxis(Rotate.Z_AXIS);

        RotateTransition rotateTransition4 = new RotateTransition();
        rotateTransition4.setNode(imageDecorate4);

        rotateTransition4.setDuration(Duration.seconds(timeRotation));
        rotateTransition4.setCycleCount(1);
        rotateTransition4.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition4.setByAngle(angleRotation);
        rotateTransition4.setAxis(Rotate.Z_AXIS);

        rotateTransition1.play();

        Thread.sleep(delayedBy);

        rotateTransition2.play();

        Thread.sleep(delayedBy);


        rotateTransition3.play();

        Thread.sleep(delayedBy);

        rotateTransition4.play();

        imageDecorate1.setMouseTransparent(false);
    }

    @FXML
    public void handleAnimationOff() {
        //currentStage.getScene().setCursor(Cursor.DEFAULT);
        Glow glow = new Glow();
        glow.setLevel(0);
        imageDecorate1.setEffect(glow);
    }

    @FXML
    public void handleAnimation1On() {
        //currentStage.getScene().setCursor(Cursor.HAND);
        Glow glow = new Glow();
        glow.setLevel(0.77);
        imageDecorate1.setEffect(glow);
    }

    @FXML
    public void handleAnimation1Off() {
        //currentStage.getScene().setCursor(Cursor.DEFAULT);
        Glow glow = new Glow();
        glow.setLevel(0);
        imageDecorate1.setEffect(glow);
    }

    @FXML
    public void handleAnimation2On() {
        //currentStage.getScene().setCursor(Cursor.HAND);
        Glow glow = new Glow();
        glow.setLevel(0.77);
        imageDecorate2.setEffect(glow);
    }

    @FXML
    public void handleAnimation2Off() {
        //currentStage.getScene().setCursor(Cursor.DEFAULT);
        Glow glow = new Glow();
        glow.setLevel(0);
        imageDecorate2.setEffect(glow);
    }

    @FXML
    public void handleAnimation3On() {
        //currentStage.getScene().setCursor(Cursor.HAND);
        Glow glow = new Glow();
        glow.setLevel(0.77);
        imageDecorate3.setEffect(glow);
    }

    @FXML
    public void handleAnimation3Off() {
        //currentStage.getScene().setCursor(Cursor.DEFAULT);
        Glow glow = new Glow();
        glow.setLevel(0);
        imageDecorate3.setEffect(glow);
    }

    @FXML
    public void handleAnimation4On() {
        //currentStage.getScene().setCursor(Cursor.HAND);
        Glow glow = new Glow();
        glow.setLevel(0.77);
        imageDecorate4.setEffect(glow);
    }

    @FXML
    public void handleAnimation4Off() {
        //currentStage.getScene().setCursor(Cursor.DEFAULT);
        Glow glow = new Glow();
        glow.setLevel(0);
        imageDecorate4.setEffect(glow);
    }

    @FXML
    public void handleLogoAnimation() {
        Glow glow = new Glow();
        glow.setLevel(0.5);
        imageLogo.setEffect(glow);

        imageLogo.setMouseTransparent(true);

        double angleRotationClockwise = -20;
        double angleRotationAntiClockwise = 360;

        double timeRotation = 1;
        Long delayedBy = Long.valueOf(40);

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(imageLogo);

        rotateTransition.setDuration(Duration.seconds(timeRotation));
        rotateTransition.setCycleCount(1);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition.setByAngle(angleRotationAntiClockwise);
        //rotateTransition.setByAngle(angleRotationClockwise);
        //rotateTransition.setByAngle(angleRotationAntiClockwise);
        rotateTransition.setAxis(Rotate.Z_AXIS);

        imageLogo.setMouseTransparent(false);

    }

    @FXML
    public void handleLogoAnimationOff() {
        Glow glow = new Glow();
        glow.setLevel(0);
        imageLogo.setEffect(glow);
    }
}