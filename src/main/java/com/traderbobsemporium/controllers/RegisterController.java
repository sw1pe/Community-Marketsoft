package main.java.com.traderbobsemporium.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.java.com.traderbobsemporium.dao.AccountDAO;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.loggers.ActivityLoggerFactory;
import main.java.com.traderbobsemporium.loggers.ActivityType;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.Util;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.SquigglesBackgroundProducer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class RegisterController implements Initializable {
    private Captcha captcha;
    @FXML
    private ImageView captchaView;
    private TextField usernameField, captchaField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCaptcha();
    }

    @FXML
    private void createCaptcha(){
        captcha =  new Captcha.Builder(200, 50)
                .addText()
                .addBackground(new SquigglesBackgroundProducer())
                .addBorder()
                .addNoise()
                .addNoise()
                .build();
        captchaField.clear();
        captchaView.setImage(SwingFXUtils.toFXImage(captcha.getImage(), null));
    }

    @FXML
    private void tryToRegister(){
        if (captcha.isCorrect(captchaField.getText())) {
            try {
                register();
                GUIManager.getInstance().getGUIByName("RegGUI").getStage().close();
            } catch (SQLException e) {
                Util.displayError(e.getMessage(), Alert.AlertType.ERROR);
                createCaptcha();
            }
        } else{
            Util.displayError("Captcha is incorrect!", Alert.AlertType.WARNING);
            createCaptcha();
        }
    }

    private void register() throws SQLException {
        Account account = new Account(Util.NEW_ID, usernameField.getText(),
                new DefaultPasswordService().encryptPassword(passwordField.getText()),
                "none", AccountRole.UNCONFIRMED);
        new AccountDAO().add(account);
        String[] loggerParams = new String[]{account.getName(), String.valueOf(account.getId())};
        new ActivityLoggerFactory().logger("AccountActivity").log(loggerParams, ActivityType.REGISTER);
        Util.displayError("Your account has been registered! Please wait for your account to be " +
                "assigned a designated role by the administrator.", Alert.AlertType.INFORMATION);
    }
}
