package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public interface ProjectService {
    List<Project> getAllProjects();
    List<Project> getProjectsByOwnerName(String username);
    void saveProject(Project project);
    Project getProjectById(long id);
    void deleteProjectById(long id);
    Set<User> getUsersByProjectId(Long id);
}
