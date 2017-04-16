package frontend;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import controller.AdminController;
import controller.EmployeeController;
import controller.LoginController;
import controller.ProjectManagerController;
import controller.ResponsibleEngineerController;
import manager.EmployeeManager;
import controller.SupervisorController;
import controller.TimesheetApproverController;
import model.Employee;
import model.Project;
import model.Title;
import utility.models.MonthlyReport;

/**
 * Navigation cases and main interaction from front end to business logic.
 * 
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Named("Master")
@SessionScoped
public class FrontEndBoundary implements Serializable {
    /**
     * Holds login logic.
     */
    @Inject
    LoginController login;
    /**
     * Holds responsible engineer logic.
     */
    @Inject
    ResponsibleEngineerController resEng;
    /**
     * Holds admin logic.
     */
    @Inject
    AdminController admin;
    /**
     * Holds employee logic.
     */
    @Inject
    EmployeeController employee;
    /**
     * Holds project manager logic.
     */
    @Inject
    ProjectManagerController projMan;
    /**
     * Holds timesheet approver logic.
     */
    @Inject
    TimesheetApproverController taApprover;
    /**
     * Holds employee manager logic.
     */
    @Inject
    private EmployeeManager employeeManager;
    /**
     * Holds supervisor logic.
     */
    @Inject
    SupervisorController supMan;

    /**
     * Ends current session.
     * 
     * @return navigation string
     */
    public String finish() {
        employee.setEmp(null);

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        ec.invalidateSession();
        return "/login.xhtml?faces-redirect=true&expired=true";
    }

    /**
     * Authenticate current user.
     * 
     * @return navigation string
     */
    public String authenticate() {
        Employee curEmp;
        if ((curEmp = login.authUser()) != null) {
            employee.setEmp(curEmp);
            taApprover.setEmp(curEmp);
            if (login.isAdmin()) {
                this.getCurrentSessonMap().put("Admin", true);
                return "admin";
            } else {
                for (Title title : curEmp.getTitles()) {
                    checkTitle(title);
                }
            }
            return "login";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid Login!", "Please Try Again!"));

            return null;
        }
    }

    /**
     * Navigate to timesheet view.
     * 
     * @param wkEnd
     *            from datatable.
     * @return navigation string
     */
    public String goToTimesheet(String wkEnd) {
        employee.setTsId(employee.getEmp().getEmpId(), wkEnd);
        return "timesheet";
    }

    /**
     * Logs out user.
     * 
     * @return navigation string
     * @throws IOException
     */
    public String logout() throws IOException {
        finish();
        return "logout";
    }

    /**
     * Navigate to page for changing password.
     * 
     * @return navigation string
     */
    public String goToChangePassword() {
        return "changepassword";
    }

    /**
     * Holds logic for changing password for an employee.
     * 
     * @return navigation string
     */
    public String changePassword() {
        Employee emp = employee.getEmp();
        String newPassword = emp.getNewPassword();
        String currentPassword = emp.getOldPassword();
        String confirmNewPassword = emp.getNewPasswordConfirm();

        if (!confirmNewPassword.equals(newPassword)) {            
            return null;
        }

        if (!currentPassword.equals(emp.getEmpPw())) {            
            return null;
        }

        emp.setEmpPw(newPassword);
        employeeManager.merge(emp);
        finish();
        return "success";
    }

    /**
     * String of notifications for a user.
     * 
     * @return notification string
     */
    public String getNotifications() {
        int monthState = 0, weekState = 0;
        StringBuilder notification = new StringBuilder();
        Integer tsApproveCount = null;

        taApprover.setEmp(employee.getEmp());
        taApprover.refreshList();
        tsApproveCount = taApprover.getListToBeApproved().size();
        notification.append(tsApproveCount.toString());
        List<Project> projectManaged = projMan.listOfProjects(employee.getEmp());
        if (projectManaged != null) {
            for (Project p : projectManaged) {
                projMan.setSelectedProject(p);

                List<String> weekList = projMan.getListOfWeeks(0);
                List<MonthlyReport> monthList = projMan.getMonthlyReports();

                if (!weekList.get(0).equals(employee.getEmp().getEmpLastVisitedWeekReport())) {
                    weekState = 1;
                }
                if (!monthList.get(0).getMonth().equals(employee.getEmp().getEmpLastVisitedMonthReport())) {
                    monthState = 1;
                }

            }
        }
        notification.append("," + weekState + "," + monthState);
        projMan.setSelectedProject(projMan.getSelectedProjectForViewing());
        return notification.toString();
    }

    /**
     * Show current supervisor features.
     * 
     * @return if role exists in session map
     */
    public boolean showSupervisor() {
        try {
            return ((boolean) getCurrentSessonMap().get("Supervisor"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Show current project manager features.
     * 
     * @return if role exists in session map
     */
    public boolean showProjectManager() {
        try {
            return ((boolean) getCurrentSessonMap().get("Project Manager"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Show current responsible engineer features.
     * 
     * @return if role exists in session map
     */
    public boolean showResponsibleEngineer() {
        try {
            return ((boolean) getCurrentSessonMap().get("Responsible Engineer"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Show current timesheet approver features.
     * 
     * @return if role exists in session map
     */
    public boolean showTimesheetApprover() {
        try {
            return ((boolean) getCurrentSessonMap().get("Timesheet Approver"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Show admin features
     * 
     * @return if role exists in session map
     */
    public boolean showAdmin() {
        try {
            return ((boolean) getCurrentSessonMap().get("Admin"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks all the titles a user has, then adds them into the sessionmap for
     * us to check later on.
     * 
     * @param title title to check
     */
    public void checkTitle(Title title) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(title.getTitNm(), true);
    }

    /**
     * Get supervisor controller.
     * 
     * @return supMan
     */
    public SupervisorController getSupMan() {
        return supMan;
    }

    /**
     * Set supervisor controller.
     * 
     * @param supMan supMan
     */
    public void setSupMan(SupervisorController supMan) {
        this.supMan = supMan;
    }

    /**
     * Get login controller.
     * 
     * @return login
     */
    public LoginController getLogin() {
        return login;
    }

    /**
     * Get current employee.
     * 
     * @return employee
     */
    public EmployeeController getEmployee() {
        return employee;
    }

    /**
     * Set current employee.
     * 
     * @param employee employee
     */
    public void setEmployee(EmployeeController employee) {
        this.employee = employee;
    }

    /**
     * Set login controller.
     * 
     * @param login login
     */
    public void setLogin(LoginController login) {
        this.login = login;
    }

    /**
     * Get controller for responsible engineer.
     * 
     * @return resEng
     */
    public ResponsibleEngineerController getResEng() {
        return resEng;
    }

    /**
     * Set controller for responsible engineer.
     * 
     * @param resEng resEng
     */
    public void setResEng(ResponsibleEngineerController resEng) {
        this.resEng = resEng;
    }

    /**
     * Get current admin.
     * 
     * @return admin
     */
    public AdminController getAdmin() {
        return admin;
    }

    /**
     * Set a new admin.
     * 
     * @param admin admin
     */
    public void setAdmin(AdminController admin) {
        this.admin = admin;
    }

    /**
     * Get a project manager controller.
     * 
     * @return projMan
     */
    public ProjectManagerController getProjMan() {
        return projMan;
    }

    /**
     * Set a new project manager controller
     * 
     * @param projMan projMan
     */
    public void setProjMan(ProjectManagerController projMan) {
        this.projMan = projMan;
    }

    /**
     * Get the timesheet approver controller.
     * 
     * @return taApprover
     */
    public TimesheetApproverController getTaApprover() {
        return taApprover;
    }

    /**
     * Set a new timesheet approver controller.
     * 
     * @param taApprover taApprover
     */
    public void setTaApprover(TimesheetApproverController taApprover) {
        this.taApprover = taApprover;
    }

    /**
     * Sets the session context roles.
     * 
     * @return session map
     */
    public Map<String, Object> getCurrentSessonMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }
}
