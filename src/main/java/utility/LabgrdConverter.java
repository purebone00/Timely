package utility;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import manager.LabourGradeManager;

/**
 * Converter for Labgrds
 * @author Timely
 * @version 1.0
 *
 */
@ManagedBean
@RequestScoped
public class LabgrdConverter implements Converter {
    
    /**
     * Labour grade manager.
     */
    @Inject
    private LabourGradeManager labourGradeManager;

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        StringBuilder builder = new StringBuilder(arg2);
        return labourGradeManager.find(builder.toString());
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        return arg2.toString();
    }

}
