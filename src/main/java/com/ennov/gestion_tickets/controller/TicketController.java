package com.ennov.gestion_tickets.controller;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.ennov.gestion_tickets.dto.TicketRequest;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.TicketUpdateRequest;
import com.ennov.gestion_tickets.service.TicketService;
import com.ennov.gestion_tickets.app.responseApi.ResponseApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Récupérer tous les tickets")
    @ApiResponse(responseCode = "200", description = "Tickets récupérés avec succès")
    @GetMapping
    public ResponseEntity<ResponseApi<List<TicketResponse>>> getAllTickets() {
        try {
            List<TicketResponse> tickets = ticketService.getAllTickets();
            if (tickets.isEmpty()) {
                return ResponseEntity.ok(ResponseApi.fail(List.of("Aucun ticket trouvé.")));
            }
            return ResponseEntity.ok(ResponseApi.success(tickets));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Récupérer un ticket par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket trouvé avec succès"),
            @ApiResponse(responseCode = "404", description = "Ticket non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<TicketResponse>> getTicketById(@PathVariable Integer id) {
        try {
            TicketResponse ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok(ResponseApi.success(ticket));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Créer un nouveau ticket")
    @ApiResponse(responseCode = "201", description = "Ticket créé avec succès")
    @PostMapping
    public ResponseEntity<ResponseApi<TicketResponse>> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        try {
            TicketResponse ticket = ticketService.createTicket(ticketRequest);
            return ResponseEntity.ok(ResponseApi.success(ticket));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Mettre à jour un ticket existant")
    @ApiResponse(responseCode = "200", description = "Ticket mis à jour avec succès")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<TicketResponse>> updateTicket(@PathVariable Integer id, @RequestBody TicketUpdateRequest ticketUpdateRequest) {
        try {
            TicketResponse updatedTicket = ticketService.updateTicket(id, ticketUpdateRequest);
            return ResponseEntity.ok(ResponseApi.success(updatedTicket));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Assigner un ticket à un utilisateur")
    @ApiResponse(responseCode = "200", description = "Ticket assigné avec succès")
    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<ResponseApi<TicketResponse>> assignTicketToUser(@PathVariable Integer id, @PathVariable Integer userId) {
        try {
            TicketResponse assignedTicket = ticketService.assignTicketToUser(id, userId);
            return ResponseEntity.ok(ResponseApi.success(assignedTicket));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }

    @Operation(summary = "Supprimer un ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Ticket non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteTicket(@PathVariable Integer id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.ok(ResponseApi.success(null));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(ResponseApi.fail(List.of(e.getMessage())));
        }
    }
}
