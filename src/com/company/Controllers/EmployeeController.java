/**@author Chlewicki Adam */
package com.company.Controllers;

import com.company.Classes.*;
import com.company.ClassesForDataBase.DataBaseForEmployee;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/** Kontroler dla Pracownika */
public class EmployeeController {
    public static String helloMsg;
    public static Integer idEmployee;

    /** Pola do podstawowych informacji */
    @FXML
    private Label helloLabel;
    @FXML
    private Label lastLogin;
    @FXML
    private Label numRent;
    @FXML
    private Label numRes;
    @FXML
    private Label unconfirmedClients;

    /** Tabela do rezerwacji */
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Integer> idRes;
    @FXML
    private TableColumn<Reservation, Integer> idCarRes;
    @FXML
    private TableColumn<Reservation, String> nameRes;
    @FXML
    private TableColumn<Reservation, String> surnameRes;
    @FXML
    private TableColumn<Reservation, String> phoneRes;
    @FXML
    private TableColumn<Reservation, String> mailRes;
    @FXML
    private TableColumn<Reservation, String> startRes;
    @FXML
    private TableColumn<Reservation, String> endRes;
    @FXML
    private TableColumn<Reservation, String> commentsRes;
    @FXML
    private TableColumn<Reservation, String> bailRes;

    /** Tabela do Wypożyczeń */
    @FXML
    private TableView<Rental> rentalsTable;
    @FXML
    private TableColumn<Rental, Integer> idRent;
    @FXML
    private TableColumn<Rental, Integer> idCarRent;
    @FXML
    private TableColumn<Rental, String> nameRent;
    @FXML
    private TableColumn<Rental, String> surnameRent;
    @FXML
    private TableColumn<Rental, String> phoneRent;
    @FXML
    private TableColumn<Rental, String> mailRent;
    @FXML
    private TableColumn<Rental, Date> startRent;
    @FXML
    private TableColumn<Rental, Date> endRent;
    @FXML
    private TableColumn<Rental, String> commentsRent;
    @FXML
    private TableColumn<Rental, String> statusRent;

    /** Tabela do klientów */
    @FXML
    private TableView<Person> clientsTable;
    @FXML
    private TableColumn<Person, Integer> idClient;
    @FXML
    private TableColumn<Person, String> nameClient;
    @FXML
    private TableColumn<Person, String> surnameClient;
    @FXML
    private TableColumn<Person, String> loginClient;
    @FXML
    private TableColumn<Person, String> phoneClient;
    @FXML
    private TableColumn<Person, String> mailClient;
    @FXML
    private TableColumn<Person, String> idNumberClient;
    @FXML
    private TableColumn<Person, String> confirmedClient;

    /** Tabela do Samochodów */
    @FXML
    private TableView<Car> carTable;
    @FXML
    private TableColumn<Car, Integer> idCarCol;
    @FXML
    private TableColumn<Car, String> brandCol;
    @FXML
    private TableColumn<Car, String> modelCol;
    @FXML
    private TableColumn<Car, String> plateCol;
    @FXML
    private TableColumn<Car, Date> inspectionCol;
    @FXML
    private TableColumn<Car, Date> insuranceCol;
    @FXML
    private TableColumn<Car, String> commentCol;

    /** Tabela do dat z rezerwacji */
    @FXML
    private TableView<Reservation> resDateTable;
    @FXML
    private TableColumn<Reservation, Integer> idResDate;
    @FXML
    private TableColumn<Reservation, Date> startResDate;
    @FXML
    private TableColumn<Reservation, Date> endResDate;

    /** Tabela do dat z wypożyczeń */
    @FXML
    private TableView<Rental> rentDateTable;
    @FXML
    private TableColumn<Rental, Integer> idRentDate;
    @FXML
    private TableColumn<Rental, Date> startRentDate;
    @FXML
    private TableColumn<Rental, Date> endRentDate;

