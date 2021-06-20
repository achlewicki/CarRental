/**@author Chlewicki Adam */
package com.company.ClassesForDataBase;

import com.company.Classes.CarDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** Abstrakcyjna klasa do połączenia z bazą i pobrania informacji kiedy samochód jest niedostępny*/
public abstract class DataBase{
    protected Connection connection = null;
    protected ResultSet resultSet = null;


    /** Nawiązanie połączenia z bazą */
    public DataBase(){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /** Ustalenie kiedy samochód jest niedostępny
     *
     * @param idCar Pole z obiektu typu Car
     * @return Zwraca listę dat kiedy samochód będzie użyty
     */
    public ObservableList<CarDate> whenCarIsNotAvailable(Integer idCar){
        ObservableList<CarDate> carDates = FXCollections.observableArrayList();

        try{
            Statement stm = connection.createStatement();
            resultSet = stm.executeQuery("SELECT planning_rent, planning_return FROM reservations " +
                    "WHERE cars_id_car = " + idCar + " AND confirmed = 0");
            while(resultSet.next()){
                carDates.add(new CarDate(idCar, resultSet.getDate("planning_rent"),
                        resultSet.getDate("planning_return")));
            }

            resultSet = stm.executeQuery("SELECT date_of_rent, date_of_return FROM rentals " +
                    "WHERE cars_id_car = " + idCar);
            while(resultSet.next()){
                carDates.add(new CarDate(idCar, resultSet.getDate("date_of_rent"),
                        resultSet.getDate("date_of_return")));
            }

            resultSet = stm.executeQuery("SELECT date_of_service_begin, date_of_service_end FROM cars_services " +
                    "WHERE cars_id_car = " + idCar);
            while(resultSet.next()){
                carDates.add(new CarDate(idCar, resultSet.getDate("date_of_service_begin"),
                        resultSet.getDate("date_of_service_end")));
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return carDates;
    }

    /** Zamknięcie połączenia z bazą
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        try{
            connection.close();
            resultSet.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
