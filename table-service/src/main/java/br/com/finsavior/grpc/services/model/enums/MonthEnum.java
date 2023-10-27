package br.com.finsavior.grpc.services.model.enums;

import lombok.Getter;

@Getter
public enum MonthEnum {
    JAN ("Jan", 1),
    FEB ("Feb", 2),
    MAR ("Mar", 3),
    APR ("Apr", 4),
    MAY ("May", 5),
    JUN ("Jun", 6),
    JUL ("Jul", 7),
    AUG ("Aug", 8),
    SEP ("Sep", 9),
    OCT ("Oct", 10),
    NOV ("Nov", 11),
    DEC ("Dec", 12);

    private final String value;
    private final int id;

    MonthEnum(String value, int id) {
        this.value = value;
        this.id = id;
    }
}
