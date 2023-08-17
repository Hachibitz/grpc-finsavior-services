package br.com.finsavior.grpc.services.repository;

import br.com.finsavior.grpc.services.entity.MainTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainTableRepository extends JpaRepository<MainTable, Long> {

}