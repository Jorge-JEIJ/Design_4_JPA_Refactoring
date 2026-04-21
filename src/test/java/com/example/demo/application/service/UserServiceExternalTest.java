package com.example.demo.application.service;

import com.example.demo.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableWireMock
class UserServiceExternalTest {

    @Value("${wiremock.server.baseUrl}")
    private String wireMockUrl;

    UserServiceExternal userServiceExternal;

    @BeforeEach
    void beforeEach(){
        userServiceExternal = new UserServiceExternal(wireMockUrl+"/");
    }

    @Test
    void getPaginatedUsers() {


        stubFor(get("/users?page=0&size=10").willReturn(okJson("""
                [
                    {
                        "id": 2,
                        "name": "kkkkkkkkkkkkkk2",
                        "age": 18,
                        "allergies": null
                    }
                ]
                """)));


        var result = userServiceExternal.getPaginatedUsers(0, 10);


        assertThat(result)
                .isNotNull()
                .hasSize( 1 );
        assertThat(result)
                .element( 0)
                .isNotNull()
                .isInstanceOf(UserDTO.class)
                ;

    }
}