package com.example.demo.specification;

import com.example.demo.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> searchByNameOrCityOrEmail(String search) {
        return (root, query, criteriaBuilder) -> {

            // If search is null or empty, return a specification that matches everything
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Returns "WHERE 1=1" (always true)
            }

            // Trim whitespace and convert to lowercase for case-insensitive search
            String searchTerm = "%" + search.trim().toLowerCase() + "%";

            // Create a list to hold our search conditions (predicates)
            List<Predicate> predicates = new ArrayList<>();

            /*
             * root.get("fieldName") - Gets the database column
             * criteriaBuilder.lower() - Converts to lowercase
             * criteriaBuilder.like() - SQL LIKE operator for pattern matching
             *
             * Example: LOWER(name) LIKE '%john%'
             */

            // Search in 'name' field
            // SQL equivalent: WHERE LOWER(name) LIKE '%searchTerm%'
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    searchTerm
            ));

            // Search in 'city' field
            // SQL equivalent: OR LOWER(city) LIKE '%searchTerm%'
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("city")),
                    searchTerm
            ));

            // Search in 'email' field
            // SQL equivalent: OR LOWER(email) LIKE '%searchTerm%'
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    searchTerm
            ));

            /*
             * criteriaBuilder.or() - Combines all predicates with OR condition
             *
             * Final SQL looks like:
             * WHERE LOWER(name) LIKE '%searchTerm%'
             *    OR LOWER(city) LIKE '%searchTerm%'
             *    OR LOWER(email) LIKE '%searchTerm%'
             */
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }


    // Search only in name field
    public static Specification<User> searchByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + name.trim().toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm);
        };
    }

    // Search only in city field
    public static Specification<User> searchByCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + city.trim().toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), searchTerm);
        };
    }

    // Search only in email field
    public static Specification<User> searchByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + email.trim().toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchTerm);
        };
    }


    public static Specification<User> withFilters(
            String name,
            String city,
            String email
    ) {
        return (root, query, cb) -> {

            // List to collect all predicates (filter conditions)
            List<Predicate> predicates = new ArrayList<>();


            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),           // Convert DB name to lowercase
                                "%" + name.toLowerCase() + "%"        // Convert search term to lowercase with wildcards
                        )
                );
            }


            if (city != null && !city.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%")
                );
            }


            if (email != null && !email.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%")
                );
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}