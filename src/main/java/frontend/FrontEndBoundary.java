package frontend;


import java.io.Serializable;


import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controller.LoginController;
import controller.ResponsibleEngineerController;




@Named("Master")
@ConversationScoped
public class FrontEndBoundary implements Serializable{
    @Inject Conversation conversation;
    @Inject LoginController login;
    
    @Inject ResponsibleEngineerController resEng;
    
    public LoginController getLogin() {
        return login;
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
        if(login.authUser()) {
            init();
            if (login.isAdmin()) {
                return "admin";
            }
            return "login";
        }
        return "fail";
    }
    
    public String logout() {
        finish();
        return "logout";
    }
    
    public void generateAllFeatures() {}
    
   

}
