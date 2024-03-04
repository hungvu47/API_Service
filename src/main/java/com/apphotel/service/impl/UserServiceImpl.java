package com.apphotel.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apphotel.exception.UserException;
import com.apphotel.model.Role;
import com.apphotel.model.User;
import com.apphotel.repository.RoleRepository;
import com.apphotel.repository.UserRepository;
import com.apphotel.service.IUserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User registerUser(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserException(user.getEmail() + " already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(userRole));

		return userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String email) {
		User user = getUserByEmail(email);
		if (user != null) {
			userRepository.deleteByEmail(email);
		}
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
	}

}
