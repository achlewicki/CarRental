/**@author Chlewicki Adam */
package com.company.Controllers;

import com.company.ClassesForDataBase.DataBaseForClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Kontroler do tworzenia użytkownika */
public class CreateClientController {
    /** Pola do wprowadzania danych + do walidacji*/
    @FXML
    private Label nameError, surnameError, phoneError, mailError, idNumberError, loginError, passwordError, confirmPasswordError;
    @FXML
    private TextField name, surname, phone, mail, idNumber, login;
    @FXML
    private PasswordField password, confirmPassword;

    /** Próba utworzenia Klienta, zakończy się powodzeniem jeśli walidacja przejdzie pomyślnie */
    @FXML
    public void onCreateClientButton(){
        nameError.setText("");
        surnameError.setText("");
        phoneError.setText("");
        mailError.setText("");
        idNumberError.setText("");
        loginError.setText("");
        passwordError.setText("");
        confirmPasswordError.setText("");

        boolean create = true;
        if(name.getText().equals("")) {
            nameError.setText("Nie podano imienia!!!");
            create = false;
        }
        else if(name.getText().length() > 20){
            nameError.setText("Za długie imię!!! (Max 20 znaków)");
            create = false;
        }

        if(surname.getText().equals("")) {
            surnameError.setText("Nie podano nazwiska!!!");
            create = false;
        }
        else if(surname.getText().length() > 30){
            surnameError.setText("Za długie nazwisko!!! (Max 30 znaków)");
        }

        if(phone.getText().equals("")) {
            phoneError.setText("Nie podano telefonu!!!");
            create = false;
        }
        else{
            Pattern pattern = Pattern.compile("\\+\\d\\d\\s\\d\\d\\d\\-\\d\\d\\d\\-\\d\\d\\d");
            Matcher matcher = pattern.matcher(phone.getText());
            if(!matcher.matches()){
                phoneError.setText("Błędny format telefonu!!!\n(Poprawny np. '+48 123-456-789')");
                create = false;
            }
        }

        if(mail.getText().equals("")) {
            mailError.setText("Nie podano maila!!!");
            create = false;
        }
        else{
            if (mail.getText().length() <= 50) {
                Pattern pattern = Pattern.compile(".+@.+\\..+");
                Matcher matcher = pattern.matcher(mail.getText());
                if(!matcher.matches()){
                    mailError.setText("Błędny format maila!!!\n(Poprawny np. 'janKowalski@polska.pl')");
                    create = false;
                }
            }
            else {
                mailError.setText("Za długi mail!!! (Max 50 znaków)");
                create = false;
            }

        }

        if(idNumber.getText().equals("")) {
            idNumberError.setText("Nie podano numeru dowodu osbistego!!!");
            create = false;
        }
        else if(idNumber.getText().length() > 10){
            idNumberError.setText("Za dlugi numer!!! (Max 10 znaków)");
            create = false;
        }

        if(login.getText().equals("")){
            loginError.setText("Nie podano loginu!!!");
            create = false;
        }
        else if(login.getText().length() > 31){
            loginError.setText("Za dlugi login!!! (Max 31 znaków)");
            create = false;
        }

        if(password.getText().equals("")){
            passwordError.setText("Nie podano hasła!!!");
            create = false;
        }
        else if(password.getText().length() < 8 || password.getText().length() > 50){
            passwordError.setText("Niepoprawna długość hasła!!!\n(Min 8 znaków, Max 50 znaków)");
            create = false;
        }
        else{
            String passwordLocal = password.getText();
            boolean constrain = false;

            Pattern patternSpecChar = Pattern.compile("[a-zA-z0-9]*");
            Matcher matcher = patternSpecChar.matcher(passwordLocal);
            if(!matcher.matches()){
                constrain = true;
            }

            Pattern patternAZ = Pattern.compile("[A-Z]+");
            Matcher matcher2 = patternAZ.matcher(passwordLocal);
            if(!matcher2.find()) constrain = false;

            Pattern pattern09 = Pattern.compile("[0-9]+");
            Matcher matcher3 = pattern09.matcher(passwordLocal);
            if(!matcher3.find()) constrain = false;

            if(constrain){
                Pattern patternPassword = Pattern.compile(passwordLocal);
                Matcher matcherFinal = patternPassword.matcher(confirmPassword.getText());
                if(!matcherFinal.matches()){
                    confirmPasswordError.setText("Hasła są różne!!!");
                    create = false;
                }
            }
            else {
                passwordError.setText("Hasło nie zawiera znaku specjalnego,\nwielkiej litery lub cyfry");
                create = false;
            }

        }

        if(create){
            ArrayList<String> newClient = new ArrayList<>();
            newClient.add(0, name.getText());
            newClient.add(1, surname.getText());
            newClient.add(2, phone.getText());
            newClient.add(3, mail.getText());
            newClient.add(4, idNumber.getText());
            newClient.add(5, login.getText());
            newClient.add(6, password.getText());

            DataBaseForClient dataBase = new DataBaseForClient();
            String errorCode = dataBase.createClient(newClient);
            if(errorCode == null){
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Potwierdzenie");
                alertInfo.setHeaderText("Poprawnie stworzono konto.\nPamiętaj, aby wysłać skan Dowodu osobistego na naszego mail\n" +
                        "Bez tego konto nie zostanie potwierdzone, a ty nie bedziesz mogł sie zalogować.");
                alertInfo.showAndWait();
                Stage stage = (Stage)name.getScene().getWindow();
                stage.close();
            }
            else if(errorCode.equals("45000")){
                mailError.setText("Mail jest już zajęty!");
                loginError.setText("Login jest już zajęty!");
            }
            else if(errorCode.equals("45001")){
                loginError.setText("Login jest już zajęty!");
            }
            else if(errorCode.equals("45002")){
                mailError.setText("Mail jest już zajęty!");
            }

        }


    }

}
