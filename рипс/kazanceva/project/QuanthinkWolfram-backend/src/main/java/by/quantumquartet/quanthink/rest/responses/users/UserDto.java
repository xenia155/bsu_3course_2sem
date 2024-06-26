package by.quantumquartet.quanthink.rest.responses.users;

import by.quantumquartet.quanthink.models.ERole;

import java.util.Set;

public class UserDto {
    private long id;
    private String email;
    private String username;
    private Set<ERole> roles;

    public UserDto() {
    }

    public UserDto(long id, String email, String username, Set<ERole> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<ERole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }
}
