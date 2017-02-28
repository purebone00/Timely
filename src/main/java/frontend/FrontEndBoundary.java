package frontend;


import java.io.Serializable;


import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controller.AdminController;
import controller.EmployeeController;
import controller.LoginController;
import controller.ResponsibleEngineerController;
import model.Employee;
import model.Timesheet;




@Named("Master")
@ConversationScoped
public class FrontEndBoundary implements Serializable{
    @Inject Conversation conversation;
    
    @Inject LoginController login;
    
    @Inject ResponsibleEngineerController resEng;
    @Inject AdminController admin;
    @Inject EmployeeController employee;
    
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

    public void start() {
        conversation.begin();
    }

    public void end() {
        conversation.end();
    }
    
    public void init() {
        start();
    }
    
    public void finish() {
        end();
    }
    
    public FrontEndBoundary() {
        
    }
    
    public String authenticate() {
        Employee curEmp;
        if((curEmp = login.authUser()) != null) {
            employee.setEmp(curEmp);
            init();
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
    
    public void generateAllFeatures() {}
    
   

}
