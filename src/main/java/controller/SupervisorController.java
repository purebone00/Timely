package controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.ProjectManager;
import model.Project;

/**
 * Contains methods used by supervisors.
 */
@Stateful
@Named("SupMan")
public class SupervisorController {
    /**
     * Used for accessing project data in database (Project table).
     */
    @Inject
    ProjectManager projectManager;
    /**
     * Returns a list of all projects in existence.
     * @return List<Project> list of projects.
     */
    public List<Project> listOfProjects() {
        return projectManager.getAllProjects();
    }

    //When getting the list of your employees, remember to check the empdel flag to see if they are deleted
}
