package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;
import manager.TsrowManager;
import model.Employee;
import model.Project;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;
import utility.DateTimeUtility;

/**
 * Contains methods used by employees to interact with their timesheets.
 */
@SuppressWarnings("serial")
@Named("Employee")
@Stateful
public class EmployeeController implements Serializable {
	/**
     * Used for accessing employee data in database (Employee table).  
	 */
    @Inject
    private EmployeeManager employeeManager;
	/**
     * Used for accessing timesheet data in database (Timesheet table).  
	 */
    @Inject
    private TimesheetManager tManager;
	/**
     * Used for accessing timesheet row data in database (Tsrow table).  
	 */
    @Inject
    private TsrowManager trManager;
	/**
     * Represents employee whose information is being altered (currently-logged in employee).
     * @HasGetter
     * @HasSetter  
	 */
    @Inject
    private Employee emp;

    /**
     * Id of the timesheet currently being viewed.
     * @HasGetter
     * @HasSetter  
     */
    private TimesheetId tsId;
    /**
     * Collection of timesheet rows in the current timesheet.
     * @HasGetter
     * @HasSetter  
     */
    private Set<Tsrow> tsrList;
    /**
     * List of all employees. Non-descriptive name.
     * @HasGetter    
     */
    private List<Employee> list;
    /**
     * List of all timesheets belonging to the employee.
     * @HasGetter  
     */
    private Set<Timesheet> tsList;
    /**
     * Represents current week's timesheet.
     * @HasGetter
     * @HasSetter
     */
    private Timesheet curTimesheet;
    /**
     * The current week's number.
     */
    private int weekNumber;

    /**
     * The amount of overtime charged to the timesheet.
     * @HasGetter
     * @HasSetter
     */
    private BigDecimal overtime;
    /**
     * The amount of flextime charged to the timesheet.
     * @HasGetter
     * @HasSetter
     */
    private BigDecimal flextime;
    /**
     * Whether or not overtime may be edited in timesheet. 
     * @HasGetter
     * @HasSetter
     */
    private boolean overtimeEditable;

