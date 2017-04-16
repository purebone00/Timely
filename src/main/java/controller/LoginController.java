package controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import frontend.EmployeeProfile;
import manager.EmployeeManager;
import model.Employee;

/**
 * Checks whether inputted username and password combination is valid. Session
 * begins if valid. Sends user to admin panel if the user is the admin.
 * @author Timely
 * @version 1.0
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
     * 
     * @HasGetter
     * @HasSetter
     */
    private String password;
    /**
     * List of all employees in employee roster.
     * 
     * @HasGetter
     * @HasSetter
     */
    private List<Employee> list;
    /**
     * Returns map containing all employees. Key = last name. Value = employee
     * object.
     * 
     * @return Map<String,Employee> map of employee objects indexed by last
     *         name.
     */
    private Map<Integer, Employee> empMap;

    /**
     * Default ctor.
     */
    public LoginController() {

    }

    /**
     * Populates list of employees and iterates through them to check username
     * and password provided. Returns authenticated employee or null.
     * @return authenticated employee or null
     */
    public Employee authUser() {
        empMap = empManager.getActiveEmpMap();
        boolean authenticated = false;

        Employee employee = empMap.get(userName);
        if (employee == null) {            
            return null;
        }
        if (employee.getEmpPw().equals(password)) {
            currentEmployee.setCurrentEmployee(employee);
            if (currentEmployee.getCurrentEmployee() != null) {                
                authenticated = true;
            }
        }
        return (authenticated) ? currentEmployee.getCurrentEmployee() : null;
    }

    /**
     * Get userName.
     * @return userName
     */
    public Integer getUserName() {
        return userName;
    }

    /**
     * Set userName.
     * @param userName userName
     */
    public void setUserName(Integer userName) {
        this.userName = userName;
    }

    /**
     * Get password.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password.
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get list.
     * @return list
     */
    public List<Employee> getList() {
        return list;
    }

    /**
     * Set list.
     * @param newList list
     */
    public void setList(List<Employee> newList) {
        this.list = newList;
    }

    /**
     * Returns whether the logged in employee is an admin.
     * 
     * @return boolean true if the current employee is admin. False if
     *         otherwise.
     */
    public boolean isAdmin() {
        return (currentEmployee.getCurrentEmployee().getEmpId().intValue() == 1
                || currentEmployee.getCurrentEmployee().getEmpId().intValue() == 2);
    }

    /**
     * Current employee profile.
     * 
     * @return currentEmployee
     */
    public EmployeeProfile getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Set current employee profile.
     * 
     * @param currentEmployee currentEmployee
     */
    public void setCurrentEmployee(EmployeeProfile currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

}
