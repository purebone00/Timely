package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import manager.EmployeeManager;
import manager.TimesheetManager;
import model.Employee;
import model.Timesheet;


@Named("taApprover")
public class TimesheetApproverController implements Serializable {
    
    @Inject TimesheetManager tManager;
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

    public Set<Timesheet> getTsToApproveList() throws IOException {
        if (tsToApproveList == null) {
            refreshTsToApproveList();
        }
        return tsToApproveList;
    }

    public void setTsToApproveList(Set<Timesheet> tsToApproveList) {
        this.tsToApproveList = tsToApproveList;
    }

    public void refreshTsToApproveList() throws IOException {
        try {
            tsToApproveList = empManager.find(getEmp().getEmpId()).getTimesheetsToApprove();    
        } catch (NullPointerException e) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/error.xhtml");
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
    
    public String approveAllTimesheet() {
        for (Timesheet t: tsToApproveList) {
            if(t.getIsApprove() == true) {
                t.setTsApprDt(new Date());
                t.setTsApprId(emp.getEmpId());
                tManager.merge(t);
                tsToApproveList.remove(t);
            }
        }
        return "success";
    }

}
