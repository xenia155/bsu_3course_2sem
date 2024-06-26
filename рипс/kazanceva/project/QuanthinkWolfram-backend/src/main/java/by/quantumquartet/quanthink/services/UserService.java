package by.quantumquartet.quanthink.services;

import static by.quantumquartet.quanthink.services.AppLogger.logError;

import by.quantumquartet.quanthink.models.ERole;
import by.quantumquartet.quanthink.models.Role;
import by.quantumquartet.quanthink.models.User;
import by.quantumquartet.quanthink.repositories.RoleRepository;
import by.quantumquartet.quanthink.repositories.UserRepository;
import by.quantumquartet.quanthink.rest.requests.users.RegisterRequest;
import by.quantumquartet.quanthink.rest.requests.users.UpdateUserRequest;
import by.quantumquartet.quanthink.rest.responses.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final Set<UserDto> onlineUsers = ConcurrentHashMap.newKeySet();

    @Autowired
    public UserService(PasswordEncoder encoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = convertToDto(user);
            usersDto.add(userDto);
        }

        return usersDto;
    }

    public Optional<UserDto> getUserById(long id) {
        Optional<User> userData = userRepository.findById(id);
        return userData.map(this::convertToDto);
    }

    public Optional<UserDto> getUserByEmail(String email) {
        Optional<User> userData = userRepository.findByEmail(email);
        return userData.map(this::convertToDto);
    }

    public boolean isEmailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto registerUser(RegisterRequest registerRequest) {
        Set<Role> roles = new HashSet<>();
        Optional<Role> roleData = roleRepository.findByName(ERole.ROLE_USER);
        if (roleData.isPresent()) {
            roles.add(roleData.get());
        } else {
            logError(UserService.class, "Role " + ERole.ROLE_USER + " not found");
            throw new RuntimeException("Role " + ERole.ROLE_USER + " not found");
        }

        User newUser = new User(
                registerRequest.getEmail(),
                registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()),
                false,
                roles
        );

        return convertToDto(userRepository.save(newUser));
    }

    public boolean confirmAccount(long id) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User user = userData.get();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        } else {
            logError(UserService.class, "User with id = " + id + " not found");
            throw new RuntimeException("User with id = " + id + " not found");
        }
    }

    public UserDto updateUser(long id, UpdateUserRequest updateUserRequest) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User existingUser = userData.get();
            String newEmail = updateUserRequest.getEmail();
            String newUsername = updateUserRequest.getUsername();
            String newPassword = updateUserRequest.getPassword();
            if (newEmail != null) {
                existingUser.setEmail(newEmail);
            }
            if (newUsername != null) {
                existingUser.setUsername(newUsername);
            }
            if (newPassword != null) {
                existingUser.setPassword(encoder.encode(newPassword));
            }
            return convertToDto(userRepository.save(existingUser));
        } else {
            logError(UserService.class, "User with id = " + id + " not found");
            throw new RuntimeException("User with id = " + id + " not found");
        }
    }

    public UserDto assignAdminRole(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            Set<Role> roles = existingUser.getRoles();

            Optional<Role> roleData = roleRepository.findByName(ERole.ROLE_ADMIN);
            if (roleData.isPresent()) {
                roles.add(roleData.get());
            } else {
                throw new RuntimeException("Role " + ERole.ROLE_ADMIN + " not found");
            }
            existingUser.setRoles(roles);
            return convertToDto(userRepository.save(existingUser));
        } else {
            logError(UserService.class, "User with id = " + id + " not found");
            throw new RuntimeException("User with id = " + id + " not found");
        }
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public void setUserOnline(UserDto userDto) {
        onlineUsers.add(userDto);
    }

    public void setUserOffline(UserDto userDto) {
        onlineUsers.remove(userDto);
    }

    public boolean isUserOnline(UserDto userDto) {
        return onlineUsers.contains(userDto);
    }

    public Set<UserDto> getOnlineUsers() {
        return new HashSet<>(onlineUsers);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());

        Set<ERole> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }
        userDto.setRoles(roles);

        return userDto;
    }
}
