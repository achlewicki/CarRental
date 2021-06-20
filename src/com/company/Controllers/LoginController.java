/**@author Adam Chlewicki */
package com.company.Controllers;

import com.company.ClassesForDataBase.DataBaseForLogin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.sql.SQLException;

/** Kontroler dla niezalogowanych użytkowników */
public class LoginController {
    /** Pola do logowania */
    @FXML
    private PasswordField password;
    @FXML
    private TextField login;
    @FXML
    private CheckBox clientCheckBox;
    @FXML
    private CheckBox employeeCheckBox;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    /** Odznaczenie checkboxa gdy drugi zostanie zaznaczony cz.I */
    @FXML
    public void onEmployeeCheckBox(){
        if(clientCheckBox.isSelected()) clientCheckBox.setSelected(false);
    }

    /** Odznaczenie checkboxa gdy drugi zostanie zaznaczony cz.II */
    @FXML
    public void onClientCheckBox(){
        if(employeeCheckBox.isSelected()) employeeCheckBox.setSelected(false);
    }

    /** Weryfikacja podanego loginu i hasła, zalogowanie gdy weryfikacja się powiedzie
     *
     * @throws IOException
     */
    @FXML
    public void loginAction() throws IOException {
        int person = -1;
        if(clientCheckBox.isSelected()) person = 0;
        else if(employeeCheckBox.isSelected()) person = 1;

        if(person == 1 || person == 0){
            DataBaseForLogin db1 = new DataBaseForLogin();
            String dbLogin = login.getText();
            String dbPassword = password.getText();
            String helloMsg = db1.checkUser(person, dbLogin, dbPassword);

            if(helloMsg.equals("Error")) errorLabel.setText("Podano błedny login, hasło, rolę\nlub twoje konto jest niepotwierdzone!");
            else{
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Witaj " + helloMsg);
                alert2.showAndWait();
                Stage stage = new Stage();
                if(person == 0){
                    TabPane clientPane = FXMLLoader.load(getClass().getResource("/com/company/Views/ClientPage.fxml"));
                    stage.setScene(new Scene(clientPane, 1200, 750));
                }

                else {
                    TabPane employeePane = FXMLLoader.load((getClass().getResource("/com/company/Views/EmployeePage.fxml")));
                    stage.setScene(new Scene(employeePane, 1200, 750));
                }

                stage.setResizable(false);
                stage.show();

                Stage st2 = (Stage)employeeCheckBox.getScene().getWindow();
                st2.close();

                try{
                    db1.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    /** Otwarcie okna z formularzem do dodania nowego klienta
     *
     * @throws IOException
     */
    @FXML
    public void onCreateClientButton() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load((getClass().getResource("/com/company/Views/CreateClientForm.fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.setResizable(false);
        stage.showAndWait();
    }
}


