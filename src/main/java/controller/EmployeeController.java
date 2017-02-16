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
public class EmployeeController implements Serializable{
    @Inject private EmployeeManager employeeManager;
    
    private List<Employee> list;
    
    public List<Employee> getList() {
        if(list == null) {
            refreshList();
        }
        return list;
    }
    
    public Employee find(int id) {
        return employeeManager.find(id);
    }
    
    private void refreshList() {
        list = employeeManager.getAll();
    }
}