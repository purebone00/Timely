package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.TimesheetManager;
import model.Employee;
import timesheetUtility.EditableTimesheet;

@Named
@RequestScoped
public class EmployeeFunctionsController implements Serializable {

    @Inject TimesheetManager timeManager;
    
    EditableTimesheet currentTimesheet;
    

    public EditableTimesheet getCurrentTimesheet() {
        return currentTimesheet;
    }

    public void setCurrentTimesheet(EditableTimesheet currentTimesheet) {
        this.currentTimesheet = currentTimesheet;
    }

    public List<EditableTimesheet> listOfTimesheets(Employee emp) {
        return timeManager.findbyEmpId(emp.getEmpId());
    }
}
