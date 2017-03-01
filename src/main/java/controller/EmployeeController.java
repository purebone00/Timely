package controller;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;

import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;

@Named("Employee")
@Stateful
public class EmployeeController implements Serializable{
    @Inject private EmployeeManager employeeManager;
    @Inject private TimesheetManager tManager;
    
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
	        refreshTsrList(/*tsId.getTsEmpId(), tsId.getTsWkEnd()*/tsId);
	    }
	    return tsrList;
	}
	
	/**
	 * Uncomment and delete 'TimesheetId id' and uncommented code for query method.
	 * @author JoeFong
	 * @param id
	 */
	public void refreshTsrList(TimesheetId id) {
	    tsrList = tManager.find(tsId).getTsrow();
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
    	//Employee newEmp = new Employee(pwd, fnm, lnm, (short)0, null, null);
    	employeeManager.persist(emp); 
    }
    
    
    public void refreshList() {
        list = employeeManager.getAll();
    }
}
