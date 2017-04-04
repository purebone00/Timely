package frontend;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;


import controller.AdminController;
import controller.EmployeeController;
import controller.LoginController;
import controller.ProjectManagerController;
import controller.ResponsibleEngineerController;
import manager.EmployeeManager;
import controller.SupervisorController;
import controller.TimesheetApproverController;
import model.Employee;
import model.Timesheet;
import utility.SessionUtils;

@Named("Master")
@SessionScoped

public class FrontEndBoundary implements Serializable {
  
    @Inject
    LoginController login;

    @Inject
    ResponsibleEngineerController resEng;
    @Inject
    AdminController admin;
    @Inject
    EmployeeController employee;
    @Inject
    ProjectManagerController projMan;
    @Inject
    TimesheetApproverController taApprover;
    @Inject
    private EmployeeManager employeeManager;
    @Inject
    SupervisorController supMan;

    public SupervisorController getSupMan() {
        return supMan;
    }

    public void setSupMan(SupervisorController supMan) {
        this.supMan = supMan;
    }

    public LoginController getLogin() {
        return login;
    }

    public EmployeeController getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeController employee) {
        this.employee = employee;
    }

    public void setLogin(LoginController login) {
        this.login = login;
    }

    public ResponsibleEngineerController getResEng() {
        return resEng;
    }

    public void setResEng(ResponsibleEngineerController resEng) {
        this.resEng = resEng;
    }

    public AdminController getAdmin() {
        return admin;
    }

    public void setAdmin(AdminController admin) {
        this.admin = admin;
    }

    public ProjectManagerController getProjMan() {
        return projMan;
    }

    public void setProjMan(ProjectManagerController projMan) {
        this.projMan = projMan;
    }

    public TimesheetApproverController getTaApprover() {
        return taApprover;
    }

    public void setTaApprover(TimesheetApproverController taApprover) {
        this.taApprover = taApprover;
    }

    public void finish() throws IOException {
        employee.setEmp(null);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/");
        
    }

    public FrontEndBoundary() {

    }

    public String authenticate() {
        Employee curEmp;
        if ((curEmp = login.authUser()) != null) {
            employee.setEmp(curEmp);
            taApprover.setEmp(curEmp);
            if (login.isAdmin()) {
                return "admin";
            }

            return "login";
        }
        return "fail";
    }

    public String goToTimesheet(String wkEnd) {
        employee.setTsId(employee.getEmp().getEmpId(), wkEnd);
        return "timesheet";
    }

    public String logout() throws IOException {
        finish();
        return "logout";
    }

    public String goToChangePassword() {
        return "changepassword";
    }

    public void generateAllFeatures() {
    }

    public String changePassword() throws IOException {
        Employee emp = employee.getEmp();
        Integer empChNo = emp.getEmpChNo();
        String newPassword = emp.getNewPassword();
        String currentPassword = emp.getOldPassword();
        String confirmNewPassword = emp.getNewPasswordConfirm();
        String failed = "failedPasswordCheck";

        if (empChNo.intValue() != emp.getEmpId().intValue())
            return failed;
        if (!confirmNewPassword.equals(newPassword))
            return failed;
        if (!currentPassword.equals(emp.getEmpPw()))
            return failed;

        emp.setEmpPw(newPassword);
        employeeManager.merge(emp);
        finish();

        return "success";
    }
    
    public String getNotifications() {
        Integer tsApproveCount = null;
        try {
            tsApproveCount = taApprover.getTsToApproveList().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return tsApproveCount.toString();
    }

}
