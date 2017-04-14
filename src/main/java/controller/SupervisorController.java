package controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import frontend.EmployeeProfile;
import manager.EmployeeManager;
import manager.ProjectManager;
import manager.TitleManager;
import model.Employee;
import model.Project;
import model.Title;

/**
 * Contains methods used by supervisors.
 * @author Timely
 * @version 1.0
 */
@Stateful
@Named("SupMan")
public class SupervisorController {
    
    /**
     * Timesheet approver id.
     */
    private static final int TIMESHEET_APPROVER_TIT_ID = 6;
    
    /**
     * Used for accessing title data in database (title table).
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private TitleManager titleManager;
    /**
     * Used for accessing project data in database (Project table).
     */
    @Inject
    private ProjectManager projectManager;
    /**
     * Used for accessing employee data in database (Employee table).
     */
    @Inject
    private EmployeeManager employeeManager;
    /**
     * Represents employee that has successfully logged in.
     */
    @Inject
    private EmployeeProfile currentEmployee;
    /**
     * List of employees supervised by currently logged in employee.
     */
    private List<Employee> supEmps;
    
    /**
     * Gets employees supervised by selected supervisor.
     * @return list of employees supervised.
     */
    public List<Employee> getSupEmps() {
        if (supEmps == null) {
            supEmps = employeeManager.getEmpSup(currentEmployee.getCurrentEmployee());            
        }
        return supEmps;
    }

    /**
     * Set supEmps.
     * @param supEmps supEmps.
     */
    public void setSupEmps(List<Employee> supEmps) {
        this.supEmps = supEmps;
    }
    /**
     * Updates the supEmps list from the database.
     */
    public void refreshSupEmps() {
        supEmps = employeeManager.getEmpSup(currentEmployee.getCurrentEmployee());
    }
    /**
     * Returns a list of all projects in existence.
     * @return List<Project> list of projects.
     */
    public List<Project> listOfProjects() {
        return projectManager.getAllProjects();
    }

    //When getting the list of your employees, remember to check the empdel flag to see if they are deleted
    
    /**
     * Action method to page for assigning a timesheet approver.
     * @return navigation string
     */
    public String assignTA() {     
        return "assignTA";
    }
    /**
     * Gets a list of employees that are timesheet approvers and supervised by current employee.
     * @return list of timesheet approvers
     */
    public List<Employee> getCurTA() {
        List<Employee> temp = new ArrayList<Employee>();
        if (getSupEmps() == null) {
            return temp;            
        }
        for (Employee e: supEmps) {
            for (Title t : e.getTitles()) {
                if (t.getTitId() == TIMESHEET_APPROVER_TIT_ID) {
                    temp.add(e);
                    break;
                }
            }
        }
       return temp;
    }
    /**
     * Gets a list of employees supervised by current employee, but are not time sheet approvers.
     * @return list of timesheet approvers
     */
    public List<Employee> getNotTA() {
        List<Employee> temp = new ArrayList<Employee>();
        List<Employee> toRemove = new ArrayList<Employee>();
        if (getSupEmps() == null) {            
            return temp;
        }
        temp.addAll(supEmps);
        for (Employee e: temp) {
            for (Title t :e.getTitles()) {
                if (t.getTitId() == TIMESHEET_APPROVER_TIT_ID) {
                    toRemove.add(e);
                    break;
                }
            }
        }
       temp.removeAll(toRemove);
       return temp;
    }
    
    /**
     * Assigns given employee as a timesheet approver.
     * @param e employee being promoted
     * @return null to refesh page.
     */
    public String assignTA(Employee e) {
        e.getTitles().add(titleManager.find((short) TIMESHEET_APPROVER_TIT_ID));
        employeeManager.merge(e);
        employeeManager.flush();
        return null;
    }
    
    /**
     * Removes given employee as a timesheet approver.
     * @param e employee being demoted
     * @return null to refesh page.
     */
    public String removeTA(Employee e) {
        employeeManager.removeTitle(e, titleManager.find((short) TIMESHEET_APPROVER_TIT_ID));
        employeeManager.flush();
        refreshSupEmps();
        return null;
    }
}
