package by.quantumquartet.quanthink.controllers;

import static by.quantumquartet.quanthink.services.AppLogger.*;

import by.quantumquartet.quanthink.models.ConfirmationToken;
import by.quantumquartet.quanthink.rest.requests.users.LoginRequest;
import by.quantumquartet.quanthink.rest.requests.users.RegisterRequest;
import by.quantumquartet.quanthink.rest.responses.SuccessResponse;
import by.quantumquartet.quanthink.rest.responses.ErrorResponse;
import by.quantumquartet.quanthink.rest.responses.jwt.JwtResponse;
import by.quantumquartet.quanthink.rest.responses.users.UserDto;
import by.quantumquartet.quanthink.security.JwtUtils;
import by.quantumquartet.quanthink.models.UserDetailsImpl;
import by.quantumquartet.quanthink.services.ConfirmationTokenService;
import by.quantumquartet.quanthink.services.EmailService;
import by.quantumquartet.quanthink.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    ConfirmationTokenService confirmationTokenService,
                                    EmailService emailService,
                                    UserService userService,
                                    JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userService.isEmailAlreadyExists(registerRequest.getEmail())) {
                logWarn(AuthenticationController.class,
                        "Email " + registerRequest.getEmail() + " already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Email already exists"));
            }

            long newUserId = userService.registerUser(registerRequest).getId();
            logInfo(AuthenticationController.class,
                    "User registered with id = " + newUserId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse<>("User registered successfully", newUserId));
        } catch (Exception e) {
            logError(AuthenticationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
//        if (userService.isEmailAlreadyExists(registerRequest.getEmail())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ErrorResponse("Email already exists"));
//        }
//        try {
//            UserDto newUser = userService.registerUser(registerRequest);
//
//            ConfirmationToken confirmationToken = confirmationTokenService.createToken(newUser);
//            emailService.sendSimpleEmail(newUser.getEmail(), confirmationToken.getToken());
//
//            long newUserId = newUser.getId();
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new SuccessResponse<>("User registered successfully", newUserId));
//        } catch (Exception e) {
//            logError(AuthenticationController.class, e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse("Internal server error"));
//        }
//    }

    @RequestMapping(value = "/confirmAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String token) {
        Optional<ConfirmationToken> confirmationTokenData = confirmationTokenService.findByToken(token);
        if (confirmationTokenData.isEmpty()) {
            logWarn(AuthenticationController.class,
                    "Confirmation token not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Confirmation token not found"));
        }

        ConfirmationToken confirmationToken = confirmationTokenData.get();
        Optional<UserDto> userData = userService.getUserById(confirmationToken.getUser().getId());
        if (userData.isEmpty()) {
            logWarn(AuthenticationController.class,
                    "User with id = " + confirmationToken.getId() + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }

        try {
            boolean isEnabled = userService.confirmAccount(userData.get().getId());
            logInfo(AuthenticationController.class,
                    "User account confirmed with id = " + confirmationToken.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new SuccessResponse<>("User account confirmed successfully", isEnabled));
        } catch (Exception e) {
            logError(AuthenticationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<UserDto> userData = userService.getUserById(userDetails.getId());
        if (userData.isEmpty()) {
            logWarn(AuthenticationController.class, "User with id = " + userDetails.getId() + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        userService.setUserOnline(userData.get());

        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail());
        logInfo(AuthenticationController.class,
                "User with id = " + jwtResponse.getId() + " logged in successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Login successful", jwtResponse));
    }
}
