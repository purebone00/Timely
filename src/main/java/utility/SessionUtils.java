package utility;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session Utilities.
 * @author Timely
 * @version 1.0
 *
 */
public class SessionUtils {
    
    /**
     * Get session.
     * @return Session
     */
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    /**
     * Get request.
     * @return Request
     */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * Get username.
     * @return username
     */
    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    /**
     * Get userId.
     * @return userid
     */
    public static String getUserId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("userid");            
        } else {            
            return null;
        }
    }
}
