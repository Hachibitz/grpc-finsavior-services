package br.com.finsavior.grpc.services.repository;

import br.com.finsavior.grpc.services.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET enabled = 0, del_fg = 'S' WHERE id = ?1")
    public void deleteUser(Long id);
}