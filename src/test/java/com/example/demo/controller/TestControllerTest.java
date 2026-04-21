package com.example.demo.controller;

import com.example.demo.application.service.UserService;
import com.example.demo.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @Mock
    private UserService userService;

    @Test
    void getUsers() {
        int page = 0;
        int size = 10;
        when( userService.getPaginatedUsers(page, size )).thenReturn(
                List.of()
        );

        var result = testController.getUsers(page, size);

        assertThat( result )
                .isNotNull() //A list can be blank, but not null
                ;
        assertThat( result.size() )
                .isGreaterThanOrEqualTo( 0 )
                .isLessThanOrEqualTo( size );
    }

    @Test
    void getUserById() {
        int id = 1;
        when( userService.getUserById( id )).thenReturn(
                Optional.ofNullable(UserDTO.builder().id(id).build())
        );

        var result = testController.getUserById( id );

        assertThat( result )
                .isNotNull()
                .isNotEqualTo(ResponseEntity.notFound().build());
        assertThat( result.getBody())
                .isNotNull();
        assertThat( result.getBody().getId())
                .isNotNull()
                .isEqualTo( id );

        //Empty response
        when( userService.getUserById( 0 )).thenReturn(
                Optional.empty()
        );

        var result2 = testController.getUserById( 0 );

        assertThat( result2 )
                .isNotNull()
                .isEqualTo(ResponseEntity.notFound().build());

    }

    @Test
    void saveUser() {
        UserDTO userDTO = UserDTO.builder().id(1).build();

        when( userService.saveUser( userDTO ))
                .thenReturn( "User created with id: " + userDTO.getId());

        var result = testController.saveUser( userDTO );

        assertThat( result )
                .isNotNull()
                .isNotBlank();
        assertThat( result.substring( result.length()-1))
                .isEqualTo(String.valueOf( userDTO.getId()));

    }

    @Test
    void deleteUser() {
    }
}