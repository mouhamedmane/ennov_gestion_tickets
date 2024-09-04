package com.ennov.gestion_tickets.controller;

import com.ennov.gestion_tickets.dto.UserRequest;
import com.ennov.gestion_tickets.dto.UserResponse;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.UserUpdateRequest;
import com.ennov.gestion_tickets.service.UserService;
import com.ennov.gestion_tickets.app.responseApi.ResponseApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer tous les utilisateurs")
    @ApiResponse(responseCode = "200", description = "Utilisateurs récupérés avec succès")
    @GetMapping
    public ResponseEntity<ResponseApi<List<UserResponse>>> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(ResponseApi.success(users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Récupérer les tickets assignés à un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<ResponseApi<List<TicketResponse>>> getUserTickets(@PathVariable Integer id) {
        try {
            List<TicketResponse> tickets = userService.getTicketsByUserId(id);
            return ResponseEntity.ok(ResponseApi.success(tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Créer un nouvel utilisateur")
    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès")
    @PostMapping
    public ResponseEntity<ResponseApi<UserResponse>> createUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse userResponse = userService.createUser(userRequest);
            return ResponseEntity.ok(ResponseApi.success(userResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Modifier un utilisateur existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<UserResponse>> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequest updateRequest) {
        try {
            UserResponse updatedUser = userService.updateUser(id, updateRequest);
            return ResponseEntity.ok(ResponseApi.success(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }
}
