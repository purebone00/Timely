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

@SuppressWarnings("serial")
@Named("Login")
@RequestScoped
public class LoginController implements Serializable {
    @Inject
    EmployeeManager empManager;

    @Inject
    EmployeeProfile currentEmployee;

    private String userName;

    private String password;

    private List<Employee> list;

    private Map<String, Employee> empMap;

    public LoginController() {

    }

    public Employee authUser() {
        empMap = empManager.getActiveEmpMap();
        boolean authenticated = false;

        Employee employee = empMap.get(userName);
        if (employee.getEmpPw().equals(password)) {
            currentEmployee.setCurrentEmployee(employee);
            if (currentEmployee.getCurrentEmployee() != null)
                authenticated = true;
        }
               
        
        return (authenticated) ? currentEmployee.getCurrentEmployee() : null;
    }
    
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
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
