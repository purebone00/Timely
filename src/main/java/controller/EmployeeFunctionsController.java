package controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import EditableTimesheets.EditableTimesheet;
import manager.TimesheetManager;

@Named
@RequestScoped
public class EmployeeFunctionsController {

    @Inject TimesheetManager timeManager;
    
    EditableTimesheet currentTimesheet;
    
}
