package com.apphotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apphotel.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String roleUser);

	boolean existsByName(String theRole);

}
