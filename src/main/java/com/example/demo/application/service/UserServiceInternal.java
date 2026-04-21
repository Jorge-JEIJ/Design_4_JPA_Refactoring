package com.example.demo.application.service;

import com.example.demo.entity.User;
import com.example.demo.model.UserDTO;
import com.example.demo.repository.AllergyRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "module.service.impl", havingValue= "internal")
public class UserServiceInternal implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllergyRepository allergyRepository;

    // 🔹 GET paginado
    public List<UserDTO> getPaginatedUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 GET by ID
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDTO);
    }

    // 🔹 CREATE
    public String saveUser(UserDTO dto) {
        User user = toEntity(dto);
        User saved = userRepository.save(user);
        return "User created with ID: " + saved.getId();
    }

    // 🔹 DELETE
    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    // 🔹 UPDATE
    public Optional<UserDTO> updateUserById(Integer id, UserDTO newUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(newUser.getName());
                    existingUser.setAge(newUser.getAge());
                    return userRepository.save(existingUser);
                })
                .map(this::toDTO);
    }

    // 🔹 SEARCH
    public List<UserDTO> findUsersByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔄 MAPPERS
    // =========================

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .allergies(null)
                .build();
    }

    private User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .build();
    }

    //EJERCIICO 2
    @Transactional
    public List<UserDTO> testTransaction(){
        User user1 = User.builder().name("John Doe").age(20).build();
        User saved1 = userRepository.save(user1);

        if(true)throw new RuntimeException("Forced exception");

        User user2 = User.builder().name("Jane Doe").age(20).build();
        User saved2 = userRepository.save(user2);

        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //EJERCICIO 3
    @Transactional(rollbackOn = Exception.class)
    public List<UserDTO> testTransactionEj3() throws Exception {
        User user1 = User.builder().name("John Doe").age(20).build();
        User saved1 = userRepository.save(user1);

        if(true)throw new Exception("Forced exception");

        User user2 = User.builder().name("Jane Doe").age(20).build();
        User saved2 = userRepository.save(user2);

        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //EJERCICIO 4
    @Transactional()
    public UserDTO testTransactionEj4() {
        User user = userRepository.findById(1).orElseThrow();
        if(user.getAllergies()==null || user.getAllergies().isEmpty())
            user.setAllergies(List.of( allergyRepository.findById(1).orElseThrow() ));
        else throw new RuntimeException("USER ALLERGY LIST NOT EMPTY");

        return this.toDTO(user);
    }
}