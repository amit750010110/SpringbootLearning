package com.example.demo.service;

import com.example.demo.Solid.S.S;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.specification.UserSpecification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User addUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        return userRepository.save(user);
    }

    public List<User> bulkUserCreationByJson(List<UserDTO> userDTOList) {
        List<User> users = new ArrayList<>();

        for (UserDTO dto : userDTOList) {
            User user = modelMapper.map(dto, User.class);
            users.add(user);
        }

        return userRepository.saveAll(users);
    }


    @Transactional
    public List<User> bulkUserCreationByCSV(MultipartFile file) {

        List<User> users = new ArrayList<>();

        try (
                Reader reader = new BufferedReader(
                        new InputStreamReader(file.getInputStream()));
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT
                                .withFirstRecordAsHeader()
                                .withIgnoreHeaderCase()
                                .withTrim())
        ) {

            for (CSVRecord record : csvParser) {

                User user = new User();
                user.setName(record.get("name"));
                user.setCity(record.get("city"));
                user.setEmail(record.get("email"));

                users.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file", e);
        }

        return userRepository.saveAll(users);
    }

    public Page<User> getAllPaginatedWithSearch(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        // If search is empty or null, return all users
        if (search == null || search.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }

        // Otherwise, perform search across name, city, and email
        return userRepository.searchUsers(search.trim(), pageable);
    }

    public User getByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        User user = userRepository.findByEmail(email.trim());

        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        return user;
    }


    public Page<User> getAllUsersUsingSpecification(int page, int size, String search, String name, String city, String email) {

        validatePaginationParameters(page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<User> userSpecification ;

        if (search != null && !search.isBlank()) {
            userSpecification = UserSpecification.searchByNameOrCityOrEmail(search);
        } else {
            userSpecification = UserSpecification.withFilters(name, city, email);
        }

        return userRepository.findAll(userSpecification, pageRequest);
    }

    private void validatePaginationParameters(int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page number must be 0 or greater.");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100. Requested: " + size
            );
        }
    }

    public static Specification<User> searchByNameOrCityOrEmail(String search) {

        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return cb.conjunction(); // no filters applied
            }

            String pattern = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("city")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }


}
