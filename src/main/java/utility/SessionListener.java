package utility;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("login.xhtml");
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
