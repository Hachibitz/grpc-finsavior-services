package br.com.finsavior.grpc.services.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "main_table")
@Data
@NoArgsConstructor
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
}