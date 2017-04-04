package frontend;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
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
    
/*    @Inject
    Conversation conversation;
*/
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

/*    public void start() {
        conversation.begin();
    }

    public void end() {
        conversation.end();
    }

    public void init() {
        start();
    }
*/
    public String finish() {
        //HttpSession session = SessionUtils.getSession();
        //session.invalidate();
        return "logout";
    }

    public FrontEndBoundary() {

    }

    public String authenticate() {
        Employee curEmp;
        if ((curEmp = login.authUser()) != null) {
            employee.setEmp(curEmp);
            taApprover.setEmp(curEmp);
            //init();
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", employee.getEmp().getEmpLnm());
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

    public String logout() {
        finish();
        return "logout";
    }

    public String goToChangePassword() {
        return "changepassword";
    }

    public void generateAllFeatures() {
    }

    public String changePassword() {
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
        Integer tsApproveCount = taApprover.getTsToApproveList().size();
        
        return tsApproveCount.toString();
    }

}
