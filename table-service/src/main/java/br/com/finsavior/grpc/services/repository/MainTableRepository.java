package br.com.finsavior.grpc.services.repository;

import br.com.finsavior.grpc.services.model.entity.MainTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainTableRepository extends JpaRepository<MainTable, Long> {
    public List<MainTable> findAllByUserIdAndBillDate(Long userId, String billDate);
}
