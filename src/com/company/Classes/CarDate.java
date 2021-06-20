/**@author Chlewicki Adam */
package com.company.Classes;

import java.sql.Date;

/** Klasa używana do przechowywania dat kiedy dany samochód ma być użyty. Klasa dla klienta*/
public class CarDate {
    private Integer idCar;
    private Date startDate, endDate;

    /**Konstruktor
     *
     * @param idCar Identyfikator samochodu z bazy
     * @param startDate Data początkowa 1 użycia samochodu
     * @param endDate Data końcowa 1 użycia samochodu
     */
    public CarDate(Integer idCar, Date startDate, Date endDate) {
        this.idCar = idCar;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getIdCar() {
        return idCar;
    }

    public void setIdCar(Integer idCar) {
        this.idCar = idCar;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
