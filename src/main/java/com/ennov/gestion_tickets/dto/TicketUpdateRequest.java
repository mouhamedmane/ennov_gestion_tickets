package com.ennov.gestion_tickets.dto;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class TicketUpdateRequest {
    private Optional<String> titre;
    private Optional<String> description;
    private Optional<Integer> userId;
    private Optional<String> statut;

    public TicketUpdateRequest() {
        this.titre = Optional.empty();
        this.description = Optional.empty();
        this.userId = Optional.empty();
        this.statut = Optional.empty();
    }

    @JsonCreator
    public TicketUpdateRequest(@JsonProperty("titre") Optional<String> titre,
                               @JsonProperty("description") Optional<String> description,
                               @JsonProperty("userId") Optional<Integer> userId,
                               @JsonProperty("statut") Optional<String> statut) {
        setTitre(titre);
        setDescription(description);
        setUserId(userId);
        setStatut(statut);
    }

    public Optional<String> getTitre() {
        return titre;
    }

    public void setTitre(Optional<String> titre) {
        titre.ifPresent(t -> {
            if (t.length() < 10 || t.length() > 100) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Le titre du ticket doit contenir entre 10 et 100 caractères");
            }
        });
        this.titre = titre;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        description.ifPresent(desc -> {
            if (desc.length() < 30 || desc.length() > 500) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"La description du ticket doit contenir entre 30 et 500 caractères");
            }
        });
        this.description = description;
    }

    public Optional<Integer> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Integer> userId) {
        userId.ifPresent(id -> {
            if (id <= 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"L'identifiant de l'utilisateur doit être positif");
            }
        });
        this.userId = userId;
    }

    public Optional<String> getStatut() {
        return statut;
    }

    public void setStatut(Optional<String> statut) {
        statut.ifPresent(s -> {
            if (!s.matches("en cours|terminé|annulé")) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"Le statut n'accepte que les valeurs 'en cours', 'terminé', 'annulé'");
            }
        });
        this.statut = statut;
    }
}
