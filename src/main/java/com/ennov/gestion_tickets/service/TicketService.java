package com.ennov.gestion_tickets.service;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.ennov.gestion_tickets.dto.TicketRequest;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.TicketUpdateRequest;
import com.ennov.gestion_tickets.entity.Ticket;
import com.ennov.gestion_tickets.entity.User;
import com.ennov.gestion_tickets.repository.TicketRepository;
import com.ennov.gestion_tickets.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ticket non retrouvé avec id " + id));
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        ticketRepository.findByTitre(ticketRequest.getTitre())
                .ifPresent(t -> {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Un ticket avec ce titre existe déjà.");
                });
        User user = userRepository.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Utilisateur non trouvé avec id " + ticketRequest.getUserId()));
        Ticket ticket = modelMapper.map(ticketRequest, Ticket.class);
        ticket.setUtilisateur(user);
        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public TicketResponse updateTicket(Integer id, TicketUpdateRequest ticketUpdateRequest) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ticket non retrouvé avec id " + id));

        ticketUpdateRequest.getTitre().ifPresent(titre -> {
            ticketRepository.findByTitre(titre)
                    .ifPresent(t -> {
                        if (!t.getId().equals(id)) { // Check if the found ticket is not the one being updated
                            throw new ApiException(HttpStatus.BAD_REQUEST, "Un autre ticket avec ce titre existe déjà.");
                        }
                    });
            ticket.setTitre(titre);
        });
        ticketUpdateRequest.getDescription().ifPresent(ticket::setDescription);
        ticketUpdateRequest.getStatut().ifPresent(ticket::setStatut);
        ticketUpdateRequest.getUserId().ifPresent(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Utilisateur non trouvé avec id " + userId));
            ticket.setUtilisateur(user);
        });

        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public void deleteTicket(Integer id) {
        if (!ticketRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Ticket non retrouvé avec id " + id);
        }
        ticketRepository.deleteById(id);
    }

    public TicketResponse assignTicketToUser(Integer ticketId, Integer userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ticket non retrouvé avec id " + ticketId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Utilisateur non trouvé avec id " + userId));
        ticket.setUtilisateur(user);
        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }
}
