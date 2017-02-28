package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Timesheet;
import model.TimesheetId;


@Dependent
@Stateless
public class TimesheetManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

    public Timesheet find(TimesheetId id) {
        return em.find(Timesheet.class, id);
    }

    public void persist(Timesheet tsrow) {
        em.persist(tsrow);
    }
    
    public void update(Timesheet tsrow) {
        em.merge(tsrow);  
    }
   
    public void merge(Timesheet tsrow) {
        em.merge(tsrow);
    } 
    
    public void remove(Timesheet tsrow) {
        em.remove(tsrow);
    }
    
    public List<Timesheet> getAll(int employeeID) {
        TypedQuery<Timesheet> query = em.createQuery("select s from Timesheet s where s.id.tsEmpId = ?1",
                Timesheet.class).setParameter(1, employeeID); 
        List<Timesheet> Tsrow = query.getResultList();
        return Tsrow;
    }
}
