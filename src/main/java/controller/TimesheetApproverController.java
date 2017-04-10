package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateful;
import javax.faces.application.ViewExpiredException;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;
import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;

/**
 * Contains methods used by timesheet approvers. 
 */
@SuppressWarnings("serial")
@Stateful
@Named("taApprover")
public class TimesheetApproverController implements Serializable {
	/**
	 * Project that contains SICK/VACATION/FLEX work packages.
	 */
    public static final int FLEX_PROJ_NO = 1111;
    /**
     * Work package id for flex time.
     */
    public static final String FLEX_WP_NO = "__FLEX";

    /**
     * Used for accessing timesheet data in database (Timesheet table).
     */
    @Inject
    TimesheetManager tManager;
    /**
     * Used for accessing employee row data in database (Employee table).
     */
    @Inject
    private EmployeeManager empManager;
    /**
     * Represents employee whose information is being altered.
     */
    @Inject
    private Employee emp;
    /**
     * Collection of timesheets to review.
     */
    private Set<Timesheet> listOfts;
    /**
     * Represents the timesheet currently being reviewed.
     */
    private Timesheet reviewTimesheet;
    /**
     * Represents the employee who owns the timesheet currently being reviewed.
     */
    private Employee employeeReviewed;
    /**
     * Collection of timesheets that have been approved.
     */
    private Set<Timesheet> listOfApproved;
    /**
     * Collection of timesheets that have not been approved yet. 
     */
    private Set<Timesheet> listToBeApproved;

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }
    /**
     * 
     */
    public Set<Timesheet> getListOfApproved() {
        if (listOfApproved == null) {
            refreshApprovedList();
        }
        return listOfApproved;
    }
    /**
     * 
     */
    public void setListOfApproved(Set<Timesheet> listOfApproved) {
        this.listOfApproved = listOfApproved;
    }
    /**
     * 
     */
    public void refreshApprovedList() {
        Set<Timesheet> temp = new HashSet<>();
        listOfts.forEach((ts) -> {
            if (tManager.find(ts.getId()).getTsSubmit() == 2) {
                temp.add(ts);
            }
        });
        listOfApproved = temp;
    }
    /**
     * 
     */
    public Set<Timesheet> getListToBeApproved() {
        if (listToBeApproved == null) {
            refreshToBeApprovedList();
        }
        return listToBeApproved;
    }
    /**
     * 
     */
    public void setListToBeApproved(Set<Timesheet> listToBeApproved) {
        this.listToBeApproved = listToBeApproved;
    }
    /**
     * 
     */
    public void refreshToBeApprovedList() {

        Set<Timesheet> temp = new HashSet<>();
        listOfts.forEach((ts) -> {
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

    /**
     * Retrieves list of timesheets the approver has yet to review. 
     */
    public void refreshList() {
        try {
            listOfts = empManager.find(getEmp().getEmpId()).getTimesheetsToApprove();
        } catch (NullPointerException e) {
            listOfts = new HashSet<Timesheet>();
        }
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
    /**
     * Method for bulk approving of timesheets.
     * @return String navigation string
     */
    public String approveAllTimesheet() {
        for (Timesheet t : listOfts) {
            if (t.getIsApprove() == true) {
                t.setTsApprDt(new Date());
                t.setTsApprover(emp);
                //t.setTsApprId(emp.getEmpId());
                tManager.merge(t);
                listOfts.remove(t);
            }
        }
        return "success";
    }

    public String accept() {
        tManager.find(this.getReviewTimesheet().getId()).setTsSubmit((short) 2);
        processFlextime();
        refreshApprovedList();
        refreshToBeApprovedList();
        return "approver";
    }

    public String reject() {
        tManager.find(this.getReviewTimesheet().getId()).setTsSubmit((short) 3);
        refreshApprovedList();
        refreshToBeApprovedList();
        return "approver";
    }
    
    private void processFlextime() {
        Timesheet t = tManager.find(this.getReviewTimesheet().getId());
        Employee e = t.getEmployee();
        BigDecimal flextimeAdd = t.getTsFlexTm() == null ? BigDecimal.ZERO : t.getTsFlexTm();
        BigDecimal flextimeSub = BigDecimal.ZERO;
        
        for (Tsrow ts : t.getTsrow()) {
            if (ts.getTsrProjNo() == FLEX_PROJ_NO && ts.getTsrWpNo().equals(FLEX_WP_NO)) {
                flextimeSub = ts.getTotal();
                break;
            }
        }
        
        BigDecimal curFlextime = e.getEmpFlxTm() == null ? BigDecimal.ZERO : e.getEmpFlxTm();
        e.setEmpFlxTm(curFlextime.add(flextimeAdd).subtract(flextimeSub));
        empManager.merge(e);
    }
 
}
