package com.riguz.jasypt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jasypt.util.text.AES256TextEncryptor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindowController {
    @FXML
    private TextField passwordText;

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private CheckBox onlyEncCheckbox;

    @FXML
    private void onRandomPassword(ActionEvent event) {
        event.consume();
        String password = generatePassayPassword();
        passwordText.setText(password);
    }

    @FXML
    private void onEncrypt(ActionEvent event) {
        event.consume();
        String password = passwordText.getText();
        if (password == null || password.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password not set");
            alert.setHeaderText("You need to set a password");
            alert.setContentText("It's recommended to generate a random password");
            alert.showAndWait();
        } else {
            String text = inputText.getText();
            if (text == null || text.isEmpty()) {
                return;
            }
            try {
                String result = encrypt(text, password, onlyEncCheckbox.isSelected());
                onSuccess(result);
            } catch (Exception ex) {
                onFailed(ex.getMessage());
            }
        }
    }

    @FXML
    private void onDecrypt(ActionEvent event) {
        event.consume();
        String password = passwordText.getText();
        if (password == null || password.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password not set");
            alert.setHeaderText("You need to set a password");
            alert.setContentText("It's recommended to generate a random password");
            alert.showAndWait();
        } else {
            String text = inputText.getText();
            if (text == null || text.isEmpty()) {
                return;
            }
            try {
                String result = decrypt(text, password, onlyEncCheckbox.isSelected());
                onSuccess(result);
            } catch (Exception ex) {
                onFailed("Decrypt failed");
            }
        }
    }

    private static final Pattern ENC_PATTERN = Pattern.compile("ENC\\((.+)\\)");

    private String encrypt(String plain, String password, boolean onlyMarked) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(password);

        if (!onlyMarked) {
            return textEncryptor.encrypt(plain);
        } else {
            Matcher matcher = ENC_PATTERN.matcher(plain);
            return matcher.replaceAll(matchResult -> "ENC(" + textEncryptor.encrypt(matchResult.group(1)) + ")");
        }
    }

    private String decrypt(String encrypted, String password, boolean onlyMarked) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(password);

        if (!onlyMarked) {
            return textEncryptor.decrypt(encrypted);
        } else {
            Matcher matcher = ENC_PATTERN.matcher(encrypted);
            return matcher.replaceAll(matchResult -> "ENC(" + textEncryptor.decrypt(matchResult.group(1)) + ")");
        }
    }

    private void onSuccess(String text) {
        outputText.setText(text);
        inputText.setStyle("-fx-background-color: green;");
    }

    private void onFailed(String message) {
        inputText.setStyle("-fx-background-color: red;");
        outputText.setText(message);
    }

    @FXML
    private void onExchange(ActionEvent event) {
        String output = outputText.getText();
        inputText.setText(output);
        onSuccess("");
    }

    private String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "INVALID";
            }

            public String getCharacters() {
                return "~!@#$%^&*-_";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(15, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }
}
