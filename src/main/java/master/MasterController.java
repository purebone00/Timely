package master;


import java.io.Serializable;


import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;




@Named("Master")
@ConversationScoped
public class MasterController implements Serializable{
    @Inject Conversation conversation;
    @Inject Login login;
    
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
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
    
    public MasterController() {
        
    }
    
    public String authenticate() {
        if(login.authUser()) {
            init();
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
