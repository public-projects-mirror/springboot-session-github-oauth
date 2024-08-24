package com.example.mapper;

import com.example.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDO, String> {
    UserDO findByUsername(String username);

    UserDO findByUserId(String userId);
}
