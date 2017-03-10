package controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.ProjectManager;
import model.Project;

@Stateful
@Named("SupMan")
public class SupervisorController {
	
	@Inject ProjectManager projectManager;
	
	public List<Project> listOfProjects() {
		return projectManager.getAllProjects();
	}
	

}
