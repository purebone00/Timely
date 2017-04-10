package manager;

import java.util.ArrayList;
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
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    public Timesheet find(TimesheetId id) {
        Timesheet foundTimesheet = em.find(Timesheet.class, id);
        
        return (foundTimesheet != null) ? foundTimesheet : null;
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

    /**
     * No longer used. JPA Relational Mapping means we don't need to query for
     * timesheets if employees already hold a list of timesheets. I am leaving
     * this code here in case shit hits the fan
     * 
     * @author Joe Fong
     * @param employeeID
     * @return
     */
    public List<Timesheet> getAll(int employeeID) {
        TypedQuery<Timesheet> query = em
                .createQuery("select s from Timesheet s where s.id.tsEmpId = ?1", Timesheet.class)
                .setParameter(1, employeeID);
        List<Timesheet> Tsrow = query.getResultList();
        return (Tsrow != null) ? Tsrow : new ArrayList<Timesheet>();
    }
}
