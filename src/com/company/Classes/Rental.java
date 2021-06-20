/**@author Chlewicki Adam */
package com.company.Classes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/** Klasa do przechowywania wypożyczeń */
public class Rental extends ReservationRental{

    /** Konstruktor
     *
     * @param idRent Identyfikator wypozyczenia
     * @param idCar Identyfikator samochodu stowarzyszonego z wypożyczeniem
     * @param idClient Identyfikator klienta stowarzyszonego z wypożyczeniem
     * @param name Imię klienta
     * @param surname Nazwisko klienta
     * @param phone Numer telefonu klienta
     * @param mail Mail klienta
     * @param comment Komentarz do wypożyczenia
     * @param dateRent Data rozpoczęcia wypożyczenia
     * @param dateReturn Data zakończenia wypożyczenia
     */
    public Rental(Integer idRent, Integer idCar, Integer idClient, String name, String surname, String phone, String mail,
                  String comment, Date dateRent, Date dateReturn){
        this.id = idRent;
        this.idCar = idCar;
        this.idClient = idClient;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.comment = comment;
        this.startDate = dateRent;
        this.endDate = dateReturn;

        LocalDate localDate= LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(today.after(dateReturn)) this.status = "ZAKONCZONE";
        else if(today.after(dateRent) && (today.before(dateReturn) || today.equals(dateReturn))) this.status = "W TRAKCIE";
        else if(today.before(dateRent)) this.status = "OCZEKUJĄCE";
    }

}
