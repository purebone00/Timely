package frontend;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import model.*;

/**
 * Contains the current employee and their profile.
 * @author Timely
 *
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class EmployeeProfile implements Serializable {

    /**
     * Current employee.
     */
    Employee employee;

    /**
     * Titles of current employee.
     */
    List<Title> employeeTitles;

    /**
     * Gets the current employee.
     * @return
     */
    public Employee getCurrentEmployee() {
        return employee;
    }

    /**
     * Sets the current employee.
     * @param currentEmployee
     */
    public void setCurrentEmployee(Employee currentEmployee) {
        this.employee = currentEmployee;
    }

    /**
     * Get list of employee titles.
     * @return
     */
    public List<Title> getEmployeeTitles() {
        return employeeTitles;
    }

    /**
     * Set list of employee titles.
     * @param employeeTitles
     */
    public void setEmployeeTitles(List<Title> employeeTitles) {
        this.employeeTitles = employeeTitles;
    }

}
