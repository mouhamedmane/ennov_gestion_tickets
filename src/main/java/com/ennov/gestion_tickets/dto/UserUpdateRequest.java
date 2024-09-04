package com.ennov.gestion_tickets.dto;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserUpdateRequest {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private Optional<String> username;
    private Optional<String> email;

    public UserUpdateRequest() {
        this.username = Optional.empty();
        this.email = Optional.empty();
    }

    @JsonCreator
    public UserUpdateRequest(
            @JsonProperty("username") Optional<String> username,
            @JsonProperty("email") Optional<String> email) {
        setUsername(username);
        setEmail(email);
    }



    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        username.ifPresent(name -> {
            if (name.length() < 10 || name.length() > 100) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"Username doit contenir entre 10 et 100 caractères");
            }
        });
        this.username = username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        email.ifPresent(mail -> {
            if (!EMAIL_PATTERN.matcher(mail).matches()) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"L'adresse email doit être valide");
            }
        });
        this.email = email;
    }
}
