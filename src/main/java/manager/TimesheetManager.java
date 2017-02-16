package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Timesheet;
import model.TimesheetId;


@Dependent
@Stateless
public class TimesheetManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    public Timesheet find(int id) {
        return em.find(Timesheet.class, id);
    }
    
    public List<Timesheet> findbyEmpId(int empId) {
        TypedQuery<TimesheetId> query = em.createNamedQuery(
                            "SELECT s FROM TimesheetId as s WHERE s.tsEmpId = ?",
                            TimesheetId.class);
        
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
