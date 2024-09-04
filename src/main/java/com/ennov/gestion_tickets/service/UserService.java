package com.ennov.gestion_tickets.service;

import com.ennov.gestion_tickets.app.exception.ApiException;
import com.ennov.gestion_tickets.dto.TicketResponse;
import com.ennov.gestion_tickets.dto.UserRequest;
import com.ennov.gestion_tickets.dto.UserResponse;
import com.ennov.gestion_tickets.dto.UserUpdateRequest;
import com.ennov.gestion_tickets.entity.User;
import com.ennov.gestion_tickets.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
    }

    public UserResponse getUserById(Integer id){
        User user=userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur non retrouvé avec id" + id));
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse createUser(UserRequest userRequest){
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ce username existe déjà");
        }
        User user = modelMapper.map(userRequest, User.class);
        User newUser = userRepository.save(user);
        return  modelMapper.map(newUser, UserResponse.class);
    }

    public UserResponse updateUser(Integer id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur non retrouvé avec id " + id));

        updateRequest.getUsername().ifPresent(username -> {
            if (userRepository.findByUsername(username).isPresent() && !username.equals(user.getUsername())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Ce username est déjà utilisé par un autre utilisateur.");
            }
            user.setUsername(username);
        });
        updateRequest.getEmail().ifPresent(user::setEmail);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    public List<TicketResponse> getTicketsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur non retrouvé avec id " + userId));

        return user.getTickets().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }
}
