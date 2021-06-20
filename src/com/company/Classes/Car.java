/**@author Chlewicki Adam */
package com.company.Classes;

import java.sql.Date;
import java.time.LocalDate;

/** Klasa używana do operowania na samochodach z bazy danych */
public class Car {
    private Integer id,year,seats;
    private Double price;
    private String brand, model, currency;

    private LocalDate endOfInspection, endOfInsurance;
    private String plateNumber, comment;

    /**Konstruktor do wczytania obiektu do tabeli samochody u Klienta
     *
     * @param id Identyfikator samochodu z bazy
     * @param brand Marka samochodu z bazy
     * @param model Model samochodu z bazy
     * @param year Rocznik samochodu z bazy
     * @param seats Liczba miejsc w samochodzie z bazy
     * @param price Cena wypożyczenia samochodu
     * @param currency Waluta ceny
     */
    public Car(Integer id, String brand, String model, Integer year, Integer seats, Double price, String currency) {
        this.id = id;
        this.year = year;
        this.seats = seats;
        this.price = price;
        this.brand = brand;
        this.model = model;
        this.currency = currency;
    }

    /**Konstruktor do wczytania obiektu do tabeli samochody u Pracownika
     *
     * @param id Identyfikator samochodu z bazy
     * @param brand Marka samochodu z bazy
     * @param model Model samochodu z bazy
     * @param plateNumber Numer rejestracyjny samochodu
     * @param endOfInspection Data końca przeglądu samochodu
     * @param endOfInsurance Data końca ubezpieczenia samochodu
     * @param comment Komentarz do samochodu
     */
    public Car(Integer id, String brand, String model, String plateNumber, Date endOfInspection, Date endOfInsurance,
               String comment) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.endOfInspection = new java.sql.Date(endOfInspection.getTime()).toLocalDate();;
        this.endOfInsurance = new java.sql.Date(endOfInsurance.getTime()).toLocalDate();
        this.comment = comment;
    }

    public LocalDate getEndOfInspection() {
        return endOfInspection;
    }

    public void setEndOfInspection(LocalDate endOfInspection) {
        this.endOfInspection = endOfInspection;
    }

    public LocalDate getEndOfInsurance() {
        return endOfInsurance;
    }

    public void setEndOfInsurance(LocalDate endOfInsurance) {
        this.endOfInsurance = endOfInsurance;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return brand + " " + model + " " + year + " " + seats + "-osobowy";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
