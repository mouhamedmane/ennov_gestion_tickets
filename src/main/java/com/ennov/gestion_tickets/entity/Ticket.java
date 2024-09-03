package com.ennov.gestion_tickets.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tickets")
@Schema(description = "Entité représentant un ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du ticket, généré automatiquement par la base de données", example = "1")
    private Integer id;

    @NotBlank(message="Le titre du ticket ne peut être vide")
    @Size(min=10, max=100, message="Le titre du ticket doit contenir 10 à 100 caractères")
    @Column(nullable=false, unique = true)
    @Schema(description = "Le titre du ticket doit être valide et unique et ne peut pas être vide.  ", example="Exportation des données")
    private String titre;

    @NotBlank(message="La description du ticket ne peut être vide")
    @Size(min=30, max=500, message="La description du ticket doit contenir 30 à 500 caractères")
    @Column(nullable=false, length = 500)
    @Schema(description = "La description du ticket ne peut pas être vide et doit détaille le probléme.  ",  example = "Impossible d'exporter les données vers Excel")
    private String description;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @Schema(description = "Utiliseur auquel le ticket est assigné")
    private User utilisateur;

    @NotBlank(message = "Le statut du ticket ne peut pas être vide")
    @Pattern(regexp="en cours|terminé|annulé" , flags= Pattern.Flag.CASE_INSENSITIVE, message="Le statut n'accepte que les valeurs 'en cours', 'terminé', 'annulé'")
    @Column(nullable = false)
    @Schema(description = "Le statut du ticket définit l'état du ticket ne peut être vide",  example = "en cours")
    private String statut;

}