    /** Tabela do dat z usług */
    @FXML
    private TableView<CarServices> servDateTable;
    @FXML
    private TableColumn<CarServices, Integer> idServDate;
    @FXML
    private TableColumn<CarServices, Date> startServDate;
    @FXML
    private TableColumn<CarServices, Date> endServDate;
    @FXML
    private TableColumn<CarServices, String> typeServDate;

    /** Pola do wysłania samochodu na serwis/usługę */
    @FXML
    private ComboBox<CarServices> servicesCombo = new ComboBox<>();
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private Label datesError;
    @FXML
    private TextField priceField;

    private DataBaseForEmployee dataBase = new DataBaseForEmployee();
    private ArrayList<String> basicInfo = new ArrayList<>();

    /** Alerty */
    private Alert alertError = new Alert(Alert.AlertType.ERROR);
    private Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

    public static ReservationRental info;
    public static String type;
    public static Person client;

    @FXML
    public void initialize() throws SQLException {
        helloLabel.setText(helloMsg);

        /** Załadowanie Informacji do wyświetlenia dla Pracownika */
        basicInfo = dataBase.getBasicInfo(idEmployee);
        lastLogin.setText(basicInfo.get(0));
        numRent.setText(basicInfo.get(1));
        numRes.setText(basicInfo.get(2));
        unconfirmedClients.setText(basicInfo.get(3));


        /** Wczytanie Rezerwacji obsługiwanych przez pracownika */
        idRes.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCarRes.setCellValueFactory(new PropertyValueFactory<>("idCar"));
        nameRes.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameRes.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneRes.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailRes.setCellValueFactory(new PropertyValueFactory<>("mail"));
        commentsRes.setCellValueFactory(new PropertyValueFactory<>("comment"));
        startRes.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endRes.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        bailRes.setCellValueFactory(new PropertyValueFactory<>("bail"));

        reservationsTable.setItems(dataBase.getReservations(idEmployee,null,"forEmployee"));


        /** Wzytanie Wypożyczeń obsługiwanych przez pracownika */
        idRent.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCarRent.setCellValueFactory(new PropertyValueFactory<>("idCar"));
        nameRent.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameRent.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneRent.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailRent.setCellValueFactory(new PropertyValueFactory<>("mail"));
        commentsRent.setCellValueFactory(new PropertyValueFactory<>("comment"));
        startRent.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endRent.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusRent.setCellValueFactory(new PropertyValueFactory<>("status"));

        rentalsTable.setItems(dataBase.getRentals(idEmployee, null, "forEmployee"));


        /** Wczytanie wszystkich klientów to tabeli*/
        idClient.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameClient.setCellValueFactory(new PropertyValueFactory<>("surname"));
        loginClient.setCellValueFactory(new PropertyValueFactory<>("login"));
        phoneClient.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailClient.setCellValueFactory(new PropertyValueFactory<>("mail"));
        idNumberClient.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        confirmedClient.setCellValueFactory(new PropertyValueFactory<>("confirmed"));

        clientsTable.setItems(dataBase.getClients());

        /** Wczytanie samochodów do tabeli */
        idCarCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        plateCol.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        inspectionCol.setCellValueFactory(new PropertyValueFactory<>("endOfInspection"));
        insuranceCol.setCellValueFactory(new PropertyValueFactory<>("endOfInsurance"));
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        confirmedClient.setCellValueFactory(new PropertyValueFactory<>("confirmed"));

        carTable.setItems(dataBase.getCars());

        /** Wczytanie dat do tabel z datami rezerwacji, wypożyczeń i usług */
        idResDate.setCellValueFactory(new PropertyValueFactory<>("id"));
        startResDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endResDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        idRentDate.setCellValueFactory(new PropertyValueFactory<>("id"));
        startRentDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endRentDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        idServDate.setCellValueFactory(new PropertyValueFactory<>("idLog"));
        startServDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endServDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        typeServDate.setCellValueFactory(new PropertyValueFactory<>("serviceDes"));

        /** Wypełnienie ComboBoxa polami z listy obiektów typu CarServices */
        servicesCombo.setItems(dataBase.getServices(0, "forComboBox"));
        servicesCombo.setCellFactory(new Callback<ListView<CarServices>, ListCell<CarServices>>() {
            @Override
            public ListCell<CarServices> call(ListView<CarServices> arg0) {
                ListCell<CarServices> cell = new ListCell<CarServices>() {

                    @Override
                    public void updateItem(CarServices carServices, boolean empty) {
                        super.updateItem(carServices, empty);
                        if (carServices != null) {
                            setText(carServices.getService());
                        } else {
                            setText(null);
                        }
                    }

                };
                return cell;
            }
        });

        servicesCombo.setButtonCell(new ListCell<CarServices>() {
            @Override
            protected void updateItem(CarServices item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getService());
                } else {
                    setText(null);
                }

            }
        });


        alertError.setTitle("BLAD");
        alertError.setContentText(null);

        alertConfirm.setTitle("POTWIERDŹ");
        alertConfirm.setContentText(null);

        alertInfo.setTitle("POTWIERDZENIE");
        alertInfo.setContentText(null);
    }

    /** Edycja rezerwacji
     *
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onEditReservationButton() throws IOException, SQLException {
        info = reservationsTable.getSelectionModel().getSelectedItem();
        type = "reservations";
        if(info != null){
            try{
                Stage stage = new Stage();
                AnchorPane editPane = FXMLLoader.load(getClass().getResource("/com/company/Views/EditPage.fxml"));
                stage.setScene(new Scene(editPane));
                stage.setResizable(false);
                stage.showAndWait();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            reservationsTable.setItems(dataBase.getReservations(idEmployee, null, "forEmployee"));
        }

        else if(info == null){
            alertError.setHeaderText("Nie wybrano Rezerwacji!");
            alertError.showAndWait();
        }
    }

    /** Edycja Wypożyczenia */
    @FXML
    public void onEditRentalButton(){
        info = rentalsTable.getSelectionModel().getSelectedItem();
        type = "rentals";

        if(info != null){
            try{
                Stage stage = new Stage();
                AnchorPane editPane = FXMLLoader.load(getClass().getResource("/com/company/Views/EditPage.fxml"));
                stage.setScene(new Scene(editPane));
                stage.setResizable(false);
                stage.showAndWait();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            rentalsTable.setItems(dataBase.getRentals(idEmployee, null, "forEmployee"));
        }
        else if(info == null) {
            alertError.setHeaderText("Nie wybrano Rezerwacji!");
            alertError.showAndWait();
        }
    }

    /** Potwierdzenie Rezerwacji = Utworzenie Wypożyczenia
     *
     * @throws SQLException
     */
    @FXML
    public void onConfirmReservationButton() throws SQLException {
        info = reservationsTable.getSelectionModel().getSelectedItem();

        if(info == null) {
            alertError.setHeaderText("Nie wybrano Rezerwacji!");
            alertError.showAndWait();
        }

        else if(info.getBail().equals("NIE")){
            alertError.setHeaderText("Nie można potwierdzić nieopłaconej Rezerwacji!");
            alertError.showAndWait();
        }


        else if(info != null){
            dataBase.confirmReservation(info, idEmployee);

            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("POTWIERDZENIE");
            alertInfo.setHeaderText("Rezerwacja Nr: " + info.getId() + " została potwierdzona.");
            alertInfo.showAndWait();

            basicInfo.clear();
            basicInfo = dataBase.getBasicInfo(idEmployee);
            numRent.setText(basicInfo.get(1));
            numRes.setText(basicInfo.get(2));

            reservationsTable.setItems(dataBase.getReservations(idEmployee, null, "forEmployee"));
            rentalsTable.setItems(dataBase.getRentals(idEmployee, null, "forEmployee"));
        }
    }

    /** Anulowanie Rezerwacji */
    @FXML
    public void onCancelReservationButton(){
        info = reservationsTable.getSelectionModel().getSelectedItem();
        type = "reservations";
        if(info == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie wybrano Rezerwacji!");
            alert.showAndWait();
        }
        else if(info != null){
            alertConfirm.setHeaderText("Czy napewno chcesz ANULOWAĆ Rezerwację Nr: " + info.getId() + "?");

            Optional<ButtonType> result = alertError.showAndWait();
            if(result.get() == ButtonType.OK){
                dataBase.cancelReservation(info);
                reservationsTable.setItems(dataBase.getReservations(idEmployee, null, "forEmployee"));
            }
        }
    }

    /** Usunięcie wypożyczenia */
    @FXML
    public void onDeleteRentalButton(){
        info = rentalsTable.getSelectionModel().getSelectedItem();
        type = "rentals";

        if(info == null) {
            alertError.setHeaderText("Nie wybrano Wypożyczenia!");
            alertError.showAndWait();
        }
        else if(info.getStatus().equals("W TRAKCIE")){
            alertError.setHeaderText("Nie można usunąć trwającego Wypożyczenia!");
            alertError.showAndWait();
        }
        else if(info.getStatus().equals("ZAKONCZONE")){
            alertError.setHeaderText("Nie można usunąć zakończonego Wypożyczenia!");
            alertError.showAndWait();
        }
        else if(info != null){
            alertConfirm.setTitle("Czy napewno chcesz USUNĄĆ Wypożyczenie Nr: " + info.getId() + "?");

            Optional<ButtonType> result = alertConfirm.showAndWait();
            if(result.get() == ButtonType.OK){
                dataBase.deleteRental(info);
                rentalsTable.setItems(dataBase.getRentals(idEmployee, null, "forEmployee"));
            }
        }
    }

    /** Potwierdzenie Klienta */
    @FXML
    public void onConfirmClientButton(){
        client = clientsTable.getSelectionModel().getSelectedItem();

        if(client == null){
            alertError.setHeaderText("Nie wybrano Klienta!");
            alertError.showAndWait();
        }
        else if( client.getConfirmed().equals("POTWIERDZONY")){
            alertError.setHeaderText("Klient jest już potwierdzony!");
            alertError.show();
        }
        else if(client != null){
            dataBase.confirmClient(client.getId());
            alertInfo.setHeaderText("Klient: " + client.getName() + " " + client.getSurname() + " został Potwierdzony.");
            alertInfo.showAndWait();
            clientsTable.setItems(dataBase.getClients());
        }
    }

    /** Wylogowanie z aplikacji
     *
     * @throws IOException
     */
    @FXML
    public void onLogoutButton() throws IOException {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Wylogować?");
        alertConfirm.setHeaderText("Czy na pewno chcesz się wylogować?");

        Optional<ButtonType> result = alertConfirm.showAndWait();
        if(result.get() == ButtonType.OK){
            Stage stage = new Stage();
            AnchorPane loginPane = FXMLLoader.load(getClass().getResource("/com/company/Views/LoginForm.fxml"));
            stage.setScene(new Scene(loginPane, 1200, 750));
            stage.setResizable(false);
            stage.show();
            Stage st2 = (Stage)helloLabel.getScene().getWindow();
            st2.close();

            try{
                dataBase.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /** Aktywne pokazywanie dat kiedy wybrany samochó będzie niedostępny */
    @FXML
    public void onSelectedCar(){
        Integer idCar = carTable.getSelectionModel().getSelectedItem().getId();

        resDateTable.setItems(dataBase.getReservations(null, idCar, "forDates"));
        rentDateTable.setItems(dataBase.getRentals(null, idCar, "forDates"));
        servDateTable.setItems(dataBase.getServices(idCar, "forDates"));
    }

    /** Jeśli usługa wymaga wpisania ceny zostanie pokazane pole do jej wpisania */
    @FXML
    public void onSelectedService(){
        if(servicesCombo.getSelectionModel().getSelectedItem().getPrice()){
            priceField.setDisable(false);
            priceField.setOpacity(1.0);
        }
        else{
            priceField.setDisable(true);
            priceField.setOpacity(0.0);
            priceField.clear();
        }
    }

    /** Utworzenie wpisu w bazie o planowanej usłudze dla wybranego samochodu + walidacja*/
    @FXML
    public void onSendCarForServiceButton(){
        datesError.setText("");
        System.out.println(servicesCombo.getSelectionModel().getSelectedItem());
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Błąd");
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        java.sql.Date startServ = null;
        java.sql.Date endServ = null;

        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null)
            datesError.setText("Nie wybrano pełnego okresu!!!");
        else{
            startServ = java.sql.Date.valueOf(startDatePicker.getValue());
            endServ = java.sql.Date.valueOf(endDatePicker.getValue());

            if(selectedCar == null) {
                alertError.setHeaderText("Nie wybrano Samochodu!!!");
                alertError.showAndWait();
            }

            else if(startServ.after(endServ)) datesError.setText("Data początkowa jest późniejsza od końcowej!!!");
            else if(servicesCombo.getSelectionModel().getSelectedItem() == null){
                alertError.setHeaderText("Nie wybrano usługi!!!");
                alertError.showAndWait();
            }

            else if(servicesCombo.getSelectionModel().getSelectedItem().getPrice() && priceField.getText().equals("")){
                alertError.setHeaderText("Nie wpisano ceny!!!");
                alertError.showAndWait();
            }

            else{
                boolean datesResOk = true, datesRentOk = true, datesServOk = true;

                ObservableList<Reservation> obCarDate = null;
                if((obCarDate = resDateTable.getItems()) != null) {
                    for (Reservation item : obCarDate) {
                        if ((startServ.before(item.getStartDate2()) && endServ.before(item.getStartDate2()))
                                || (startServ.after(item.getEndDate2()) && endServ.after(item.getEndDate2()))) continue;
                        else {
                            datesResOk = false;
                            break;
                        }
                    }
                }

                ObservableList<Rental> obCarDate2 = null;
                if((obCarDate2 = rentDateTable.getItems()) != null) {
                    for (Rental item : obCarDate2) {
                        if ((startServ.before(item.getStartDate2()) && endServ.before(item.getStartDate2()))
                                || (startServ.after(item.getEndDate2()) && endServ.after(item.getEndDate2()))) continue;
                        else {
                            datesRentOk = false;
                            break;
                        }
                    }
                }

                ObservableList<CarServices> obCarDate3 = null;
                if((obCarDate3 = servDateTable.getItems()) != null) {
                    for (CarServices item : obCarDate3) {
                        if ((startServ.before(item.getStartDate()) && endServ.before(item.getStartDate()))
                                || (startServ.after(item.getEndDate()) && endServ.after(item.getEndDate()))) continue;
                        else {
                            datesServOk = false;
                            break;
                        }
                    }
                }

                if(!datesResOk || !datesRentOk || !datesServOk) {
                    alertError.setHeaderText("Błąd - Samochód w tym czasie samochód jest zajęty!");
                    alertError.showAndWait();
                }

                else{
                    dataBase.sendCarForSevice(selectedCar.getId(), servicesCombo.getSelectionModel().getSelectedItem().getIdService(),
                            startServ, endServ, priceField.getText());
                    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                    alertInfo.setTitle("Potwierdzenie");
                    alertInfo.setHeaderText("Samochod " + selectedCar.getId() + " został poprawnie wysłany na usługę.");
                    servDateTable.setItems(dataBase.getServices(selectedCar.getId(),"forDates"));
                    alertInfo.showAndWait();
                }

            }

        }


    }



}
