package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.LabourGradeManager;
import manager.ProjectManager;
import manager.TitleManager;
import manager.WorkPackageManager;
import model.Employee;
import model.Labgrd;
import model.Project;
import model.Title;
import model.Workpack;
import model.WorkpackId;

/**
 * Contains methods used by admin to alter employee roster.
 */
@SuppressWarnings("serial")
@SessionScoped
@Named("Admin")
public class AdminController implements Serializable {
    
	/**
	 * Project that contains SICK/VACATION/FLEX work packages.
	 */
    public static final int FLEX_PROJ_NO = TimesheetApproverController.FLEX_PROJ_NO;
    /**
     * Work package id for flex time.
     */
    public static final String FLEX_WP_NO = TimesheetApproverController.FLEX_WP_NO;
    
    /**
     * Used for accessing employee data in database (Employee table).  
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Represents employee whose information is being altered.
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private Employee newEmployee;

    /**
     * Contains methods used by employees to interact with their timesheets.
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private EmployeeController employeeController;

    /**
     * Used for accessing labour grade data in database (Labgrd table).
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private LabourGradeManager labourGradeManager;
    
    /**
     * Used for accessing project data in database (Project table).
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private ProjectManager projectManager;
    
    /**
     * Used for accessing work package data in database (Workpack table).
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private WorkPackageManager workpackManager;
    
    /**
     * Used for accessing title data in database (title table).
     * @HasGetter
     * @HasSetter
     */
    @Inject
    private TitleManager titleManager;
    
    /**
     * Represents the currently selected supervisor to display details on.
     * @HasGetter
     * @HasSetter
     */
    private Employee selectedSup;

    public Employee getSelectedSup() {
        return selectedSup;
    }

    public void setSelectedSup(Employee selectedSup) {
        this.selectedSup = selectedSup;
    }

    public EmployeeController getEmployeeController() {
        return employeeController;
    }

    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee employee) {
        newEmployee = employee;
    }
    
    /**
     * Adds new employee to company's employee roster.
     * @return String navigation string for returning to admin homepage. 
     */
    public String addEmployee() {
        newEmployee.setEmpFlxTm(BigDecimal.ZERO);
        
        // add employee to project 1111 which contains the SICK/VACATION/FLEX WP's
        Project defaultProj = projectManager.find(FLEX_PROJ_NO);
        defaultProj.getEmployees().add(newEmployee);
        newEmployee.setProjects(new HashSet<Project>());
        newEmployee.getProjects().add(defaultProj);
        
        // add employee to __FLEX WP so they can charge to flextime
        Workpack flex = workpackManager.find(new WorkpackId(FLEX_PROJ_NO, FLEX_WP_NO));
        flex.getEmployees().add(newEmployee);
        newEmployee.setWorkpackages(new HashSet<Workpack>());
        newEmployee.getWorkpackages().add(flex);
        
        employeeManager.persist(newEmployee);
        projectManager.update(defaultProj);
        workpackManager.merge(flex);
        employeeController.refreshList();
        return "admin";
    }

    public List<Labgrd> getLabourGrades() {
        return labourGradeManager.getAll();
    }

    /**
     * Generated editable form fields for making changes 
     * to an employee's information.
     * @return String null navigation string for refreshing the current page.
     */
    public String editAction(Employee employee) {
        employee.setEditable(true);
        return null;
    }

    /**
     * Saves employee information inputted into form fields.
     * @return String navigation string that refreshes current page. 
     */
    public String saveAction(Employee e) {

        e.setEditable(false);
        employeeManager.merge(e);
        employeeManager.flush();
        // return to current page
        return null;

    }

    /**
     * Deletes a given employee.
     * @param e the employee to delete.
     * @return String navigation string for refreshing the current page.
     */
    public String delete(Employee e) {

        employeeManager.delete(e);

        employeeManager.merge(e);
        // employeeManager.flush();

        // return to current page
        return null;
    }

    /**
     * Restores the employee so its no longer deleted.
     * @param e the employee to delete.
     * @return String navigation string for refreshing the current page.
     */
    public String restore(Employee e) {

        employeeManager.restore(e);

        employeeManager.merge(e);
        // employeeManager.flush();

        // return to current page
        return null;
    }
    
    /**
     * Promotes given employee into a supervisor
     * @param e employee to promote 
     */
    public String makeSup(Employee e) {
        
        e.getTitles().add(titleManager.find((short)1));
        employeeManager.merge(e);
        employeeManager.flush();
        return null;
    }
    
    /**
     * Checks if given employee is a supervisor
     * @param e employee being checked
     * @return true if employee is a supervisor false otherwise
     */
    public boolean checkSup(Employee e){
        for(Title t: e.getTitles()){
            if(t.getTitId() == 1)
                return true;
        }
        return false;
    }
    
    /**
     * Removes given employee from being a supervisor
     * @param e employee to demote 
     */
    public String removeSup(Employee e) {
        employeeManager.removeTitle(e, titleManager.find((short)1));
        employeeManager.flush();
        employeeController.resetList();
        return null;
    }
    
    /**
     * Action method to move to assignEmpToSup.xhtml
     * @param e
     * @return
     */
    public String selectSupForAssign(Employee e) {
        setSelectedSup(e);
        return "assignEmpToSup";
    }
    
    /**
     * Gets a list of employees supervised by selected supervisor
     * @return list of employees
     */
    public List<Employee> getSupEmp(){
        
        return employeeManager.getEmpSup(selectedSup);
    }
    
    /**
     * Gets a list of employees not supervised by selected supervisor
     * @return list of employees
     */
    public List<Employee> getNotSupEmp(){
        
        return employeeManager.getEmpNotSup(selectedSup);
    }
    
    /**
     * Assigns given employee to the currently selected supervisor.
     * @param e employee to be given a supervisor
     */
    public void assignEmployeeToSup(Employee e){
        e.setEmpSupId(selectedSup.getEmpId());
        employeeManager.merge(e);
    }
    
    /**
     * Removes given employees supervisor
     * @param e Employee to be acted upon
     * @return null to refresh the page
     */
    public String removeEmpFromSup(Employee e){
        e.setEmpSupId(null);
        employeeManager.merge(e);
        return null;
    }
}
