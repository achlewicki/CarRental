/**@author Chlewicki Adam */
package com.company.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Klasa abstrakcyjna dla Rezerwacji i Wypożyczeń */
public abstract class ReservationRental {
    protected Integer id, idCar, idClient;
    protected String name, surname, phone, mail, comment, bail, status;
    protected Date startDate, endDate;
    protected String brand, model, contact;



    public ReservationRental(){};

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(startDate);
        return date;
    }

    public Date getStartDate2() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(endDate);
        return date;
    }

    public Date getEndDate2() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getIdCar() {
            return idCar;
        }

    public void setIdCar(Integer idCar) {
            this.idCar = idCar;
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

    public String getComment() {
            return comment;
        }

    public void setComment(String comment) {
            this.comment = comment;
        }

    public String getBail() {
            return bail;
        }

        public void setBail(String bail) {
            this.bail = bail;
        }
}

