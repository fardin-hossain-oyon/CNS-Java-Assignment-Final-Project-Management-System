package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Project;
import org.springframework.security.core.userdetails.UserDetailsService;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.web.dto.UserRegistrationDto;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	Set<Project> getProjectsByCollaboratorName(String username);
	void deleteProjectById(long id);
    User getUserByUsername(String username);
	List<User> getAllUsers();
	void saveUser(User existingUser);
//	Collection<User>
}
