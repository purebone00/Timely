package test.timely.controller;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Test;

import controller.AdminController;
import manager.LabourGradeManager;
import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;
import utility.DateTimeUtility;

public class AdminControllerTest {
	
	private static Logger log = Logger.getLogger("ErrorLogger");
	 
	 @Test
	    public void getLabourGrades() throws Exception {
		 LabourGradeManager lg = new LabourGradeManager();
		 assertNotNull(lg.getAll());
		 log.info("Labor Grades");
	    }
	 
	 @Test
	    public void editAction() throws Exception {
		 
		Employee e = new Employee();
		e.setFullName("dave");
		AdminController ac = new AdminController();
		ac.editAction(e);
		assertNull(ac.editAction(e));
		 log.info(ac.editAction(e));
	    }
	 
	 
	 @Test
	    public void TimesheetEmpEndOfWeek() throws Exception {
		 
		 Timesheet ts = new Timesheet();
		 TimesheetId tsId = new TimesheetId(5,"friday");
		 ts.setId(tsId);
		 DateTimeUtility dtu = new DateTimeUtility();
		 assertEquals("friday", tsId.getTsWkEnd());
		 log.info("Day of week is: " + tsId.getTsWkEnd());
	    }
	 
	 
}
