package com.company.Controllers;

import com.company.ClassesForDataBase.DataBaseForEmployee;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.company.Controllers.EmployeeController.*;


public class EditController {
    @FXML
    private Label title, carInfo, notNullIdCar, notNullStartDate, notNullEndDate, notNullBail;
    @FXML
    private TextField idCar, startDate, endDate;
    @FXML
    private TextArea comment;
    @FXML
    private CheckBox paid, notPaid;
    @FXML
    private Button confirmEdit, cancelEdit;

    public void initialize() throws SQLException {
        if(type.equals("reservations")) {
            title.setText("Edycja Rezerwacji");
            if(info.getBail().equals("TAK")) paid.setSelected(true);
            else if(info.getBail().equals("NIE")) notPaid.setSelected(true);
        }
        else if(type.equals("rentals")){
            title.setText("Edycja Wypożyczenia");
            paid.setDisable(true);
            notPaid.setDisable(true);
        }

        idCar.setText(info.getIdCar().toString());
        comment.setText(info.getComment());
        startDate.setText(info.getStartDate());
        endDate.setText(info.getEndDate());



        DataBaseForEmployee db = new DataBaseForEmployee();
            carInfo.setText(db.getCar(info.getIdCar()));


    }

    @FXML
    public void onPaidCheckBox(){
        if(notPaid.isSelected()) notPaid.setSelected(false);
    }

    @FXML
    public void onNotPaidCheckBox(){
        if(paid.isSelected()) paid.setSelected(false);
    }

    @FXML
    public void onGivenIdCar(){
        try{
            Integer givenIdCar = Integer.parseInt(idCar.getText());
            DataBaseForEmployee db = new DataBaseForEmployee();
            Integer numberOfCars = db.getNumberOfCars();
            if(givenIdCar > 0 && givenIdCar <= numberOfCars) carInfo.setText(db.getCar(givenIdCar));
        } catch (NumberFormatException e){
            System.out.println("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConfirmEditButton(){
        DataBaseForEmployee db = new DataBaseForEmployee();
        Integer control = 0;
        Integer givenIdCar = 0;
        Date firstDate = null, secondDate = null;
        if(idCar.getText() == null) notNullIdCar.setText("Nie podano numeru samochodu!");

        try{
            Integer numberOfCars = db.getNumberOfCars();
            Integer tmp = Integer.parseInt(idCar.getText());
            givenIdCar = tmp;

            //sprawdzic czy samochod nie jest zajety; + komunikat od do zajety
            if(givenIdCar > numberOfCars || givenIdCar < 0) notNullIdCar.setText("Podany numer samochodu nie istnieje!");
            else control++;
        }
        catch (NumberFormatException e) {
            notNullIdCar.setText("Podana wartość nie jest numerem samochodu!");
        }

        if(startDate.getText() == null) notNullStartDate.setText("Nie podano daty początkowej!");
        else if(endDate.getText() == null) notNullEndDate.setText("Nie podano daty końcowej!");
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date tmp1 = sdf.parse(startDate.getText());
                Date tmp2 = sdf.parse(endDate.getText());
                firstDate = tmp1;
                secondDate = tmp2;

                if (secondDate.before(firstDate))
                    notNullEndDate.setText("Data końcowa jest wczesniejsza niż początkowa!");
                else control++;

            } catch (ParseException e) {
                notNullStartDate.setText("Podano błędny format daty! Oczekiwany: YYYY-MM-DD");
                e.printStackTrace();
            }
        }

        if(control == 2){
            info.setIdCar(givenIdCar);
            info.setComment(comment.getText());
            info.setStartDate(firstDate);
            info.setEndDate(secondDate);
            if(paid.isSelected()) info.setBail("TAK");
            else info.setBail("NIE");

            db.editReservationsOrRentals(info, type);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("POTWIERDZENIE");
            alert.setHeaderText("Poprawnie dokonano Edycji");
            alert.showAndWait();

            Stage st = (Stage)confirmEdit.getScene().getWindow();
            st.close();
            //napisac trigger na aktualizacje unavailable
        }
    }

    public void onCancelEditButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("OSTRZEŻENIE");
        alert.setHeaderText("Czy jest pewien, że chcesz anulować Edycję?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            Stage st = (Stage)confirmEdit.getScene().getWindow();
            st.close();
        }
    }
}

