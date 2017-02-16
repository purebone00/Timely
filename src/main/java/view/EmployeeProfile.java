package view;

import java.util.List;

import javax.inject.Named;

import model.*;

@Named
public class EmployeeProfile {

    Employee employee;
    
    List<Title> employeeTitles;

    public Employee getCurrentEmployee() {
        return employee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.employee = currentEmployee;
    }

    public List<Title> getEmployeeTitles() {
        return employeeTitles;
    }

    public void setEmployeeTitles(List<Title> employeeTitles) {
        this.employeeTitles = employeeTitles;
    }
    
    
}
