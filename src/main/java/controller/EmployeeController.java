package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
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
public class EmployeeController implements Serializable{
    @Inject private EmployeeManager employeeManager;
    @Inject private TsrowManager tsrManager;
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
	private List<Timesheet> tsList;

    public List<Timesheet> getTsList() {
	    if(tsList == null) {
	        refreshTsList();
	    }
	    return tsList;
	}
	
	public void refreshTsList() {
	    tsList = tManager.getAll(emp.getEmpId());
	}
    
	public List<Tsrow> getTsrList() {
	    if(tsrList == null) {
	        refreshTsrList(tsId.getTsEmpId(), tsId.getTsWkEnd());
	    }
	    return tsrList;
	}
	
	public void refreshTsrList(int employeeNumber, String wkEnd) {
	    tsrList = tsrManager.getAllForTable(employeeNumber, wkEnd);
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
