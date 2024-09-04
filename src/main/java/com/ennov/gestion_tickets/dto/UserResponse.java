package com.ennov.gestion_tickets.dto;

import java.util.List;

public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private List<TicketResponse> tickets;

    public UserResponse(){

    }

    public UserResponse(Integer id, String username, String email, List<TicketResponse> tickets){
        this.id = id;
        this.username = username;
        this.email = email;
        this.tickets = tickets;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<TicketResponse> getTickets() {
        return tickets;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
    }
}
