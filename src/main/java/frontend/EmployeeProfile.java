package frontend;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import model.*;

@Named
@SessionScoped
public class EmployeeProfile implements Serializable {

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
