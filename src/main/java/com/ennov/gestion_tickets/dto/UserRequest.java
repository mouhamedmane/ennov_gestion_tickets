package com.ennov.gestion_tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @NotBlank(message="Username ne peut être vide")
    @Size(min=10, max=100, message="Username doit contenir 10 à 100 caractères")
    private String username;

    @NotBlank(message="L'adresse email ne peut être vide")
    @Email(message="L'adresse email doit être valide")
    private String email;

    public UserRequest(){

    }

    public UserRequest(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
