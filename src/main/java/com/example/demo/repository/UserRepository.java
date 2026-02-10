package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * UserRepository - Data access layer for User entity
 *
 * Extends two interfaces:
 * 1. JpaRepository<User, Integer> - Provides basic CRUD operations (findAll, save, delete, etc.)
 * 2. JpaSpecificationExecutor<User> - Enables dynamic queries using Specifications
 *
 * JpaSpecificationExecutor provides methods like:
 * - findAll(Specification<User> spec, Pageable pageable) - Search with pagination
 * - findOne(Specification<User> spec) - Find single result
 * - count(Specification<User> spec) - Count matching records
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);

    User findByEmail(String email);
}

