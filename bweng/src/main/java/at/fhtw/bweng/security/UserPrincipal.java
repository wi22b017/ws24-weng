package at.fhtw.bweng.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

public class UserPrincipal extends User {

    private UUID id;
    private String role;

    public UserPrincipal(UUID id, String username, String password, String role) {
        super(username, password, List.of(new SimpleGrantedAuthority(role)));
        this.id = id;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getRole(){
        return role;
    }
}
