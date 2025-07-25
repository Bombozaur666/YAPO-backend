package com.example.YAPO.repositories.user;

import com.example.YAPO.models.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
