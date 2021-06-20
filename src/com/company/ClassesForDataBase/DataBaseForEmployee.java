/**@author Chlewicki Adam */
package com.company.ClassesForDataBase;

import com.company.Classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/** Klasa do operowania na bazie przez Pracownika */
public class DataBaseForEmployee extends DataBase {

    /** Pobranie daty ostatniego logowania, liczby rezerwacji i wypożyczeń obsługiwanych przez tego pracownika,
     * a także ilu jest niepotwierdzonych klientów
     * @param idEmployee Identyfikator Pracownika
     * @return Zwraca listę stringów składającą się z daty ostatniego logowania; liczby rezerwacji,
     * wypożyczeń obsługiwanych przez tego pracownika i niepotwierdzonych klientów
     */
    public ArrayList<String> getBasicInfo(Integer idEmployee) {
        Integer count = 0;
        ArrayList<String> basicInfo = new ArrayList<>();

        try {
            Statement stm = connection.createStatement();

            resultSet = stm.executeQuery("SELECT last_successful_login FROM employees WHERE id_employee = '" + idEmployee + "'");
            resultSet.next();
            basicInfo.add(resultSet.getString("last_successful_login"));

            resultSet = stm.executeQuery("SELECT COUNT(id_rent) AS c FROM rentals WHERE employees_id_employee = '" + idEmployee + "'");
            resultSet.next();
            basicInfo.add(resultSet.getString("c"));

            resultSet = stm.executeQuery("SELECT COUNT(id_reservation) AS c FROM reservations WHERE " +
                    "confirmed = 0 AND employees_id_employee = '" + idEmployee + "'");
            resultSet.next();
            basicInfo.add(resultSet.getString("c"));

            resultSet = stm.executeQuery("SELECT 1 FROM clients WHERE confirmed = 0");
            while (resultSet.next()) count++;
            basicInfo.add(count.toString());

            stm.executeQuery("UPDATE employees SET last_successful_login = current_date() WHERE id_employee = " + idEmployee);
            stm.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return basicInfo;
    }

    /** Rezerwacje obsługiwane przez Pracownika
     *
     * @param idEmployee Identyfikator pracownika
     * @param idCar Identyfikator samochodu
     * @param type Dla "kogo" mają zasotać pobrane dane
     * @return Zwraca listę obiektów rezerwacji pełnych lub niecałych
     */
    public ObservableList<Reservation> getReservations(Integer idEmployee, Integer idCar, String type){
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        try{
            Statement stm = connection.createStatement();

            if(type.equals("forEmployee")){
                resultSet = stm.executeQuery("SELECT r.id_reservation, r.cars_id_car, c.id_client, c.name, c.surname, c.phone, c.mail, r.comments, r.planning_rent," +
                        "r.planning_return, r.is_bail_paid FROM reservations r" +
                        " JOIN clients c ON r.clients_id_client = c.id_client" +
                        " WHERE r.employees_id_employee =" + idEmployee + " AND r.confirmed = 0");

                while(resultSet.next()){
                    reservationList.add(new Reservation(resultSet.getInt("r.id_reservation"), resultSet.getInt("r.cars_id_car"),
                            resultSet.getInt("c.id_client"),
                            resultSet.getString("c.name"), resultSet.getString("c.surname"),
                            resultSet.getString("c.phone"), resultSet.getString("c.mail"),
                            resultSet.getString("r.comments"), resultSet.getDate("r.planning_rent"),
                            resultSet.getDate("r.planning_return"), resultSet.getInt("r.is_bail_paid")));
                }
            }

            else if(type.equals("forDates")){
                resultSet = stm.executeQuery("SELECT id_reservation, planning_rent, " +
                        "planning_return FROM reservations WHERE cars_id_car = " + idCar);
                while(resultSet.next())
                    reservationList.add(new Reservation(resultSet.getInt("id_reservation"),null,null,null,null,
                            null,null, null, resultSet.getDate("planning_rent"),
                            resultSet.getDate("planning_return"), 0));
            }
            stm.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }

        return reservationList;
    }

    /** Rezerwacje obsługiwane przez Pracownika
     *
     * @param idEmployee Identyfikator pracownika
     * @param idCar Identyfikator samochodu
     * @param type Dla "kogo" mają zasotać pobrane dane
     * @return Zwraca listę obiektów wypożyczeń pełnych lub niecałych
     */
    public ObservableList<Rental> getRentals(Integer idEmployee, Integer idCar, String type){
        ObservableList<Rental> rentalList = FXCollections.observableArrayList();
        try{
            Statement stm = connection.createStatement();

            if(type.equals("forEmployee")){
                resultSet = stm.executeQuery("SELECT r.id_rent, r.cars_id_car, c.id_client, c.name, c.surname, c.phone, c.mail, " +
                        "r.comments, r.date_of_rent, date_of_return FROM rentals r " +
                        "JOIN clients c ON r.clients_id_client = c.id_client " +
                        "WHERE r.employees_id_employee =" + idEmployee);

                while(resultSet.next()){
                    rentalList.add(new Rental(resultSet.getInt("r.id_rent"), resultSet.getInt("r.cars_id_car"),
                            resultSet.getInt("c.id_client"),
                            resultSet.getString("c.name"), resultSet.getString("c.surname"),
                            resultSet.getString("c.phone"), resultSet.getString("c.mail"),
                            resultSet.getString("r.comments"), resultSet.getDate("r.date_of_rent"),
                            resultSet.getDate("r.date_of_return")));
                }
            }

            else if(type.equals("forDates")){
                resultSet = stm.executeQuery("SELECT id_rent, date_of_rent, date_of_return FROM rentals WHERE " +
                        "cars_id_car = " + idCar);
                while(resultSet.next())
                    rentalList.add(new Rental(resultSet.getInt("id_rent"),null,null,null,null,
                            null,null, null, resultSet.getDate("date_of_rent"),
                            resultSet.getDate("date_of_return")));
            }
            stm.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return rentalList;
    }

    /** Pobranie usług w jakich znajduje się poszczególny samochód
     *
     * @param idCar Identyfikator samochodu
     * @param type Dla "kogo" mają zostać pobrane dane
     * @return Zwraca listę obiektów usług dla samochodu
     */
    public ObservableList<CarServices> getServices(Integer idCar, String type){
        ObservableList<CarServices> carServicesList = FXCollections.observableArrayList();

        try{
            Statement stm = connection.createStatement();
            if(type.equals("forDates")){
                resultSet = stm.executeQuery("SELECT id_log, date_of_service_begin, date_of_service_end, " +
                        "s.type_of_service FROM cars_services JOIN services s ON services_id_service = s.id_service WHERE cars_id_car = " + idCar);

                while(resultSet.next()) carServicesList.add(new CarServices(resultSet.getInt("id_log"),
                        resultSet.getDate("date_of_service_begin"), resultSet.getDate("date_of_service_end"),
                        resultSet.getString("type_of_service")));
            }

            else if(type.equals("forComboBox")){
                resultSet = stm.executeQuery("SELECT id_service, name, type_of_service, s.comments FROM services s " +
                        "JOIN partners ON partners_id_partner = id_partner");

                while(resultSet.next()) carServicesList.add(new CarServices(resultSet.getInt("id_service"),
                        resultSet.getString("name") + " - " + resultSet.getString("type_of_service"),
                        resultSet.getString("comments")));
            }
            stm.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return carServicesList;
    }

    /** Opis samochodu
     *
     * @param idCar Identyfikator samochodu
     * @return Zwraca tekst o samochodzie
     * @throws SQLException
     */
    public String getCar(Integer idCar) throws SQLException {
        String carInfo = null;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT brand, model, year FROM  cars where id_car=?") ;
            preparedStatement.setInt(1, idCar);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            carInfo = resultSet.getString("brand") + " " + resultSet.getString("model") +
                    " " + resultSet.getInt("year");
            preparedStatement.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return  carInfo;
    }

    /** Pobiera liczbę samochodów w bazie
     *
     * @return Liczbę samochodów
     */
    public Integer getNumberOfCars(){
        Integer count = 0;

        try{
            Statement stm = connection.createStatement();
            resultSet = stm.executeQuery("SELECT COUNT(id_car) AS c FROM cars");
            resultSet.next();
            count = resultSet.getInt("c");
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /** Edycja rezerwacji i wypożyczenia
     *
     * @param info Obiekt na Rezerwacje lub Wypozyczenie
     * @param type Określenie czy to Rezerwacja czy Wypożyczenie
     */
    public void editReservationsOrRentals(ReservationRental info, String type){
        try{
            if(type.equals("reservations")){
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reservations SET cars_id_car = ?, " +
                        "planning_rent = ?, planning_return = ?, is_bail_paid = ?, comments = ? WHERE id_reservation = ?");
                preparedStatement.setInt(1,info.getIdCar());
                preparedStatement.setString(2, info.getStartDate());
                preparedStatement.setString(3, info.getEndDate());
                if(info.getBail().equals("TAK")) preparedStatement.setInt(4, 1);
                else preparedStatement.setInt(4, 0);
                preparedStatement.setString(5, info.getComment());
                preparedStatement.setInt(6, info.getId());
                preparedStatement.executeQuery();
                preparedStatement.close();
            }


            else if(type.equals("rentals")){
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE rentals SET cars_id_car = ?, " +
                        "date_of_rent = ?, date_of_return = ?, comments = ? WHERE id_rent = ?");

                preparedStatement.setInt(1,info.getIdCar());
                preparedStatement.setString(2, info.getStartDate());
                preparedStatement.setString(3, info.getEndDate());
                preparedStatement.setString(4, info.getComment());
                preparedStatement.setInt(5, info.getId());
                preparedStatement.executeQuery();
                preparedStatement.close();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /** Potweirdzenie rezerwacji w bazie - utworzenie wypożyczenia
     *
     * @param info Obiekt na Rezerwację
     * @param idEmployee Identyfikator Pracownika
     */
    public void confirmReservation(ReservationRental info, Integer idEmployee){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO rentals(employees_id_employee," +
                    "cars_id_car, clients_id_client, date_of_rent, date_of_return, comments) VALUES" +
                    "(?,?,?,?,?,?)");
            preparedStatement.setInt(1, idEmployee);
            preparedStatement.setInt(2, info.getIdCar());
            preparedStatement.setInt(3, info.getIdClient());
            preparedStatement.setString(4, info.getStartDate());
            preparedStatement.setString(5, info.getEndDate());
            preparedStatement.setString(6, info.getComment());
            preparedStatement.executeQuery();
            preparedStatement.close();

            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE reservations SET confirmed = 1 WHERE id_reservation = " + info.getId());
            stm.close();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Anulowanie rezerwacji/wypożyczenia w bazie
     *
     * @param info Obiekt na rezerwację lub wypożyczenie
     */
    public void cancelReservation(ReservationRental info){
        try{
            Statement stm = connection.createStatement();
            stm.executeQuery("DELETE FROM reservations WHERE id_reservation = " + info.getId());
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Usunięcie wypożyczenia lub rezerwacji
     *
     * @param info Obiekt na rezerwację lub wypożyczenie
     */
    public void deleteRental(ReservationRental info){
        try{
            Statement stm = connection.createStatement();
            stm.executeQuery("DELETE FROM rentals WHERE id_rent = " + info.getId());
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Pobranie listy klientów
     *
     * @return Zwrócenie listy obiektów typu Person
     */
    public ObservableList<Person> getClients(){
        ObservableList<Person> clientList = FXCollections.observableArrayList();
        try{
            Statement stm = connection.createStatement();
            resultSet = stm.executeQuery("SELECT id_client, name, surname, " +
                    "login, phone, mail, id_number, confirmed FROM clients");
            while(resultSet.next()){
                clientList.add(new Person(resultSet.getInt("id_client"), resultSet.getString("name"),
                        resultSet.getString("surname"), resultSet.getString("login"),
                        resultSet.getString("phone"), resultSet.getString("mail"),
                        resultSet.getString("id_number"), resultSet.getInt("confirmed")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return clientList;
    }

    /** Potwierdzenie klienta w bazie
     *
     * @param id Identyfikator klienta
     */
    public void confirmClient(Integer id){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET confirmed = 1 WHERE "
            + "id_client = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Pobranie listy samochodów z bazy
     *
     * @return Zwraca listę obiektów typu Car
     */
    public ObservableList<Car> getCars(){
        ObservableList<Car> carList = FXCollections.observableArrayList();

        try{
            Statement stm = connection.createStatement();
            resultSet = stm.executeQuery("SELECT id_car, brand, model, plate_number, end_of_inspection," +
                    "end_of_insurance, comments FROM cars");

            while(resultSet.next())
                carList.add(new Car(resultSet.getInt("id_car"), resultSet.getString("brand"),
                        resultSet.getString("model"), resultSet.getString("plate_number"),
                        resultSet.getDate("end_of_inspection"), resultSet.getDate("end_of_insurance"),
                        resultSet.getString("comments")));
            stm.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return carList;
    }

    /** Dodanie wpisu w historii usług na zadanym samochodzie
     *
     * @param idCar Identyfikator samochodu
     * @param idService Identyfikator usługi
     * @param startDate Początek usługi
     * @param endDate Koniec usługi
     * @param comment Komentarz
     * @return True jeśli wszystko poszło OK, wpw false
     */
    public boolean sendCarForSevice(Integer idCar, Integer idService, Date startDate, Date endDate, String comment){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cars_services(cars_id_car, " +
                    "services_id_service, date_of_service_begin, date_of_service_end, comments) VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1,idCar);
            preparedStatement.setInt(2,idService);
            preparedStatement.setDate(3,startDate);
            preparedStatement.setDate(4,endDate);
            preparedStatement.setString(5, comment);
            preparedStatement.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
