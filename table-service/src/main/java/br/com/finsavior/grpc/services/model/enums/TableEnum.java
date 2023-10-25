package br.com.finsavior.grpc.services.model.enums;

import lombok.Getter;

@Getter
public enum TableEnum {
    MAIN ("main"),
    CREDIT_CARD ("credit-card");

    private final String value;

    TableEnum(String value) {
        this.value = value;
    }
}
