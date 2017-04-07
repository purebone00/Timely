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
import manager.WorkPackageManager;
import model.Employee;
import model.Labgrd;
import model.Project;
import model.Workpack;
import model.WorkpackId;

@SuppressWarnings("serial")
@SessionScoped
@Named("Admin")
public class AdminController implements Serializable {
    
    public static final int FLEX_PROJ_NO = TimesheetApproverController.FLEX_PROJ_NO;
    public static final String FLEX_WP_NO = TimesheetApproverController.FLEX_WP_NO;
    
    @Inject
    private EmployeeManager employeeManager;

    @Inject
    private Employee newEmployee;

    @Inject
    private EmployeeController employeeController;

    @Inject
    private LabourGradeManager labourGradeManager;
    
    @Inject
    private ProjectManager projectManager;
    
    @Inject
    private WorkPackageManager workpackManager;

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

    public String editAction(Employee employee) {

        employee.setEditable(true);
        return null;
    }

    public String saveAction(Employee e) {

        e.setEditable(false);
        employeeManager.merge(e);
        employeeManager.flush();
        // return to current page
        return null;

    }

    /**
     * Deletes the employee
     * 
     * @param e
     *            the employee
     * @return navigation location
     */
    public String delete(Employee e) {

        employeeManager.delete(e);

        employeeManager.merge(e);
        // employeeManager.flush();

        // return to current page
        return null;
    }

    /**
     * Restores the employee so its no longer deleted
     * 
     * @param e
     *            the employee
     * @return navigation location
     */
    public String restore(Employee e) {

        employeeManager.restore(e);

        employeeManager.merge(e);
        // employeeManager.flush();

        // return to current page
        return null;
    }

}
