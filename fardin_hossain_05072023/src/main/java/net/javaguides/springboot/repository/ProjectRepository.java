package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    default List<Project> findByOwnerName(String userName){
        List<Project> projects = new ArrayList<>();

        for(Project project : this.findAll()){
            if(project.getOwner() == null){
                continue;
            }

            if(project.getOwner().getUsername().equals(userName)){
                projects.add(project);
            }
        }

        return projects;

    }
}
