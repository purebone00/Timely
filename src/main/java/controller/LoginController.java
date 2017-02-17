package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import model.Employee;
import view.EmployeeProfile;

@SuppressWarnings("serial")
@Named("Login")
@RequestScoped
public class LoginController implements Serializable {
    @Inject EmployeeManager empManager;

    @Inject EmployeeProfile currentEmployee;
    
    private String userName;
    
    private String password;
    
    private List<Employee> list;
    
    public LoginController() {
        
    }
    
    public boolean authUser() {
        list = empManager.getAll();
        boolean authenticated = false;
        
        for(int i = 0; i < list.size(); i++) {
            Employee employee = list.get(i);
            if(employee.getEmpLnm().equals(userName) && employee.getEmpPw().equals(password)) {
                currentEmployee.setEmployee(empManager.find(employee.getEmpId()));
                if (currentEmployee.getEmployee() != null) {
                    authenticated = true;
                }
            }
        }
        return authenticated;
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
        return (currentEmployee.getEmployee().getEmpId().intValue() == 1);
    }

    public EmployeeProfile getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(EmployeeProfile currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    
}
