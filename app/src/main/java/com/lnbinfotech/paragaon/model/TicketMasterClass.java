package com.lnbinfotech.paragaon.model;

//Created by lnb on 8/12/2017.

import java.io.Serializable;

public class TicketMasterClass implements Serializable{

    public String finyr, ticketNo,Particular,Subject,ImagePAth,Status,CrBy,CrDate,CrTime, type, assignTO;
    public int auto, id, clientAuto;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getParticular() {
        return Particular;
    }

    public void setParticular(String particular) {
        Particular = particular;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getImagePAth() {
        return ImagePAth;
    }

    public void setImagePAth(String imagePAth) {
        ImagePAth = imagePAth;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCrBy() {
        return CrBy;
    }

    public void setCrBy(String crBy) {
        CrBy = crBy;
    }

    public String getCrDate() {
        return CrDate;
    }

    public void setCrDate(String crDate) {
        CrDate = crDate;
    }

    public String getCrTime() {
        return CrTime;
    }

    public void setCrTime(String crTime) {
        CrTime = crTime;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public String getFinyr() {
        return finyr;
    }

    public void setFinyr(String finyr) {
        this.finyr = finyr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClientAuto() {
        return clientAuto;
    }

    public void setClientAuto(int clientAuto) {
        this.clientAuto = clientAuto;
    }

    public String getAssignTO() {
        return assignTO;
    }

    public void setAssignTO(String assignTO) {
        this.assignTO = assignTO;
    }
}
