package com.example.SpringBatchPOC.repository;

import com.example.SpringBatchPOC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
