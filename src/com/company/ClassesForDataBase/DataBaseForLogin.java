/**@author Chlewicki Adam */
package com.company.ClassesForDataBase;

import java.sql.*;
import static com.company.Controllers.EmployeeController.helloMsg;
import static com.company.Controllers.EmployeeController.idEmployee;
import static com.company.Controllers.ClientController.idClient;

/** Klasa do operowania na bazie dla użytkowników niezalogowanych */
public class DataBaseForLogin extends DataBase {

    /** Sprawdzenie czy dany użytkownik istnieje
     *
     * @param who Typ użytkownika (Klient/Pracownik)
     * @param login Login użytkownika
     * @param password Hasło użytkownika
     * @return Zwraca powitalny tekst jeśli dane zgadzają sie, wpw "Error"
     */
    public String checkUser(int who, String login, String password) {
        helloMsg = null;
        try {
            PreparedStatement preparedStatement = null;
            if(who == 0) preparedStatement = connection.prepareStatement("SELECT id_client, name, surname, confirmed FROM clients WHERE login = ? AND password = SHA2(?,256)");
            else if(who == 1) preparedStatement = connection.prepareStatement("SELECT id_employee, name, surname FROM employees WHERE login = ? AND password = SHA2(?,256)");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Integer confirmed = 0;
                if(who == 0) confirmed = resultSet.getInt("confirmed");
                else if(who == 1) confirmed = 1;
                if(confirmed == 1){
                    helloMsg = resultSet.getString("name") + " ";
                    helloMsg += resultSet.getString("surname") + "!";
                    if(who == 1) idEmployee = resultSet.getInt("id_employee");
                    else if(who == 0) idClient = resultSet.getInt("id_client");
                }
            }


            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (helloMsg != null) return helloMsg;
        else return "Error";
    }
}

