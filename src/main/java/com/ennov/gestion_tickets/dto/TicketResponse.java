package com.ennov.gestion_tickets.dto;

public class TicketResponse {
    private Integer id;
    private String titre;
    private String description;
    private UserResponse utilisateur;
    private String statut;

    public TicketResponse (){

    }

    public TicketResponse (Integer id, String titre, String description, UserResponse utilisateur, String statut){
        this.id = id;
        this.description = description;
        this.titre = titre;
        this.utilisateur = utilisateur;
        this.statut = statut;
    }

    public Integer getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public UserResponse getUtilisateur() {
        return utilisateur;
    }

    public String getStatut() {
        return statut;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUtilisateur(UserResponse utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
