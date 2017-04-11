package controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import frontend.EmployeeProfile;
import manager.EmployeeManager;
import model.Employee;
import model.Emptitle;
import model.Title;

/**
 * Checks whether inputted username and password combination is valid. 
 * Session begins if valid. Sends user to admin panel if the user is the admin.
 */
@SuppressWarnings("serial")
@Named("Login")
@RequestScoped
public class LoginController implements Serializable {
	/**
	 * Used for accessing employee data in database (Employee table).
	 */
    @Inject
    EmployeeManager empManager;
    /**
     * Represents employee that has successfully logged in.
     */
    @Inject
    EmployeeProfile currentEmployee;
    /**
     * Username entered during login attempt.
     */
    private Integer userName;
    /**
     * Password entered during login attempt.
     * @HasGetter
     * @HasSetter
     */
    private String password;
    /**
     * List of all employees in employee roster.
     * @HasGetter
     * @HasSetter
     */
    private List<Employee> list;
    /**
     * Returns map containing all employees. Key = last name. Value = employee object.
     * @return Map<String,Employee> map of employee objects indexed by last name.
     */
    private Map<Integer, Employee> empMap;
    /**
     * Not used.
     */
    private boolean validationComplete = false;

    public LoginController() {

    }
    /**
     * Populates list of employees and iterates through them to check 
     * username and password provided. Returns authenticated employee or null.
     */
    public Employee authUser() {
        empMap = empManager.getActiveEmpMap();
        boolean authenticated = false;

        Employee employee = empMap.get(userName);
        if (employee == null)
        	return null;
        if (employee.getEmpPw().equals(password)) {
            currentEmployee.setCurrentEmployee(employee);
            if (currentEmployee.getCurrentEmployee() != null)
                authenticated = true;
        }
               
        
        return (authenticated) ? currentEmployee.getCurrentEmployee() : null;
    }
    
    

    public Integer getUserName() {
        return userName;
    }

    public void setUserName(Integer userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Employee> getList() {
        return list;
    }

    public void setList(List<Employee> newList) {
        this.list = newList;
    }
    /**
     * Returns whether the logged in employee is an admin.
     * @return boolean true if the current employee is admin. False if otherwise.
     */
    public boolean isAdmin() {
        return (currentEmployee.getCurrentEmployee().getEmpId().intValue() == 1
                || currentEmployee.getCurrentEmployee().getEmpId().intValue() == 2);
    }

    public EmployeeProfile getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(EmployeeProfile currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    
}
