package manager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import timesheetUtility.EditableTimesheet;


@Dependent
@Stateless
public class TimesheetManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    public Timesheet find(TimesheetId id) {
        return em.find(Timesheet.class, id);
    }
    
    public List<EditableTimesheet> findbyEmpId(Integer empId) {
        TypedQuery<Timesheet> query = em.createQuery("SELECT t FROM Timesheet t", Timesheet.class);
        
        List<EditableTimesheet> listOfTs = new ArrayList<EditableTimesheet>();
        for (Timesheet ts : query.getResultList()) {
            EditableTimesheet newTs = new EditableTimesheet();
            newTs.setTimesheet(ts);
            listOfTs.add(newTs);
        }
        
        return listOfTs;
    }
    

    public void persist(Timesheet timesheet) {
        em.persist(timesheet);
    }
    
    public void update(Timesheet timesheet) {
        em.persist(timesheet);  
    }
   
    public void merge(Timesheet timesheet) {
        em.merge(timesheet);
    } 
    
    public void remove(Timesheet timesheet) {
        em.remove(timesheet);
    }
    
}
