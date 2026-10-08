package cl.bci.users.v1.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.users.v1.dto.UserRequestDTO;
import cl.bci.users.v1.dto.UserResponseDTO;
import cl.bci.users.v1.services.UserService;

@RestController
@RequestMapping(value = "/api/v1/users",
        consumes = "application/json",
        produces = "application/json")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO response = this.userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
