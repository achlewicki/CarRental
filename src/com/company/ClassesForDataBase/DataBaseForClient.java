/**@author Chlewicki Adam */
package com.company.ClassesForDataBase;

import com.company.Classes.Car;
import com.company.Classes.Person;
import com.company.Classes.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;

/** Klasa do operowania na bazie dla Klienta */
public class DataBaseForClient extends DataBase {

    /** Pobranie wszystkich samochodów z bazy
     *
     * @return Zwraca listę wszystkich samochodów z bazy
     */
    public ObservableList<Car> selectTableCars() {
        ObservableList<Car> carList = FXCollections.observableArrayList();

        try {
            Statement stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT id_car, brand, model, year, number_of_seats, price, currency FROM cars");
            while (resultSet.next()) {
                carList.add(new Car(resultSet.getInt("id_car"), resultSet.getString("brand"),
                        resultSet.getString("model"), resultSet.getInt("year"),
                        resultSet.getInt("number_of_seats"), (double)resultSet.getInt("price")/100,
                        resultSet.getString("currency")));
            }
            stm.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }

    /** Pobranie z bazy wszystkich rezerwacji dla wybranego klienta
     *
     * @param idClient Identyfikator klienta w bazie
     * @return Zwraca listę wszystkich rezerwacji
     */
    public ObservableList<Reservation> getReservations(Integer idClient){
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        try{
            Statement stm = connection.createStatement();

            resultSet = stm.executeQuery("SELECT r.id_reservation, c.brand, c.model, r.comments, r.planning_rent," +
                    "r.planning_return, r.is_bail_paid, r.confirmed, e.phone, e.mail FROM reservations r" +
                    " JOIN cars c ON r.cars_id_car = c.id_car" +
                    " JOIN employees e ON r.employees_id_employee = e.id_employee" +
                    " WHERE r.clients_id_client =" + idClient);

            while(resultSet.next()){
                String contact = null;
                contact = resultSet.getString("e.mail") + "     " + resultSet.getString("e.phone");
                reservationList.add(new Reservation(resultSet.getInt("r.id_reservation"),
                                resultSet.getString("c.brand"),
                                resultSet.getString("c.model"), resultSet.getDate("r.planning_rent"),
                                resultSet.getDate("r.planning_return"), resultSet.getInt("r.is_bail_paid"),
                                contact, resultSet.getString("r.comments"), resultSet.getInt("r.confirmed")));
            }
            stm.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }

        return reservationList;
    }

    /** Pobranie z bazy informacji o kliencie
     *
     * @param idClient Identyfikator klienta w bazie
     * @return Zwraca obiekt typu Person dla klienta
     */
    public Person getBasicInfo(Integer idClient){
        Person client = null;
        try{
            Statement stm = connection.createStatement();

            resultSet = stm.executeQuery("SELECT id_client, name, surname, phone, mail, id_number, login FROM clients" +
                    " WHERE id_client = " + idClient);

            if(resultSet.next()){
                client = new Person(resultSet.getInt("id_client"),
                        resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("login"), resultSet.getString("phone"),
                        resultSet.getString("mail"), resultSet.getString("id_number"), 1);
            }
            stm.close();
        }

        catch(SQLException e){
            e.printStackTrace();
        }

        return client;
    }

    public Boolean checkLogin(String login, Person client){
        boolean confirm = false;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM clients WHERE" +
                    " login = ? AND id_client = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setInt(2, client.getId());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) confirm = true;
            else confirm = false;
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return confirm;
    }

    /** Sprawdzenie czy podane hasło przez użytkownika jest takie same jak w bazie
     *
     * @param passwd Tekst podany podczas próby edycji informacji o kliencie przez klienta
     * @param client Obiekt klasy Person, informacje o kliencie
     * @return Zwraca true jeśli hasła się zgadzają, false wpw
     */
    public Boolean checkPassword(String passwd, Person client){
        boolean confirm = false;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM clients WHERE" +
                    " password = SHA2(?,256) AND id_client = ?");
            preparedStatement.setString(1, passwd);
            preparedStatement.setInt(2, client.getId());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) confirm = true;
            else confirm = false;
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return confirm;
    }

    /** Zmiana imienia w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changeName(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET name = ? " +
                    " WHERE id_client = ?" );
            preparedStatement.setString(1, client.getName());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Zmiana nazwiska w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changeSurname(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET surname = ? " +
                    " WHERE id_client = ?" );
            preparedStatement.setString(1, client.getSurname());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Zmiana telefonu w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changePhone(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET phone = ? WHERE" +
                    " id_client = ?" );
            preparedStatement.setString(1, client.getPhone());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Zmiana maila w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changeMail(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET mail= ? WHERE" +
                    " id_client = ?" );
            preparedStatement.setString(1, client.getMail());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Zmiana numeru dowodu osobistego w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changeIdNumber(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET id_number = ? " +
                    " WHERE id_client = ?" );
            preparedStatement.setString(1, client.getIdNumber());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Zmiana loginu w bazie
     *
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public boolean changeLogin(Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM clients WHERE login = ?");
            preparedStatement.setString(1,client.getLogin());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) return false;
            preparedStatement.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET login = ? WHERE" +
                    " id_client = ?" );
            preparedStatement.setString(1, client.getLogin());
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /** Zmiana hasła w bazie
     * @param passwd Nowe hasło
     * @param client Obiekt typu Person, informacje o kliencie
     */
    public void changePassword(String passwd, Person client){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET password = SHA2(?,256)" +
                    " WHERE id_client = ?" );
            preparedStatement.setString(1, passwd);
            preparedStatement.setInt(2,client.getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Utworzenie nowej rezerwacji przez klienta w bazie
     *
     * @param idClient Identyfikator klienta
     * @param idCar Identyfikator samochodu
     * @param startDate Początek rezerwacji
     * @param endDate Koniec rezerwacji
     * @param address Opcjonalny adres dostawy samochodu
     */
    public void setNewReservation(Integer idClient, Integer idCar, Date startDate, Date endDate, String address){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(" +
                    "employees_id_employee, cars_id_car, planning_rent, planning_return, clients_id_client," +
                    "is_bail_paid, comments, confirmed) VALUES(1,?,?,?,?,0,?,0)");

//            Statement stm = connection.createStatement();
//            resultSet = stm.executeQuery("SELECT e.id_employee, COUNT(r.id_reservation) FROM employees e" +
//                        " LEFT JOIN reservations r ON e.id_employee = r.employees_id_employee " +
//                        " GROUP BY e.id_employee" +
//                        " ORDER BY COUNT(r.id_reservation) ASC LIMIT 1");
//            resultSet.next();
//            preparedStatement.setInt(1,resultSet.getInt("e.id_employee"));
            preparedStatement.setInt(1, idCar);
            preparedStatement.setDate(2, startDate);
            preparedStatement.setDate(3, endDate);
            preparedStatement.setInt(4,idClient);
            if(address == null) preparedStatement.setString(5, address);
            else preparedStatement.setString(5, address);
            preparedStatement.executeQuery();
            //stm.close();
            preparedStatement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Utworzenie nowego klienta w bazie
     *
     * @param newClient Lista danych o kliencie wypełniona przez klienta
     */
    public String createClient(ArrayList<String> newClient){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clients(name, surname, phone," +
                    "mail, id_number, login, password, confirmed) VALUES (?,?,?,?,?,?,SHA2(?,256),0)");

            preparedStatement.setString(1, newClient.get(0));
            preparedStatement.setString(2, newClient.get(1));
            preparedStatement.setString(3, newClient.get(2));
            preparedStatement.setString(4, newClient.get(3));
            preparedStatement.setString(5, newClient.get(4));
            preparedStatement.setString(6, newClient.get(5));
            preparedStatement.setString(7, newClient.get(6));

            preparedStatement.executeQuery();
            preparedStatement.close();

        }
        catch (SQLException e){
            e.printStackTrace();
            return e.getSQLState();
        }
        return null;
    }
}

