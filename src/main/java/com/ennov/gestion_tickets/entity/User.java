package com.ennov.gestion_tickets.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users")
@Schema(description = "Entité représentant un utilisateur")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de l'utilisateur, généré automatiquement par la base de données", example = "1")
    private Integer id;

    @NotBlank(message="Username ne peut être vide")
    @Size(min=10, max=100, message="Username doit contenir 10 à 100 caractères")
    @Column(nullable=false, unique = true)
    @Schema(description = "Le username de l'utilisateur doit être valide et unique et ne peut pas être vide.  ", example="mouhamadoumane")
    private String username;

    @NotBlank(message="L'adresse email ne peut être vide")
    @Email(message="L'adresse email doit être valide")
    @Column(nullable=false)
    @Schema(description = "L'email de l'utilisateur doit être valide et ne peut pas être vide.  ", example="mouhamadou.mane@ennov.io")
    private String email;

    @OneToMany(mappedBy = "utilisateur")
    @Schema(description = "La liste de tickets assignés à l'utilisateur")
    private List<Ticket> tickets;

}
