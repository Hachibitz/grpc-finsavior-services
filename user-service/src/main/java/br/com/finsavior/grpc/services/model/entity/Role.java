package br.com.finsavior.grpc.services.model.entity;

import br.com.finsavior.grpc.services.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum name;
}
