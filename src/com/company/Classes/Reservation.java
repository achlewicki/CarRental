/**@author Chlewicki Adam */
package com.company.Classes;

import java.util.Date;

/** Klasa do przechowywania Rezerwacji */
public class Reservation extends ReservationRental{

    /** Konstruktor dla Pracownika
     * @param idReservation Identyfikator rezerwacji
     * @param idCar Identyfikator samochodu stowarzyszonego z rezerwacją
     * @param idClient Identyfikator klienta stowarzyszonego z rezerwacją
     * @param name Imię klienta
     * @param surname Nazwisko klienta
     * @param phone Numer telefonu klienta
     * @param mail Mail klienta
     * @param comment Komentarz do rezerwacji
     * @param planningRent Data rozpoczęcia rezerwacji
     * @param planningReturn Data zakończenia rezerwacji
     * @param bail Czy rachunek opłacony
     */
    public Reservation(Integer idReservation, Integer idCar, Integer idClient, String name, String surname, String phone, String mail,
                       String comment, Date planningRent, Date planningReturn, Integer bail) {
        this.id = idReservation;
        this.idCar = idCar;
        this.idClient = idClient;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.comment = comment;
        this.startDate = planningRent;
        this.endDate = planningReturn;

        if(bail == 1) this.bail = "TAK";
        else this.bail = "NIE";
    }

    /** Konstruktor dla Klienta
     *
     * @param idReservation Identyfikator Rezerwacji
     * @param brand Marka samochodu
     * @param model Model samochodu
     * @param planningRent Data rozpoczęcia rezerwacji
     * @param planningReturn Data zakończenia rezerwacji
     * @param bail Czy rachunek jest opłacony
     * @param contact Kontakt do Pracownika opiekującego się rezerwacją
     * @param comment Komentarz do Rezerwacji
     */
    public Reservation(Integer idReservation, String brand, String model, Date planningRent,
                                Date planningReturn, Integer bail, String contact, String comment, Integer status) {
        this.id = idReservation;
        this.brand = brand;
        this.model = model;
        this.startDate = planningRent;
        this.endDate = planningReturn;
        this.contact = contact;
        this.comment = comment;

        if(status == 1) this.status = "Potwierdzone";
        else this.status = "Niepotwierdzone";

        if(bail == 1) this.bail = "TAK";
        else this.bail = "NIE";


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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
