package by.quantumquartet.quanthink.repositories;

import by.quantumquartet.quanthink.models.Role;
import by.quantumquartet.quanthink.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
