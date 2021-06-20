/**@author Chlewicki Adam*/
package com.company.Controllers;

import com.company.Classes.*;
import com.company.ClassesForDataBase.DataBaseForClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Kontroler dla Klienta */
public class ClientController {

    /** Tabela dla samochodów */
    @FXML
    private TableView<Car> carTable;
    @FXML
    private TableColumn<Car, Integer> idCarCol;
    @FXML
    private TableColumn<Car, Integer> yearCol;
    @FXML
    private TableColumn<Car, Integer> seatsCol;
    @FXML
    private TableColumn<Car, Integer> priceCol;
    @FXML
    private TableColumn<Car, String> brandCol;
    @FXML
    private TableColumn<Car, String> modelCol;
    @FXML
    private TableColumn<Car, String> currencyCol;

    /** Filtry */
    @FXML
    private ChoiceBox<String> currencyFilter;
    @FXML
    private Slider fromSlider;
    @FXML
    private Slider toSlider;
    @FXML
    private Button searchButton;
    @FXML
    private TextField brandFilter;
    @FXML
    private TextField modelFilter;
    @FXML
    private ComboBox<Integer> yearFilter = new ComboBox<>();
    @FXML
    private ComboBox<Integer> seatsFilter = new ComboBox<>();

    /** Label dla wybranego samochodu */
    @FXML
    private Label carLabel;

    /** Tabela dla rezerwacji */
    @FXML
    private TableView<Reservation> ordersTable;
    @FXML
    private TableColumn<Reservation, Integer> idResCol;
    @FXML
    private TableColumn<Reservation, String> brandResCol;
    @FXML
    private TableColumn<Reservation, String> modelResCol;
    @FXML
    private TableColumn<Reservation, String> startDateCol;
    @FXML
    private TableColumn<Reservation, String> endDateCol;
    @FXML
    private TableColumn<Reservation, String> bailCol;
    @FXML
    private TableColumn<Reservation, String> contactCol;
    @FXML
    private TableColumn<Reservation, String> commentCol;
    @FXML
    private TableColumn<Reservation, String> statusCol;

    /** Pola do edycji profilu */
    @FXML
    private TextField name, surname, mail, idNumber, phone, currentLogin, login, currentPassword, newPassword, confirmPassword;
    @FXML
    private Label nameError, surnameError, phoneError, mailError, idNumberError, loginError, currentPasswordError,
            newPasswordError, confirmPasswordError, currentLoginError;

    /** Pola do walidacji pól edycji profilu */
    @FXML
    private Label errorStartDate, errorEndDate, errorRes, addressError;

    /** Pola do dokonania rezerwacji */
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private TextArea addressText;
    @FXML
    private CheckBox addAddress;

    /** Tabela dla dat kiedy samochód jest użyty */
    @FXML
    private TableView<CarDate> carDatesTable;
    @FXML
    private TableColumn<CarDate, LocalDate> startCarDatesCol;
    @FXML
    private TableColumn<CarDate, LocalDate> endCarDatesCol;

    ObservableList<Car> carList = FXCollections.observableArrayList();
    DataBaseForClient dataBase = new DataBaseForClient();
    private Person client = null;
    public static Integer idClient;

    @FXML
    public void initialize() {
        /** Ustawianie sliderów */
        fromSlider.setMin(100);
        fromSlider.setMax(1000);
        fromSlider.setValue(100);
        fromSlider.setShowTickLabels(true);
        toSlider.setMin(100);
        toSlider.setMax(1000);
        toSlider.setValue(1000);
        toSlider.setShowTickLabels(true);

        /** Ustawienie filtru dla rocznika cz.I */
        currencyFilter.getItems().add(0,"Dowolna");
        currencyFilter.getSelectionModel().selectFirst();

        /** Uzupełnienie tabeli z samochodami */
        idCarCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("seats"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));

        carList = dataBase.selectTableCars();
        carTable.setItems(carList);
        carLabel.setText("");

