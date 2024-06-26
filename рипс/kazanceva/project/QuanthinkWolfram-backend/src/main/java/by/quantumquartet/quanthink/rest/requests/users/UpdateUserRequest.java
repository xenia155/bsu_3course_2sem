package by.quantumquartet.quanthink.rest.requests.users;

public class UpdateUserRequest extends RegisterRequest {

    public UpdateUserRequest(String email, String username, String password) {
        super(email, username, password);
    }
}
