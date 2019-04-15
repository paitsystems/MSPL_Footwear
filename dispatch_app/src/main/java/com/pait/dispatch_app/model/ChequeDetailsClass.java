package com.pait.dispatch_app.model;// Created by anup on 9/21/2017.

public class ChequeDetailsClass {
    private String chq_det_bank, chq_det_branch, chq_det_date, chq_det_number, chq_det_amt, chq_det_ref,
                    chq_det_image, no_of_chq, custBankName, custBankBranch, CBL;
    private int srNo, cartons;

    public String getCBL() {
        return CBL;
    }

    public void setCBL(String CBL) {
        this.CBL = CBL;
    }

    public int getCartons() {
        return cartons;
    }

    public void setCartons(int cartons) {
        this.cartons = cartons;
    }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public String getChq_det_bank() {
        return chq_det_bank;
    }

    public void setChq_det_bank(String chq_det_bank) {
        this.chq_det_bank = chq_det_bank;
    }

    public String getChq_det_branch() {
        return chq_det_branch;
    }

    public void setChq_det_branch(String chq_det_branch) {
        this.chq_det_branch = chq_det_branch;
    }

    public String getChq_det_date() {
        return chq_det_date;
    }

    public void setChq_det_date(String chq_det_date) {
        this.chq_det_date = chq_det_date;
    }

    public String getChq_det_number() {
        return chq_det_number;
    }

    public void setChq_det_number(String chq_det_number) {
        this.chq_det_number = chq_det_number;
    }

    public String getChq_det_amt() {
        return chq_det_amt;
    }

    public void setChq_det_amt(String chq_det_amt) {
        this.chq_det_amt = chq_det_amt;
    }

    public String getChq_det_ref() {
        return chq_det_ref;
    }

    public void setChq_det_ref(String chq_det_ref) {
        this.chq_det_ref = chq_det_ref;
    }

    public String getChq_det_image() {
        return chq_det_image;
    }

    public void setChq_det_image(String chq_det_image) {
        this.chq_det_image = chq_det_image;
    }

    public String getNo_of_chq() {
        return no_of_chq;
    }

    public void setNo_of_chq(String no_of_chq) {
        this.no_of_chq = no_of_chq;
    }

    public String getCustBankName() {
        return custBankName;
    }

    public void setCustBankName(String custBankName) {
        this.custBankName = custBankName;
    }

    public String getCustBankBranch() {
        return custBankBranch;
    }

    public void setCustBankBranch(String custBankBranch) {
        this.custBankBranch = custBankBranch;
    }
}