    /**
     * For determining whether or not button for adding a new timesheet row is displayed.
     * Returns true (button is rendered in view) if  
     */
    public boolean showAddButton() {
        try {
            return (tManager.find(getNewTsId()) != null) ? false : true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public TimesheetId getTsId() {
        return tsId;
    }

    public void setTsId(Integer empId, String wkend) {
        this.tsId = new TimesheetId(empId, wkend);
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }
    /**
     * Number of columns for tracking hours worked per package.
     * (one for each day of the week plus one for the sum of hours worked in the week). 
     */
    private static int DAYS_IN_WEEK_AND_TOTAL = 8;
    /**
     * Total amount of hours charged per day over all work packages.
     * @HasGetter
     * @HasSetter
     */
    private BigDecimal[] dailyTotals;
    
    public BigDecimal[] getDailyTotals() {
        dailyTotals = new BigDecimal[DAYS_IN_WEEK_AND_TOTAL];
        for (int i = 0; i < dailyTotals.length; i++) {
            dailyTotals[i] = new BigDecimal(0);
        }
        for (Tsrow row : tsrList) {
            if (row.getTsrMon() != null) {
                dailyTotals[0] = dailyTotals[0].add(new BigDecimal(row.getTsrMon().doubleValue()));
            }
            if (row.getTsrTue() != null) {
                dailyTotals[1] = dailyTotals[1].add(new BigDecimal(row.getTsrTue().doubleValue()));
            }
            if (row.getTsrWed() != null) {
                dailyTotals[2] = dailyTotals[2].add(new BigDecimal(row.getTsrWed().doubleValue()));
            }
            if (row.getTsrThu() != null) {
                dailyTotals[3] = dailyTotals[3].add(new BigDecimal(row.getTsrThu().doubleValue()));
            }
            if (row.getTsrFri() != null) {
                dailyTotals[4] = dailyTotals[4].add(new BigDecimal(row.getTsrFri().doubleValue()));
            }
            if (row.getTsrSat() != null) {
                dailyTotals[5] = dailyTotals[5].add(new BigDecimal(row.getTsrSat().doubleValue()));
            }
            if (row.getTsrSun() != null) {
                dailyTotals[6] = dailyTotals[6].add(new BigDecimal(row.getTsrSun().doubleValue()));
            }
            if (row.getTotal() != null) {
                dailyTotals[7] = dailyTotals[7].add(new BigDecimal(row.getTotal().doubleValue()));
            }
        }
        return dailyTotals;
    }

    public void setDailyTotals(BigDecimal[] dailyTotals) {
        this.dailyTotals = dailyTotals;
    }

    public Timesheet getCurTimesheet() {
        curTimesheet = tManager.find(tsId);
        return curTimesheet;
    }

    public void setCurTimesheet(Timesheet curTimesheet) {
        this.curTimesheet = curTimesheet;
    }

    public Set<Timesheet> getTsList() {
        if (tsList == null) {
            refreshTsList();
        }
        return tsList;
    }
    /**
     * Retrieves updated list of all timesheets belonging to the employee.
     */
    public void refreshTsList() {

        try {
            tsList = employeeManager.find(getEmp().getEmpId()).getTimesheet();
        } catch (NullPointerException e) {
            tsrList = new HashSet<Tsrow>();
        }
    }

    public Set<Tsrow> getTsrList() {
        tsrList = refreshTsrList(tsrList, curTimesheet.getId());
        overtime = curTimesheet.getTsOverTm() == null ? null : curTimesheet.getTsOverTm();
        flextime = curTimesheet.getTsFlexTm() == null ? null : curTimesheet.getTsFlexTm();
        return tsrList;
    }
    /**
     * Retrieves current list of all timesheet rows belonging to a given timesheet.
     * @param tsrList the updated timesheet row list
     * @param id ID of the timesheet whose rows are being fetched
     */
    public Set<Tsrow> refreshTsrList(Set<Tsrow> tsrList, TimesheetId id) {
        int remainder = 0;

        // commented this out because it was causing tsrows to never
        // refresh, it would always be set to the first timesheet
        // viewed.
                if (tsrList != null)
                    return tsrList;

        tsrList = tManager.find(id).getTsrow();

        if (tsrList.size() < 5) {
            int size = tsrList.size();
            remainder = 5 - size;
            for (int i = 0; i < remainder; i++) {
                Tsrow row = new Tsrow();
                row.setTsrEmpId(emp.getEmpId());
                tsrList.add(row);
            }
        }

        return tsrList;
    }
    /**
     * Returns employee with a given ID. 
     * @param id ID of employee to find.
     * @return Employee employee retrieved with the given ID.
     */
    public Employee find(int id) {
        return employeeManager.find(id);
    }

    /**
     * Adds a new employee to the employee roster.
     */
    public void add() {
        employeeManager.persist(emp);
    }

    public List<Employee> getList() {
        refreshList();

        return list;
    }

    /**
     * Retrieves updated list of all employees.
     */
    public void refreshList() {
        if (list == null)
            list = employeeManager.getAll();
    }
    /**
     * Generated editable form fields for making changes 
     * to a timesheet.
     * @return String null navigation string for refreshing the current page.
     */
    public String editAction() {
        for (Tsrow row : tsrList) {
            row.setEditable(true);
        }
        setOvertimeEditable(true);
        return null;
    }

    /**
     * Saves timesheet information inputted into form fields.
     * @return String navigation string that refreshes current page. 
     */
    public String saveAction() {
        for (Tsrow row : tsrList) {
            row.setEditable(false);
            row.setTimesheet(getCurTimesheet());
            if (row.getTsrProjNo() != 0 && !row.getTsrWpNo().isEmpty()) {
                trManager.merge(row);
            }
        }
        setOvertimeEditable(false);
        curTimesheet.setTsOverTm(overtime);
        curTimesheet.setTsFlexTm(flextime);
        tManager.merge(curTimesheet);
        return null;
    }
    /**
     * Adds a row to the current timesheet.
     */
    public String addTsrow() {
        Tsrow row = new Tsrow();
        row.setTsrEmpId(emp.getEmpId());
        tsrList.add(row);
        return null;
    }

    public int getWeekNumber() throws ParseException {
        String input = getCurTimesheet().getId().getTsWkEnd();
        String format = "yyyyMMdd";
        DateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(input);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.SATURDAY);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        weekNumber = week;
        System.out.println(weekNumber);
        return weekNumber;
    }

    /**
     * Saves information in a timesheet to database.
     * @return String navigation string for refreshing the current page.
     */
    public String submitTimesheet() {
        if (curTimesheet.getTsSubmit() == 0) {
            curTimesheet.setTsSubmit((short) 1);
            tManager.merge(curTimesheet);
        }
        return null;
    }
    /**
     * Returns a list containing IDs of all projects belonging to the employee. 
     * @return List<Integer> list of project IDs
     */
    public List<Integer> projectIntegerList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Project p : emp.getProjects()) {
            list.add(p.getProjNo());
        }
        return list;
    }
    /**
     * Generates a new timesheet id.
     * @return TimesheetId newly-generated timesheet id.
     */
    public TimesheetId getNewTsId() {
        DateTimeUtility dtu = new DateTimeUtility();
        return new TimesheetId(emp.getEmpId(), dtu.getEndOfWeek());
    }
    /**
     * Creates a new timesheet.
     * @return String navigation string for refreshing the current page.
     */
    public String createNewTimesheet() {

        Timesheet ts = new Timesheet();
        ts.setId(getNewTsId());
        ts.setTsDel((short) 0);
        ts.setTsSubmit((short) 0);
        ts.setTsPayGrade(emp.getEmpLabGrd());
        Set<Tsrow> tsrList = new HashSet<Tsrow>();
        tsrList = refreshTsrList(tsrList, tsId);
        ts.setTsrow(tsrList);
        tManager.persist(ts);
        tsList.add(ts);
        return null;
    }

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public BigDecimal getFlextime() {
        return flextime;
    }

    public void setFlextime(BigDecimal flextime) {
        this.flextime = flextime;
    }

    public boolean isOvertimeEditable() {
        return overtimeEditable;
    }

    public void setOvertimeEditable(boolean overtimeEditable) {
        this.overtimeEditable = overtimeEditable;
    }

}
