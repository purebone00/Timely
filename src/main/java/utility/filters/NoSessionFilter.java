package utility.filters;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Must match <servlet-name> of your FacesServlet.
public class NoSessionFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        try {
            HttpSession ses = req.getSession(false);
            if (ses != null) {
                chain.doFilter(req, res);
            } else {
                res.sendRedirect(req.getContextPath() + "/faces/error.xhtml");
            }
        } catch (NullPointerException e) {
            System.out.println("hmmmmm");
            res.sendRedirect(req.getContextPath() + "/faces/error.xhtml");
        } catch (javax.ejb.EJBException e) {
            System.out.println("interesting");
            res.sendRedirect(req.getContextPath() + "/faces/error.xhtml");            
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    // ...
}