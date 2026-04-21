package com.example.demo.application.service;

import com.example.demo.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(value = "module.service.impl", havingValue= "external")
public class UserServiceExternal implements UserService {
    private final RestTemplate restTemplate;
    private final String baseUrl; //"http://localhost:8080/"

    public UserServiceExternal( @Value("${module.service.url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    // Simple GET that returns a Map (assumes JSON object)
    public List<UserDTO> getPaginatedUsers(int page, int size) {
        //return restTemplate.getForObject(baseUrl + "/users", UserDTO.class);
        return restTemplate.exchange(baseUrl + "users?page=0&size=10", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<UserDTO>>() {}).getBody();
    }

    public Optional<UserDTO> getUserById(Integer id){
        return Optional.ofNullable(restTemplate.getForObject(baseUrl + "user/" + id, UserDTO.class));
    }

    public String saveUser(UserDTO dto){
        //return "N/A";
        try {
            return restTemplate.postForObject(baseUrl + "user", dto, String.class);
        }catch (RestClientException e){
            return "ERROR";
        }
    }

    public boolean deleteUser(Integer id){
        try {
            restTemplate.delete(baseUrl + "user/"+id);
        }catch (RestClientException e){
            return false;
        }
        return true;
    }

    public Optional<UserDTO> updateUserById(Integer id, UserDTO newUser){
        //restTemplate.put(baseUrl + "user/" +id, newUser);
        return restTemplate.exchange(baseUrl + "user/" +id,
                HttpMethod.PUT, new HttpEntity<>(newUser),
                new ParameterizedTypeReference<Optional<UserDTO>>() {}).getBody();
    }

    public List<UserDTO> findUsersByName(String name){
        return restTemplate.exchange(baseUrl + "user/search",
                HttpMethod.GET, HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<UserDTO>>() {}).getBody();
    }
}
