package br.com.finsavior.grpc.services.repository;

import br.com.finsavior.grpc.services.model.entity.CreditCardTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardTableRepository extends JpaRepository<CreditCardTable, Long> {
    public List<CreditCardTable> findAllByUserIdAndBillDate(Long userId, String date);
}
