package com.backend.repository;

import com.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
}
