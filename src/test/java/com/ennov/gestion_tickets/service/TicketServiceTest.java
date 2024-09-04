package com.ennov.gestion_tickets.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.ennov.gestion_tickets.dto.TicketRequest;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.TicketUpdateRequest;
import com.ennov.gestion_tickets.entity.Ticket;
import com.ennov.gestion_tickets.entity.User;
import com.ennov.gestion_tickets.repository.TicketRepository;
import com.ennov.gestion_tickets.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void getTicketById_found() {
        // Arrange
        Integer ticketId = 1;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setTitre("Important Task");
        ticket.setDescription("Complete ASAP");
        ticket.setStatut("en cours");

        TicketResponse expectedResponse = new TicketResponse(ticketId, "Important Task", "Complete ASAP", null, "en cours");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);

        // Act
        TicketResponse actualResponse = ticketService.getTicketById(ticketId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getTitre(), actualResponse.getTitre());
        verify(ticketRepository).findById(ticketId);
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void getTicketById_notFound() {
        // Arrange
        Integer ticketId = 1;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.getTicketById(ticketId));
        assertEquals("Ticket non retrouvé avec id " + ticketId, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void createTicket_success() {
        // Arrange
        TicketRequest ticketRequest = new TicketRequest("New Ticket", "Description of the new ticket", 1, "en cours");
        Ticket ticket = new Ticket();
        User user = new User();
        user.setId(1);
        ticket.setUtilisateur(user);

        TicketResponse expectedResponse = new TicketResponse(1, "New Ticket", "Description of the new ticket", null, "en cours");

        when(ticketRepository.findByTitre("New Ticket")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(ticketRequest, Ticket.class)).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Act
        TicketResponse actualResponse = ticketService.createTicket(ticketRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("New Ticket", actualResponse.getTitre());
        verify(ticketRepository).findByTitre("New Ticket");
        verify(userRepository).findById(1);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void createTicket_ticketTitleExists_throwsApiException() {
        // Arrange
        TicketRequest ticketRequest = new TicketRequest("Existing Ticket", "Description", 1, "en cours");

        when(ticketRepository.findByTitre("Existing Ticket")).thenReturn(Optional.of(new Ticket()));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.createTicket(ticketRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Un ticket avec ce titre existe déjà.", exception.getMessage());
    }

    @Test
    void createTicket_userNotFound_throwsApiException() {
        // Arrange
        TicketRequest ticketRequest = new TicketRequest("New Ticket", "Description", 99, "en cours");

        when(ticketRepository.findByTitre("New Ticket")).thenReturn(Optional.empty());
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.createTicket(ticketRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Utilisateur non trouvé avec id 99", exception.getMessage());
    }


    @Test
    void updateTicket_success() {
        // Arrange
        Integer ticketId = 1;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setTitre("Old Titles");

        TicketUpdateRequest updateRequest = new TicketUpdateRequest(Optional.of("New Titles"), Optional.of("Updated Description Description"), Optional.of(1), Optional.of("en cours"));
        User user = new User();
        user.setId(1);

        TicketResponse expectedResponse = new TicketResponse(ticketId, "New Titles", "Updated Description Description", null, "en cours");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.findByTitre("New Titles")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Act
        TicketResponse actualResponse = ticketService.updateTicket(ticketId, updateRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("New Titles", actualResponse.getTitre());
        verify(ticketRepository).save(ticket);
    }


    @Test
    void updateTicket_titleConflict_throwsApiException() {
        // Arrange
        Integer ticketId = 1;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        Ticket anotherTicket = new Ticket();
        anotherTicket.setId(2);

        TicketUpdateRequest updateRequest = new TicketUpdateRequest(Optional.of("Existing Title"), Optional.empty(), Optional.empty(), Optional.empty());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.findByTitre("Existing Title")).thenReturn(Optional.of(anotherTicket));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.updateTicket(ticketId, updateRequest));
        assertEquals("Un autre ticket avec ce titre existe déjà.", exception.getMessage());
    }

    @Test
    void updateTicket_userNotFound_throwsApiException() {
        // Arrange
        Integer ticketId = 1;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        TicketUpdateRequest updateRequest = new TicketUpdateRequest(Optional.empty(), Optional.empty(), Optional.of(99), Optional.empty());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.updateTicket(ticketId, updateRequest));
        assertEquals("Utilisateur non trouvé avec id 99", exception.getMessage());
    }

    @Test
    void deleteTicket_success() {
        // Arrange
        Integer ticketId = 1;
        when(ticketRepository.existsById(ticketId)).thenReturn(true);

        // Act
        ticketService.deleteTicket(ticketId);

        // Assert
        verify(ticketRepository).deleteById(ticketId);
    }

    @Test
    void deleteTicket_notFound_throwsApiException() {
        // Arrange
        Integer ticketId = 1;
        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.deleteTicket(ticketId));
        assertEquals("Ticket non retrouvé avec id " + ticketId, exception.getMessage());
    }

    @Test
    void assignTicketToUser_success() {
        // Arrange
        Integer ticketId = 1;
        Integer userId = 1;
        Ticket ticket = new Ticket();
        User user = new User();
        TicketResponse expectedResponse = new TicketResponse(ticketId, "Task", "Description", null, "en cours");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);

        // Act
        TicketResponse actualResponse = ticketService.assignTicketToUser(ticketId, userId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("Task", actualResponse.getTitre());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void assignTicketToUser_ticketNotFound_throwsApiException() {
        // Arrange
        Integer ticketId = 1;
        Integer userId = 1;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.assignTicketToUser(ticketId, userId));
        assertEquals("Ticket non retrouvé avec id " + ticketId, exception.getMessage());
    }

    @Test
    void assignTicketToUser_userNotFound_throwsApiException() {
        // Arrange
        Integer ticketId = 1;
        Integer userId = 99;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> ticketService.assignTicketToUser(ticketId, userId));
        assertEquals("Utilisateur non trouvé avec id " + userId, exception.getMessage());
    }
}
