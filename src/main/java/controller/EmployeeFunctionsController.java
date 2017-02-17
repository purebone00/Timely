package controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.TimesheetManager;
import model.Employee;
import timesheetUtility.EditableTimesheet;
import timesheetUtility.TimesheetComparer;

@Named
@RequestScoped
public class EmployeeFunctionsController implements Serializable {

    @Inject TimesheetManager timeManager;
    
    EditableTimesheet currentTimesheet;
    
    TimesheetComparer tsComparer = new TimesheetComparer();
    

    public EditableTimesheet getCurrentTimesheet() {
        return currentTimesheet;
    }

    public void setCurrentTimesheet(EditableTimesheet currentTimesheet) {
        this.currentTimesheet = currentTimesheet;
    }

    public List<EditableTimesheet> listOfTimesheets(Employee emp) {
        List <EditableTimesheet> lsTimesheet = timeManager.findbyEmpId(emp.getEmpId());
        
        Collections.sort(lsTimesheet, tsComparer);
        Collections.reverse(lsTimesheet);
        
        return lsTimesheet;
    }
    
    public String createNew(Integer employeeId) {
        EditableTimesheet newTimesheet = new EditableTimesheet();
        newTimesheet.setEmpId(employeeId.intValue());
        newTimesheet.setRows(newTimesheet.createNewRows(5));
        
        return (goTo(newTimesheet));
    }
    
    public String goTo(EditableTimesheet selectedTimesheet) {
        setCurrentTimesheet(selectedTimesheet);
        return "timesheet";
    }
}
