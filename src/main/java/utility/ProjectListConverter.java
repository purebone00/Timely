package utility;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import manager.ProjectManager;

@ManagedBean
@RequestScoped
public class ProjectListConverter implements Converter{
    @Inject ProjectManager entityManager;
    
    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        return arg2.toString();
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        return entityManager.find(Integer.valueOf(arg2));
    }
    
}
