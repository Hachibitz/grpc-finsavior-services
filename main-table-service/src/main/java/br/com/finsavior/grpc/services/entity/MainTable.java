package br.com.finsavior.grpc.services.entity;

import javax.persistence.*;

@Entity
@Table(name = "main_table")
public class MainTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "bill_type")
    private String billType;

    @Column(name = "bill_date")
    private String billDate;

    @Column(name = "bill_name")
    private String billName;

    @Column(name = "bill_value")
    private double billValue;

    @Column(name = "bill_description")
    private String billDescription;

    public MainTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public double getBillValue() {
        return billValue;
    }

    public void setBillValue(double billValue) {
        this.billValue = billValue;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
    }
}