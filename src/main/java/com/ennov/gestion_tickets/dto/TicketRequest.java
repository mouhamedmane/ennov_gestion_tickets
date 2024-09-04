package com.ennov.gestion_tickets.dto;

import jakarta.validation.constraints.*;

public class TicketRequest {

    @NotBlank(message="Le titre du ticket ne peut être vide")
    @Size(min=10, max=100, message="Le titre du ticket doit contenir 10 à 100 caractères")
    private String titre;

    @NotBlank(message="La description du ticket ne peut être vide")
    @Size(min=30, max=500, message="La description du ticket doit contenir 30 à 500 caractères")
    private String description;

    @NotNull(message = "L'identifiant de l'utilisateur est requis")
    @Positive(message = "L'identifiant de l'utilisateur doit être positif")
    private Integer userId;

    @NotBlank(message = "Le statut du ticket ne peut pas être vide")
    @Pattern(regexp="en cours|terminé|annulé" , flags= Pattern.Flag.CASE_INSENSITIVE, message="Le statut n'accepte que les valeurs 'en cours', 'terminé', 'annulé'")
    private String statut;

    public TicketRequest(){

    }

    public TicketRequest(String titre, String description, Integer userId, String statut){
        this.titre=titre;
        this.description=description;
        this.userId=userId;
        this.statut=statut;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getStatut() {
        return statut;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
