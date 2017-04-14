package utility;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import manager.EmployeeManager;

/**
 * Converter for TaApprover.
 * @author Timely
 * @version 1.0
 *
 */
@ManagedBean
@RequestScoped
@FacesConverter(value="TaApproverConverter")
public class TaApproverConverter implements Converter{
    
    /**
     * Manager for employees.
     */
    @Inject
    private EmployeeManager eManager;

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        StringBuilder builder = new StringBuilder(arg2);
        return eManager.find(builder.toString());
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        return arg2.toString();
    }
}
