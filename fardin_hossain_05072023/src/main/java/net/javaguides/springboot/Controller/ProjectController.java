package net.javaguides.springboot.Controller;


import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ProjectRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Iterator;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    // write an api to get all projects
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Project> getAllProjects() {

        List<Project> projects = projectService.getAllProjects();

        return projects;
    }


}
