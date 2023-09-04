package br.com.finsavior.grpc.services.model.enums;

public enum UserRoleEnum {
    ROLE_USER(1L),
    ROLE_ADMIN(2L);

    public final Long id;

    UserRoleEnum(Long id) {
        this.id = id;
    }
}