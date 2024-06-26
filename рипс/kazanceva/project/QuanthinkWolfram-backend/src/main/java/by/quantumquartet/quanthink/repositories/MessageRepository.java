package by.quantumquartet.quanthink.repositories;

import by.quantumquartet.quanthink.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
