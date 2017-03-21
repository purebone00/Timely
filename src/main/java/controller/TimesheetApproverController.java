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

@Stateful
@Named("taApprover")
public class TimesheetApproverController implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8723895938632264068L;

    @Inject
    private EmployeeManager empManager;
    @Inject
    private Employee emp;

    private Set<Timesheet> tsToApproveList;
    private Timesheet reviewTimesheet;
    private Employee employeeReviewed;

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public Employee getEmp() {
        return emp;
    }

    public Set<Timesheet> getTsToApproveList() {
        if (tsToApproveList == null) {
            refreshTsToApproveList();
        }
        return tsToApproveList;
    }

    public void setTsToApproveList(Set<Timesheet> tsToApproveList) {
        this.tsToApproveList = tsToApproveList;
    }

    public void refreshTsToApproveList() {
        tsToApproveList = empManager.find(getEmp().getEmpId()).getTimesheetsToApprove();
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

    public void setEmployeeReviewed(int employeeReviewed) {
        this.employeeReviewed = empManager.find(employeeReviewed);
    }

    public String goToReviewTimesheet(Timesheet selectedTimesheet) {
        this.setReviewTimesheet(selectedTimesheet);
        return "review";
    }

}
