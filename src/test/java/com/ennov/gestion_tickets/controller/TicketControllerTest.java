package com.ennov.gestion_tickets.controller;

import com.ennov.gestion_tickets.dto.TicketRequest;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.TicketUpdateRequest;
import com.ennov.gestion_tickets.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new Jdk8Module());
        mockMvc = standaloneSetup(ticketController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getAllTickets() throws Exception {
        List<TicketResponse> tickets = Arrays.asList(
                new TicketResponse(1, "Titre1", "Description1", null, "en cours"),
                new TicketResponse(2, "Titre2", "Description2", null, "terminé")
        );
        when(ticketService.getAllTickets()).thenReturn(tickets);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void getTicketById() throws Exception {
        TicketResponse ticket = new TicketResponse(1, "Titre1", "Description1", null, "en cours");
        when(ticketService.getTicketById(1)).thenReturn(ticket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.titre").value("Titre1"));
    }

    @Test
    void createTicket() throws Exception {
        TicketRequest ticketRequest = new TicketRequest("Titre12345", "Description du ticket Nouveau De", 1, "en cours");
        TicketResponse ticketResponse = new TicketResponse(1, "Titre12345", "Description du ticket Nouveau De", null, "en cours");

        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(ticketResponse);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.titre").value("Titre12345"));
    }

    @Test
    void updateTicketSuccess() throws Exception {
        Integer ticketId = 1;
        String updatedTitre = "Titre modifié";
        String updatedStatut = "terminé";

        TicketUpdateRequest updateRequest = new TicketUpdateRequest(
                Optional.of(updatedTitre),
                Optional.empty(),
                Optional.empty(),
                Optional.of(updatedStatut)
        );
        TicketResponse updatedTicket = new TicketResponse(ticketId, updatedTitre, "Description du ticket", null, updatedStatut);

        when(ticketService.updateTicket(eq(ticketId), any(TicketUpdateRequest.class))).thenReturn(updatedTicket);

        mockMvc.perform(put("/api/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.titre").value(updatedTitre))
                .andExpect(jsonPath("$.data.statut").value(updatedStatut));
    }

    @Test
    void deleteTicket() throws Exception {
        doNothing().when(ticketService).deleteTicket(1);

        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isOk());
    }

    @Test
    void assignTicketToUser() throws Exception {
        Integer ticketId = 1;
        Integer userId = 2;
        TicketResponse ticketResponse = new TicketResponse(ticketId, "Titre", "Description", null, "en cours");

        when(ticketService.assignTicketToUser(ticketId, userId)).thenReturn(ticketResponse);

        mockMvc.perform(put("/api/tickets/1/assign/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
