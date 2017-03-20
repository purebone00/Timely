package test.timely.controller;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.junit.Test;

import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;
import utility.DateTimeUtility;

public class EmployeeControllerTest {
	
	private static Logger log = Logger.getLogger("ErrorLogger");
	 
	 @Test
	    public void createTimesheet() throws Exception {
		 
		 Timesheet ts = new Timesheet();
		 TimesheetId tsId = new TimesheetId(5,"friday");
		 ts.setId(tsId);
		 DateTimeUtility dtu = new DateTimeUtility();
		 assertNotNull(ts);
		 log.info("Timesheet not null");
	    }
	 
	 @Test
	    public void TimesheetEmpId() throws Exception {
		 
		 Timesheet ts = new Timesheet();
		 TimesheetId tsId = new TimesheetId(5,"friday");
		 ts.setId(tsId);
		 DateTimeUtility dtu = new DateTimeUtility();
		 assertEquals(5, tsId.getTsEmpId());
		 log.info("Emp id returend was: " + tsId.getTsEmpId());
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
