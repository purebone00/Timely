package controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.LabourGradeManager;
import model.Employee;
import model.Labgrd;

@RequestScoped
@Named("Admin")
public class AdminController {
	@Inject private EmployeeManager employeeManager;

	@Inject private Employee newEmployee;
	
	@Inject private EmployeeController employeeController;
	
	@Inject private LabourGradeManager labourGradeManager;
	
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
    	employeeManager.persist(newEmployee);
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
        //return to current page
        return null;

    }
    
}
