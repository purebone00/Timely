package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import model.Employee;

@Named("Employee")
@SessionScoped
public class EmployeeBean implements Serializable{
    @Inject private EmployeeManager employeeManager;
    
    private List<Employee> list;
    
    public List<Employee> getList() {
        if(list == null) {
            refreshList();
        }
        return list;
    }
    
    public boolean find(int id) {
        Employee employee;
        employee = employeeManager.find(id);
        return (employee!=null)? true:false;
    }
    
    private void refreshList() {
        Employee[] employee = employeeManager.getAll();
        list = new ArrayList<Employee>(Arrays.asList(employee));
    }
    
    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }
}
