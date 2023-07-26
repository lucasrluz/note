package com.api.note.controllers.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.note.dtos.user.UserDTOSaveDemoResponse;
import com.api.note.dtos.user.UserDTOSaveRequest;
import com.api.note.dtos.user.UserDTOSaveResponse;
import com.api.note.services.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserDTOSaveRequest userDTOSaveRequest) {
        try {
            UserDTOSaveResponse saveUserResponse = this.userService.save(userDTOSaveRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saveUserResponse);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/demo")
    public ResponseEntity<Object> saveDemo() {
        UserDTOSaveDemoResponse userDTOSaveDemoResponse = this.userService.saveDemo();

        return ResponseEntity.status(HttpStatus.OK).body(userDTOSaveDemoResponse);
    }
}
