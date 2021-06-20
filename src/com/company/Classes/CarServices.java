/**@author Chlewicki Adam */
package com.company.Classes;

import java.sql.Date;

/** Klasa do przechowywania informacji o usłudze dla danego samochodu, w tym dat. Klasa dla Pracownika */
public class CarServices {
    Integer idLog;
    Date startDate, endDate;
    String serviceDes;

    Integer idService;
    String service;
    Boolean price;

    /** Konstruktor do wczytania pojedyńczego zapisu z usług dla konretnego samochodu
     *
     * @param idLog Numer usługi w bazie
     * @param startDate Początek usługi
     * @param endDate Koniec usługi
     * @param serviceDes Opis usługi
     */
    public CarServices(Integer idLog, Date startDate, Date endDate, String serviceDes) {
        this.idLog = idLog;
        this.startDate = startDate;
        this.endDate = endDate;
        this.serviceDes = serviceDes;
    }

    /** Konstruktor do utworzenia obiektów o dostępnych usługach
     *
     * @param idService Numer usługi
     * @param service Nazwa usługi
     * @param price Cena usługi
     */
    public CarServices(Integer idService, String service, String price) {
        super();
        this.idService = idService;
        this.service = service;
        if(price != null) this.price = true;
        else this.price = false;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
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

    public String getServiceDes() {
        return serviceDes;
    }

    public void setServiceDes(String serviceDes) {
        this.serviceDes = serviceDes;
    }



    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Boolean getPrice() {
        return price;
    }

    public void setPrice(Boolean price) {
        this.price = price;
    }
}
