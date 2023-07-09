package net.javaguides.springboot.Jasper_Report;

import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.repository.ProjectRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.ProjectDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "E:\\Java_Training\\JAVA_Assignment\\trying_to_add_members_with_hashset\\src\\main\\resources\\reports";
        List<Project> projects = projectRepository.findAll();

        //convert projects list to project dto list
        List<ProjectDto> projectDtos = new ArrayList<>();

        for(int i = 0; i<projects.size(); i++) {
        	ProjectDto projectDto = new ProjectDto();
        	projectDto.setId(projects.get(i).getId());
        	projectDto.setName(projects.get(i).getName());
        	projectDto.setIntro(projects.get(i).getIntro());
        	projectDto.setOwner_name(userRepository.findById(projects.get(i).getOwner().getId()).get().getUsername());
        	projectDto.setStatus(projects.get(i).getStatus());
        	projectDto.setStartDate(projects.get(i).getStartDate());
        	projectDto.setEndDate(projects.get(i).getEndDate());
        	projectDtos.add(projectDto);
        }


        //load file and compile it
        File file = ResourceUtils.getFile("E:\\Java_Training\\JAVA_Assignment\\trying_to_add_members_with_hashset\\src\\main\\java\\net\\javaguides\\springboot\\Jasper_Report\\jasper.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(projectDtos);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Fardin Hossain");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\projects.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\projects.pdf");
        }

        return "report generated in path : " + path;
    }
}
