package com.ennov.gestion_tickets.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.ennov.gestion_tickets.dto.UserRequest;
import com.ennov.gestion_tickets.dto.UserResponse;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.UserUpdateRequest;
import com.ennov.gestion_tickets.service.UserService;
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

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new Jdk8Module());
        mockMvc = standaloneSetup(userController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserResponse> users = Arrays.asList(new UserResponse(1, "User1", "user1@example.com"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void getUserTickets() throws Exception {
        List<TicketResponse> tickets = Arrays.asList(new TicketResponse(1, "Ticket1", "Description1", null, "en cours"));
        when(userService.getTicketsByUserId(1)).thenReturn(tickets);

        mockMvc.perform(get("/api/users/1/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void createUser() throws Exception {
        UserRequest userRequest = new UserRequest("User123456", "user1@example.com");
        UserResponse userResponse = new UserResponse(1, "User123456", "user1@example.com");

        when(userService.createUser(argThat(request ->
                request.getUsername().equals(userRequest.getUsername()) &&
                        request.getEmail().equals(userRequest.getEmail())
        ))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("User123456"));
    }


    @Test
    void updateUserSuccess() throws Exception {
        Integer userId = 1;
        String username = "ValidUsername123";
        String email = "valid@example.com";
        UserUpdateRequest updateRequest = new UserUpdateRequest(Optional.of(username), Optional.of(email));
        UserResponse updatedUser = new UserResponse(userId, username, email);
        when(userService.updateUser(eq(userId), any(UserUpdateRequest.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.email").value(email));
    }







}
