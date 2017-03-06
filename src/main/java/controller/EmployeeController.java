package controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;
import manager.TsrowManager;
import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;


@Named("Employee")
@Stateful
public class EmployeeController implements Serializable{
    @Inject private EmployeeManager employeeManager;
    @Inject private TimesheetManager tManager;
    @Inject private TsrowManager trManager;
    @Inject private Employee emp;
    
    private TimesheetId tsId;
    
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
	
	private List<Tsrow> tsrList;
	private List<Employee> list;
	private Set<Timesheet> tsList;
	private Timesheet curTimesheet; 
	private int weekNumber;
	
	
	
    public Timesheet getCurTimesheet() {
        curTimesheet = tManager.find(tsId);
        return curTimesheet;
    }
    public void setCurTimesheet(Timesheet curTimesheet) {
        this.curTimesheet = curTimesheet;
    }
    public Set<Timesheet> getTsList() {
	    if(tsList == null) {
	        refreshTsList();
	    }
	    return tsList;
	}
	
	public void refreshTsList() {
	     tsList = employeeManager.find(getEmp().getEmpId()).getTimesheet();    
	}
    
	public List<Tsrow> getTsrList() {
	    if(tsrList == null) {
	        refreshTsrList();
	    }
	    return tsrList;
	}	

	public void refreshTsrList() {
	    int remainder = 0;
	    tsrList = tManager.find(tsId).getTsrow();
	    if(tsrList.size() < 5) {
	        int size = tsrList.size();
	        remainder = 5 - size;
	        for(int i = 0; i < remainder; i++) {
	            Tsrow row = new Tsrow();
	            tsrList.add(row);
	        }
	    }
	}
	
    public List<Employee> getList() {
        if(list == null) {
            refreshList();
        }
        return list;
    }
    
    public Employee find(int id) {
        return employeeManager.find(id);
    }
    public void add(){
    	employeeManager.persist(emp); 
    }
    
    public void refreshList() {
        list = employeeManager.getAll();
    }
    
    
    public String editAction() {
        for( Tsrow row: tsrList) {
            row.setEditable(true);
        }
        
        return null;
    }
    
    public String saveAction() {
        for (Tsrow row : tsrList){
            row.setEditable(false);
            if(row.getTsrProjNo() != 0 && row.getTsrWpNo() != null) {
                trManager.merge(row);
            }
        }
        return null;
    }
    
    public String addTsrow() {
        tsrList.add(new Tsrow());
        return null;
    }
    
    public int getWeekNumber() throws ParseException {
        String input = getCurTimesheet().getId().getTsWkEnd();
        String format = "YYYYMMDD";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(input);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        weekNumber = week;
        return weekNumber;
    }
    
    
    
}
