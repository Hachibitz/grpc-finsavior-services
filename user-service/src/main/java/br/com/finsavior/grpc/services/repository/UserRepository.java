package br.com.finsavior.grpc.services.repository;

import br.com.finsavior.grpc.services.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}