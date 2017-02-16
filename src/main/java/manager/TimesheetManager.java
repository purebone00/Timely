package manager;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Timesheet;


@Dependent
@Stateless
public class TimesheetManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    public Timesheet find(int id) {
        return em.find(Timesheet.class, id);
    }

    public void persist(Timesheet empTitle) {
        em.persist(empTitle);
    }
    
    public void update(Timesheet empTitle) {
        em.merge(empTitle);  
    }
   
    public void merge(Timesheet empTitle) {
        em.merge(empTitle);
    } 
    
    public void remove(Timesheet empTitle) {
        em.remove(empTitle);
    }
    
}
