package net.javaguides.springboot.Controller;

import com.ctc.wstx.exc.WstxOutputException;
import net.javaguides.springboot.Jasper_Report.JasperReportService;
import net.javaguides.springboot.Utils.UserSelectionForm;
import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ProjectRepository;
import net.javaguides.springboot.service.ProjectService;
import net.javaguides.springboot.service.UserService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private JasperReportService jasperReportService;

	@GetMapping("/getProjects")
	public List<Project> getEmployees() {
		return projectRepository.findAll();
	}

	@GetMapping("/report/{format}")
	public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
		return jasperReportService.exportReport(format);
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String loginPost() {
		return "redirect:/";
	}
	
	@GetMapping("/")
	public String home(Model model) {

		//get username of currently logged in user
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		System.out.println("Currently logged in user: " + username);

		model.addAttribute("projects", projectService.getProjectsByOwnerName(username));
		model.addAttribute("collaborations", userService.getProjectsByCollaboratorName(username));
		model.addAttribute("allProjects", projectService.getAllProjects());

		return "homepage";
	}



	@GetMapping("/newProject")
	public String newProject(Model model) {

		Project project = new Project();
		model.addAttribute("project", project);

		return "createProject";
	}


	@PostMapping("/newProject")
	public String addNewProject(Project project, Model model) throws ParseException {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		System.out.println("Currently logged in user: " + username);

		User user = userService.getUserByUsername(username);

		project.setOwner(user);

		//get current date
		LocalDate localDate = LocalDate.now();
		java.util.Date currentDate = java.sql.Date.valueOf(localDate);


		if(project.getStartDate().after(currentDate)){
			project.setStatus("PRE");
		}
		else if((project.getStartDate().before(currentDate) || project.getStartDate().equals(currentDate)) && (project.getEndDate().after(currentDate) || project.getEndDate().equals(currentDate)) || project.getEndDate() == null){
			project.setStatus("STARTED");
		}
		else if(project.getEndDate().before(currentDate)){
			project.setStatus("ENDED");
		}

		System.out.println("Project start and end dates: " + project.getStartDate() + " " + project.getEndDate());

		projectService.saveProject(project);

		return "redirect:/";
	}


	@GetMapping("/editProject/{id}")
	public String editProject(@PathVariable Long id, Model model) {

		//check if the logged in user is the owner of the project
		if (seeIfAuthorized(id)) return "error";


		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);

		return "updateProject";
	}

	@PostMapping("/editProject/{id}")
	public String updateProject(@PathVariable Long id, Project project, Model model) {

		Project existingProject = projectService.getProjectById(id);

		if (seeIfAuthorized(id)) return "error";

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		System.out.println("Currently logged in user: " + username);
		User user = userService.getUserByUsername(username);
//		User user = userRepository.findByUsername(username);

		existingProject.setOwner(user);

		//get current date
		LocalDate localDate = LocalDate.now();
		java.util.Date currentDate = java.sql.Date.valueOf(localDate);


		if(project.getStartDate().after(currentDate)){
			project.setStatus("PRE");
		}
		else if((project.getStartDate().before(currentDate) || project.getStartDate().equals(currentDate)) && (project.getEndDate().after(currentDate) || project.getEndDate().equals(currentDate)) || project.getEndDate() == null){
			project.setStatus("STARTED");
		}
		else if(project.getEndDate().before(currentDate)){
			project.setStatus("ENDED");
		}

		existingProject.setName(project.getName());
		existingProject.setIntro(project.getIntro());
		existingProject.setStatus(project.getStatus());
		existingProject.setStartDate(project.getStartDate());
		existingProject.setEndDate(project.getEndDate());

		projectService.saveProject(existingProject);

		return "redirect:/";
	}


	@GetMapping("/deleteProject/{id}")
	public String deleteProject(@PathVariable Long id) {
		System.out.println("Deleting project with id: " + id);

		if (seeIfAuthorized(id)) return "error";

		userService.deleteProjectById(id);
		projectService.deleteProjectById(id);
		return "redirect:/";
	}

	private boolean seeIfAuthorized(Long id) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		System.out.println("Currently logged in user: " + username);
		System.out.println("IN DELETE");
//		User user = userRepository.findByUsername(username);
		if(!projectService.getProjectById(id).getOwner().getUsername().equals(username)) {
			return true;
		}
		return false;
	}

	@GetMapping("/addProjectMembers/{id}")
	public String addProjectMember(@PathVariable Long id, Model model) {

		if (seeIfAuthorized(id)) return "error";

		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);

		List<User> allUsers = userService.getAllUsers();
		List<User> users = new ArrayList<>();
//		List<User> users = userRepository.findAll();

		// remove current user
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String username = loggedInUser.getName();
		System.out.println("Currently logged in user: " + username);

		User user = userService.getUserByUsername(username);

		for(int i=0; i<allUsers.size();i++){

			if(!allUsers.get(i).getUsername().equals(username) && !project.getMembers().contains(allUsers.get(i))){
				users.add(allUsers.get(i));
			}

		}

		model.addAttribute("users", users);

		UserSelectionForm userSelectionForm = new UserSelectionForm();
		model.addAttribute("userSelectionForm", userSelectionForm);

		return "addProjectMembers";
	}

	@PostMapping("/addProjectMembers/{id}")
	public String addProjectMember(@PathVariable Long id, UserSelectionForm form, Model model) {

		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);

//		List<User> users = userRepository.findAll();
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);

		String selectedUser = form.getSelectedUser();

//		User existingUser = userRepository.findByUsername(selectedUser);
		User existingUser = userService.getUserByUsername(selectedUser);
		existingUser.getProjects().add(project);
		userService.saveUser(existingUser);

//		userRepository.save(existingUser);
		return "redirect:/";
	}

	@GetMapping("/removeProjectMember/{id}")
	public String removeProjectMember(@PathVariable Long id, Model model) {

		if (seeIfAuthorized(id)) return "error";

		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);

		Set<User> users = projectService.getUsersByProjectId(id);
		model.addAttribute("users", users);

		UserSelectionForm userSelectionForm = new UserSelectionForm();
		model.addAttribute("userSelectionForm", userSelectionForm);

		return "removeProjectMember";
	}

	@PostMapping("/removeProjectMember/{id}")
	public String removeProjectMember(@PathVariable Long id, @ModelAttribute("userSelectionForm") UserSelectionForm form, Model model) {

		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);

		Set<User> users = projectService.getUsersByProjectId(id);
		model.addAttribute("users", users);

		String selectedUser = form.getSelectedUser();
//		User existingUser = userRepository.findByUsername(selectedUser);

		User existingUser = userService.getUserByUsername(selectedUser);

		existingUser.getProjects().remove(project);
		userService.saveUser(existingUser);

		return "redirect:/";
	}



}
