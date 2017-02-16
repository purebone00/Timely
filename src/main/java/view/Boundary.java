package view;


import java.io.Serializable;


import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controller.EmployeeFunctionsController;
import controller.LoginController;

@Named("Master")
@ConversationScoped
public class Boundary implements Serializable {
    @Inject Conversation conversation;
    @Inject LoginController login;
    @Inject EmployeeFunctionsController empFunc; 
    
    public LoginController getLogin() {
        return login;
    }


    
    public EmployeeFunctionsController getEmpFunc() {
        return empFunc;
    }

    public void setEmpFunc(EmployeeFunctionsController empFunc) {
        this.empFunc = empFunc;
    }

    public void setLogin(LoginController login) {
        this.login = login;
    }

    public void start() {
        conversation.begin();
    }

    public void end() {
        conversation.end();
    }

    public Boundary() {
        
    }
    
    public String authenticate() {
        if(login.authUser()) {
            start();
            return(login.isAdmin())?"admin":"login";           
        }
        return "fail";
    }
    
    public String logout() {
        end();
        return "logout";
    }
    
    public void generateAllFeatures() {}
    
   

}
