package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        super();
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {

        return projectRepository.findAll();
    }

    public List<Project> getProjectsByOwnerName(String username) {
        return projectRepository.findByOwnerName(username);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public Project getProjectById(long id) {
        return projectRepository.findById(id).get();
    }

    public void deleteProjectById(long id) {
        projectRepository.deleteById(id);
    }

    public Set<User> getUsersByProjectId(Long id) {
        return projectRepository.findById(id).get().getMembers();
    }
}