        /** Uzupełnienie tabeli z zamówieniami klienta */
        idResCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandResCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelResCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        bailCol.setCellValueFactory(new PropertyValueFactory<>("bail"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        ordersTable.setItems(dataBase.getReservations(idClient));

        /** Pobranie podstawowych informacji o kliencie */
        client = dataBase.getBasicInfo(idClient);
        updateClient();

        /** Dalsze ustawienie filtru dla rocznika cz.II */
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for(int i = 1954; i <= 2013; i++) years.add(i);
        yearFilter.setItems(years);
        yearFilter.setPromptText("Wybierz Rocznik");

        /** Ustawienie filtru dla miejsc w samochodzie */
        ObservableList<Integer> seats = FXCollections.observableArrayList();
        seats.add(0,1);
        seats.add(1,2);
        seats.add(2,3);
        seats.add(3,4);
        seats.add(4,5);
        seatsFilter.setItems(seats);
        seatsFilter.setPromptText("Wybierz Ilość Miejsc");
    }

    /** Aktualizacja informacji o kliencie */
    @FXML
    public void updateClient(){
        name.setText(client.getName());
        surname.setText(client.getSurname());
        phone.setText(client.getPhone());
        mail.setText(client.getMail());
        idNumber.setText(client.getIdNumber());
    }

    /** Czuwanie nad poprawnością ustawienia sliderów względem siebie cz. I*/
    @FXML
    public void onFromSliderAction(){
        if(fromSlider.getValue() > toSlider.getValue()) {
            toSlider.setValue(fromSlider.getValue());
        }
    }

    /** Czuwanie nad poprawnością ustawienia sliderów względem siebie cz. II*/
    @FXML
    public void onToSliderAction(){
        if(fromSlider.getValue() > toSlider.getValue()) fromSlider.adjustValue(toSlider.getValue());
    }

    /** Edycja danych klienta */
    @FXML
    public void onEditClientButton(){
        nameError.setText("");
        surnameError.setText("");
        phoneError.setText("");
        mailError.setText("");
        idNumberError.setText("");
        loginError.setText("");
        currentPasswordError.setText("");
        newPasswordError.setText("");
        confirmPasswordError.setText("");
        currentLoginError.setText("");


        boolean changePersonalData = false;

        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("UWAGA!!!");
        alertConfirm.setHeaderText("Czy napewno chcesz Edytować swoje dane osobowe i/lub login i/lub hasło? \n (W przypadku" +
                " zmiany danych osobowych wymagane będzie potwierdzenie Ciebie\n przez naszego Pracownika i nie" +
                " będziesz mógł zalogować się przez \nkilka godzin)");

        Optional<ButtonType> result = alertConfirm.showAndWait();
        if(result.get() == ButtonType.OK) {
            if (dataBase.checkPassword(currentPassword.getText(), client) && dataBase.checkLogin(currentLogin.getText(), client)) {
                if (!name.getText().equals(client.getName())) {
                    if (name.getText().equals(""))
                        nameError.setText("Nie podano imienia!!!");

                    else if (name.getText().length() > 20)
                        nameError.setText("Za długie imię!!! (Max 20 znaków)");

                    else {
                        client.setName(name.getText());
                        dataBase.changeName(client);
                        changePersonalData = true;
                    }
                }

                if (!surname.getText().equals(client.getSurname())) {
                    if (surname.getText().equals(""))
                        surnameError.setText("Nie podano nazwiska!!!");

                    else if (surname.getText().length() > 30)
                        surnameError.setText("Za długie nazwisko!!! (Max 30 znaków)");

                    else {
                        client.setSurname(surname.getText());
                        dataBase.changeSurname(client);
                        changePersonalData = true;
                    }
                }

                if (!phone.getText().equals(client.getPhone())) {
                    if (phone.getText().equals(""))
                        phoneError.setText("Nie podano telefonu!!!");

                    else {
                        Pattern pattern = Pattern.compile("\\+\\d\\d\\s\\d\\d\\d\\-\\d\\d\\d\\-\\d\\d\\d");
                        Matcher matcher = pattern.matcher(phone.getText());
                        if (!matcher.matches())
                            phoneError.setText("Błędny format telefonu!!!\n(Poprawny np. '+48 123-456-789')");

                        else {
                            client.setPhone(phone.getText());
                            dataBase.changePhone(client);
                            changePersonalData = true;
                        }
                    }
                }

                if (!mail.getText().equals(client.getMail())) {
                    if (mail.getText().equals(""))
                        mailError.setText("Nie podano maila!!!");

                    else {
                        if (mail.getText().length() <= 50) {
                            Pattern pattern = Pattern.compile(".+@.+\\..+");
                            Matcher matcher = pattern.matcher(mail.getText());
                            if (!matcher.matches())
                                mailError.setText("Błędny format maila!!!\n(Poprawny np. 'janKowalski@polska.pl')");
                            else {
                                client.setMail(mail.getText());
                                dataBase.changeMail(client);
                                changePersonalData = true;
                            }
                        } else mailError.setText("Za długi mail!!! (Max 50 znaków)");
                    }
                }

                if (!idNumber.getText().equals(client.getIdNumber())) {
                    if (idNumber.getText().equals(""))
                        idNumberError.setText("Nie podano numeru dowodu osbistego!!!");

                    else if (idNumber.getText().length() > 10)
                        idNumberError.setText("Za dlugi numer!!! (Max 10 znaków)");

                    else {
                        client.setIdNumber(idNumber.getText());
                        dataBase.changeIdNumber(client);
                        changePersonalData = true;
                    }
                }

                if (changePersonalData) client.setConfirmed("NIE");

                if (!login.getText().equals(client.getLogin()) && !login.getText().equals("")) {

                    if (login.getText().length() > 31)
                        loginError.setText("Za dlugi login!!! (Max 31 znaków)");

                    else {
                        String oldLogin = client.getLogin();
                        client.setLogin(login.getText());
                        if (!dataBase.changeLogin(client)) {
                            loginError.setText("Login jest już zajęty!");
                            client.setLogin(oldLogin);
                        }
                    }
                }

                if (!newPassword.getText().equals("")) {
                    if (newPassword.getText().length() < 8 || newPassword.getText().length() > 50)
                        newPasswordError.setText("Niepoprawna długość hasła!!!\n(Min 8 znaków, Max 50 znaków)");

                    else {
                        String passwordLocal = newPassword.getText();
                        boolean constrain = false;

                        Pattern patternSpecChar = Pattern.compile("[a-zA-z0-9]*");
                        Matcher matcher = patternSpecChar.matcher(passwordLocal);
                        if (!matcher.matches()) {
                            constrain = true;
                        }

                        Pattern patternAZ = Pattern.compile("[A-Z]+");
                        Matcher matcher2 = patternAZ.matcher(passwordLocal);
                        if (!matcher2.find()) constrain = false;

                        Pattern pattern09 = Pattern.compile("[0-9]+");
                        Matcher matcher3 = pattern09.matcher(passwordLocal);
                        if (!matcher3.find()) constrain = false;

                        if (constrain) {
                            Pattern patternPassword = Pattern.compile(passwordLocal);
                            Matcher matcherFinal = patternPassword.matcher(confirmPassword.getText());
                            if (!matcherFinal.matches()) {
                                confirmPasswordError.setText("Hasła są różne!!!");
                            } else dataBase.changePassword(newPassword.getText(), client);
                        } else
                            newPasswordError.setText("Hasło nie zawiera znaku\nspecjalnego, wielkiej\nlitery lub cyfry");
                    }
                }
            } else {
                currentLoginError.setText("Błędne hasło lub login!");
                currentPasswordError.setText("Błędne hasło lub login!!");
            }
        }

        currentLogin.setText("");
        login.setText("");
        currentPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

    /** Aktywne wyświetlanie dat kiedy wybrany samochód będzie niedostępny */
    @FXML
    public void onSelectedRow(){
        Car selectedCar = null;
        if(carTable.getSelectionModel().getSelectedItem() != null){
            selectedCar = carTable.getSelectionModel().getSelectedItem();
        }

        carLabel.setText(selectedCar.toString());


        DataBaseForClient db = new DataBaseForClient();
        startCarDatesCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCarDatesCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        carDatesTable.setItems(db.whenCarIsNotAvailable(selectedCar.getId()));

    }

    /** Czyszczenie filtrów */
    @FXML
    public void onClearFiltersButton(){
        brandFilter.clear();
        modelFilter.clear();
        yearFilter.setValue(null);
        seatsFilter.setValue(null);
        fromSlider.setValue(100.0);
        toSlider.setValue(1000.0);
        currencyFilter.getSelectionModel().selectFirst();
        carTable.setItems(carList);
    }

    /** Dodanie adresu do rezerwacji */
    @FXML
    public void onAddAddressAction(){
        if(addAddress.isSelected()){
            addressText.setDisable(false);
            addressText.setOpacity(1.0);
        }
        else {
            addressText.setDisable(true);
            addressText.setOpacity(0.0);
            addressText.setText("");
        }
    }

    /** Stworzenie nowej rezerwacji + walidacja danych*/
    @FXML
    public void onSetNewReservationButton(){
        errorStartDate.setText("");
        errorEndDate.setText("");
        errorRes.setText("");
        addressError.setText("");
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();

        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        boolean datesOk = true, carOk = true;

        String address = null;

        if(selectedCar == null) {
            carOk = false;
            errorRes.setText("Nie wybrano Samochodu!");
        }


        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            errorEndDate.setText("Nie wybrano pełnego okresu!");
            datesOk = false;
        }
        else if(startDatePicker.getValue().isAfter(endDatePicker.getValue())){
            datesOk = false;
            errorEndDate.setText("Data końcowa nie może być\nwcześniejsza niż data początkowa!");
        }

        else if((startDate = java.sql.Date.valueOf(startDatePicker.getValue())).before(currentDate)){
            datesOk = false;
            errorStartDate.setText("Data początkowa nie może być\nwcześniejsza niż dzisiejsza!");
        }

        else if(carOk && datesOk){
            endDate = java.sql.Date.valueOf(endDatePicker.getValue());

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Bład");

            ObservableList<CarDate> obCarDate = dataBase.whenCarIsNotAvailable(selectedCar.getId());
            if(!obCarDate.isEmpty()){
                for(CarDate item : obCarDate){
                    if((startDate.before(item.getStartDate()) && endDate.before(item.getEndDate()))
                            || (startDate.after(item.getEndDate()) && endDate.after(item.getEndDate()))) continue;
                    else{
                        datesOk = false;
                        break;
                    }
                }
            }

            if(!datesOk){
                alertError.setHeaderText("Błąd - Samochód w tym czasie samochód jest zajęty!");
                alertError.showAndWait();
            }
        }

        if(addAddress.isSelected() && addressText.getText().equals("")){
            addressError.setText("Nie wpisano adresu dostawy!");
            carOk = false;
        }
        else if(addAddress.isSelected()) address = "Dostawa: " + addressText.getText();
        else address = addressText.getText();

        if(datesOk && carOk){
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("POTWIERDŹ");
            Double price = selectedCar.getPrice() + selectedCar.getPrice()
                    * TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
            alertConfirm.setHeaderText("Czy na pewno chcesz dokonać rezerwacji samochodu: \n" + selectedCar.getBrand() +
                    " " + selectedCar.getModel() + "\n\nCałkowity koszt wypożyczenia:\n" + price + selectedCar.getCurrency() + "?");
            Optional<ButtonType> result = alertConfirm.showAndWait();

            if(result.get() == ButtonType.OK){
                dataBase.setNewReservation(idClient, selectedCar.getId(), startDate, endDate, address);
                ordersTable.setItems(dataBase.getReservations(idClient));
                carDatesTable.setItems(dataBase.whenCarIsNotAvailable(selectedCar.getId()));
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Potwierdzenie");
                alertInfo.setHeaderText("Rezerwacja samochodu nr: " + selectedCar.getId() + " została przyjęta.\n" +
                        "Pamiętaj, aby zapłacić całość należności\nnajpóźniej na 2 dni przed planowanym wypożyczeniem." +
                        "\nW przeciwnym razie rezerwacja zostanie usunięta!!!");
                alertInfo.show();
            }
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
            Stage st2 = (Stage)name.getScene().getWindow();
            st2.close();
            try {
                dataBase.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    /** Filtrowanie listy samochodów */
    @FXML
    public void onSearchButtonAction(){
        FilteredList<Car> filteredData = new FilteredList<>(carList, p -> true);

        String newBrand = brandFilter.getText();
        String newModel = modelFilter.getText();

        Integer newYear = yearFilter.getValue();
        Integer newSeats = seatsFilter.getValue();


        System.out.println(newYear);

        Double newPriceMin = fromSlider.getValue();
        Double newPriceMax = toSlider.getValue();
        String newCurrency = currencyFilter.getValue();

        String lowerCaseFilter = newBrand.toLowerCase();
        String lowerCaseFilter2 = newModel.toLowerCase();

            filteredData.setPredicate(Car -> {

                String checkingBrand = Car.getBrand().toLowerCase();
                String checkingModel = Car.getModel().toLowerCase();
                Double checkingPrice = Car.getPrice();
                String checkingCurrency = Car.getCurrency();

                Integer checkingYear = Car.getYear();
                Integer checkingSeats = Car.getSeats();

                if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna") ) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna") ) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna") ) return true;

                else if((newBrand.isEmpty() || newBrand == null) && checkingModel.contains(lowerCaseFilter2)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna") ) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna") ) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && newYear == null && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if((newBrand.isEmpty() || newBrand == null) && (newModel.isEmpty() || newModel == null)
                        && checkingYear.equals(newYear) && newSeats == null
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && newCurrency.equals("Dowolna")) return true;

                else if(checkingBrand.contains(lowerCaseFilter) && checkingModel.contains(lowerCaseFilter2)
                        && checkingYear.equals(newYear) && checkingSeats.equals(newSeats)
                        && newPriceMin <= checkingPrice && checkingPrice <= newPriceMax
                        && checkingCurrency.contains(newCurrency)) return true;

                else return false;
            });

        carTable.setItems(filteredData);
    }
}
