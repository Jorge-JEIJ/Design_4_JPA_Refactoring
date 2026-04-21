package com.example.demo.application.service;

import com.example.demo.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getPaginatedUsers(int page, int size);
    Optional<UserDTO> getUserById(Integer id);
    String saveUser(UserDTO dto);
    boolean deleteUser(Integer id);
    Optional<UserDTO> updateUserById(Integer id, UserDTO newUser);
    List<UserDTO> findUsersByName(String name);


    /*
    List<UserDTO> testTransaction();
    List<UserDTO> testTransactionEj3() throws Exception;
    UserDTO testTransactionEj4();
     */
}
