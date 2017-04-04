package controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;
import model.Employee;
import model.Timesheet;

@Stateful
@Named("taApprover")
public class TimesheetApproverController implements Serializable {

    @Inject
    TimesheetManager tManager;
    @Inject
    private EmployeeManager empManager;
    @Inject
    private Employee emp;

    private Set<Timesheet> listOfts;
    private Timesheet reviewTimesheet;
    private Employee employeeReviewed;
    private Set<Timesheet> listOfApproved;
    private Set<Timesheet> listToBeApproved;

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }


    public Set<Timesheet> getListOfApproved() {
        if (listOfApproved == null) {
            refreshApprovedList();
        }
        return listOfApproved;
    }

    public void setListOfApproved(Set<Timesheet> listOfApproved) {
        this.listOfApproved = listOfApproved;
    }
    
    public void refreshApprovedList() {
        Set<Timesheet> temp = new HashSet<>();
        listOfts.forEach((ts)->{
            if (tManager.find(ts.getId()).getTsSubmit() == 2) {
                temp.add(ts);
            }
        });
        listOfApproved = temp;
    }

    public Set<Timesheet> getListToBeApproved() {
        if (listToBeApproved == null) {
            refreshToBeApprovedList();
        }
        return listToBeApproved;
    }

    public void setListToBeApproved(Set<Timesheet> listToBeApproved) {
        this.listToBeApproved = listToBeApproved;
    }
    
    public void refreshToBeApprovedList() {
        Set<Timesheet> temp = new HashSet<>();
        listOfts.forEach((ts)->{
            if (tManager.find(ts.getId()).getTsSubmit() == 1) {
                temp.add(ts);
            }
        });
        listToBeApproved = temp;
    }

    public Set<Timesheet> getListOfts() {
        if (listOfts == null) {
            refreshList();
        }
        return listOfts;
    }

    public void setListOfts(Set<Timesheet> tsToApproveList) {
        this.listOfts = tsToApproveList;
    }

    public void refreshList() {
        listOfts = empManager.find(getEmp().getEmpId()).getTimesheetsToApprove();
    }

    public Timesheet getReviewTimesheet() {
        return reviewTimesheet;
    }

    public void setReviewTimesheet(Timesheet reviewTimesheet) {
        this.reviewTimesheet = reviewTimesheet;
    }

    public Employee getEmployeeReviewed() {
        return employeeReviewed;
    }

    public void setEmployeeReviewed(Employee e) {
        this.employeeReviewed = e;
    }

    public String goToReviewTimesheet(Timesheet selectedTimesheet) {
        this.setReviewTimesheet(selectedTimesheet);
        this.setEmployeeReviewed(selectedTimesheet.getEmployee());
        return "review";
    }
    
    public String viewTimesheet(Timesheet selectedTimesheet) {
        this.setReviewTimesheet(selectedTimesheet);
        this.setEmployeeReviewed(selectedTimesheet.getEmployee());
        return "view";
    }

    public String approveAllTimesheet() {
        for (Timesheet t : listOfts) {
            if (t.getIsApprove() == true) {
                t.setTsApprDt(new Date());
                t.setTsApprId(emp.getEmpId());
                tManager.merge(t);
                listOfts.remove(t);
            }
        }
        return "success";
    }
    
    public String accept() {
        tManager.find(this.getReviewTimesheet().getId()).setTsSubmit((short)2);
        refreshApprovedList();
        refreshToBeApprovedList();
        return "approver";
    }
    
    public String reject() {
        tManager.find(this.getReviewTimesheet().getId()).setTsSubmit((short)3);
        refreshApprovedList();
        refreshToBeApprovedList();
        return "approver";
    }

}
