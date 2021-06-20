/**@author Chlewicki Adam */
package com.company.Classes;

/** Klasa do przechowywania informacji o Kliencie */
public class Person {
    private Integer id;
    private String name,surname, phone, mail, login, idNumber, confirmed;

    /** Konstruktor
     *
     * @param id Identyfikator osoby w bazie
     * @param name Imię osoby w bazie
     * @param surname Nazwisko osoby w bazie
     * @param login Login osoby w bazie
     * @param phone Numer telefonu osoby
     * @param mail Mail osoby
     * @param idNumber Numer dowowdu osobistego osoby
     * @param confirmed Informacja czy dana osoba jest już potwierdzona w bazie
     */
    public Person(Integer id, String name, String surname, String login, String phone, String mail, String idNumber,
                  Integer confirmed) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.login = login;
        this.idNumber = idNumber;
        if(confirmed == 1) this.confirmed = "POTWIERDZONY";
        else if (confirmed == 0) this.confirmed = "NIEPOTWIERDZONY";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }


}
