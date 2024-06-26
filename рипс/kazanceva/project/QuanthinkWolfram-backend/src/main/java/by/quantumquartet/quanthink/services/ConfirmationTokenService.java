package by.quantumquartet.quanthink.services;

import static by.quantumquartet.quanthink.services.AppLogger.logError;

import by.quantumquartet.quanthink.models.ConfirmationToken;
import by.quantumquartet.quanthink.models.User;
import by.quantumquartet.quanthink.repositories.ConfirmationTokenRepository;
import by.quantumquartet.quanthink.repositories.UserRepository;
import by.quantumquartet.quanthink.rest.responses.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository,
                                    UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public ConfirmationToken createToken(UserDto user) {
        Optional<User> userData = userRepository.findById(user.getId());
        if (userData.isPresent()) {
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    UUID.randomUUID().toString(),
                    new Timestamp(System.currentTimeMillis()),
                    userData.get()
            );
            confirmationToken.setUser(userData.get());
            return confirmationTokenRepository.save(confirmationToken);
        } else {
            logError(ConfirmationTokenService.class, "User with id = " + user.getId() + " not found");
            throw new RuntimeException("User with id = " + user.getId() + " not found");
        }
    }
}
