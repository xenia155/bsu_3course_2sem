package by.quantumquartet.quanthink.repositories;

import by.quantumquartet.quanthink.models.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    @Query("select c from Calculation c where c.user.id = :userId")
    List<Calculation> findCalculationsByUserId(long userId);

    void deleteCalculationsByUserId(long userId);
}
