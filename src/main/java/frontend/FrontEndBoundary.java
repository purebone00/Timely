package frontend;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
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

@SuppressWarnings("serial")
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

    

    public String finish() throws IOException {
        employee.setEmp(null);

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();

        return "/login.xhtml?faces-redirect=true&expired=true";
    }

    public FrontEndBoundary() {

    }

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
        }
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Invalid Login!",
                "Please Try Again!"));

        return null;
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
                projMan.selectProject(p);

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
    
    

    public boolean showSupervisor() {
        try {
            return ((boolean)getCurrentSessonMap().get("Supervisor"));
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    public boolean showProjectManager() {
        try {
            return ((boolean)getCurrentSessonMap().get("Project Manager"));
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    public boolean showResponsibleEngineer() {
        try {
            return ((boolean)getCurrentSessonMap().get("Responsible Engineer"));
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    public boolean showTimesheetApprover() {
        try {
            return ((boolean)getCurrentSessonMap().get("Timesheet Approver"));
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    public boolean showAdmin() {
        try {
            return ((boolean)getCurrentSessonMap().get("Admin"));
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    /**
     * Checks all the titles a user has, then adds them 
     * into the sessionmap for us to check later on.
     * @param title
     */
    public void checkTitle(Title title) {
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .put(title.getTitNm(),true);
    }
    
    //Getter and Setters
    
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
    
    public Map<String, Object> getCurrentSessonMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }
}
