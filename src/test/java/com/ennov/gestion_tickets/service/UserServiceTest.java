package com.ennov.gestion_tickets.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.UserRequest;
import com.ennov.gestion_tickets.dto.UserUpdateRequest;
import com.ennov.gestion_tickets.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.ennov.gestion_tickets.dto.UserResponse;
import com.ennov.gestion_tickets.entity.User;
import com.ennov.gestion_tickets.repository.UserRepository;
import com.ennov.gestion_tickets.app.exception.ApiException;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_Successfull() {
        // Arrange
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        user.setUsername("mouhamadoumane");
        user.setEmail("mouhamadou.mane@ennov.io");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserResponse expectedResponse = new UserResponse(userId, user.getUsername(), user.getEmail());
        when(modelMapper.map(user, UserResponse.class)).thenReturn(expectedResponse);

        // Act
        UserResponse actualResponse = userService.getUserById(userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(userRepository).findById(userId);
        verify(modelMapper).map(user, UserResponse.class);
    }

    @Test
    void getUserById_notFound() {
        // Arrange
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> userService.getUserById(userId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Utilisateur non retrouvé avec id" + userId, exception.getMessage());
    }

    @Test
    void createUser_successful() {
        // Arrange
        UserRequest userRequest = new UserRequest("newuser", "newuser@example.com");
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        User newUser = new User(1, "newuser", "newuser@example.com", null);

        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.empty());
        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(newUser);
        when(modelMapper.map(newUser, UserResponse.class)).thenReturn(new UserResponse(1, "newuser", "newuser@example.com"));

        // Act
        UserResponse result = userService.createUser(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newUser.getId(), result.getId());
        assertEquals(newUser.getUsername(), result.getUsername());
        assertEquals(newUser.getEmail(), result.getEmail());
        verify(userRepository).findByUsername(userRequest.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_usernameExists() {
        // Arrange
        UserRequest userRequest = new UserRequest("existinguser", "existing@example.com");
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(new User()));

        // Act & Assert
        ApiException thrown = assertThrows(ApiException.class, () -> userService.createUser(userRequest));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Ce username existe déjà", thrown.getMessage());
    }


    @Test
    void updateUser_success() {
        // Arrange
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@example.com");

        UserUpdateRequest updateRequest = new UserUpdateRequest(Optional.of("newUsername"), Optional.of("new@example.com"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(new UserResponse(userId, "newUsername", "new@example.com"));

        // Act
        UserResponse result = userService.updateUser(userId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }



    @Test
    void updateUser_usernameExists() {
        // Arrange
        Integer userId = 1;
        User existingUser = new User(userId, "oldUsername", "old@example.com", null);
        UserUpdateRequest updateRequest = new UserUpdateRequest(Optional.of("newUsername"), Optional.empty());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.of(new User()));

        // Act & Assert
        ApiException thrown = assertThrows(ApiException.class, () -> userService.updateUser(userId, updateRequest));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Ce username est déjà utilisé par un autre utilisateur.", thrown.getMessage());
    }


    @Test
    void getTicketsByUserId_found() {
        // Arrange
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        List<Ticket> tickets = List.of(new Ticket(1, "Ticket 1", "Description 1", user, "en cours"),
                new Ticket(2, "Ticket 2", "Description 2", user, "terminé"));
        user.setTickets(tickets);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenAnswer(invocation -> {
                    Ticket ticket = invocation.getArgument(0, Ticket.class);
                    return new TicketResponse(
                            ticket.getId(),
                            ticket.getTitre(),
                            ticket.getDescription(),
                            null,
                            ticket.getStatut()
                    );
                });

        // Act
        List<TicketResponse> result = userService.getTicketsByUserId(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ticket 1", result.get(0).getTitre());
        assertEquals("Ticket 2", result.get(1).getTitre());
        verify(userRepository).findById(userId);
    }

    @Test
    void getTicketsByUserId_notFound_throwsApiException() {
        // Arrange
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException thrown = assertThrows(ApiException.class, () -> userService.getTicketsByUserId(userId));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Utilisateur non retrouvé avec id " + userId, thrown.getMessage());
    }

}
