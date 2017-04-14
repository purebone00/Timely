package manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Emptitle;
import model.EmptitleId;

/**
 * Does CRUD for EmployeeTitles.
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Dependent
@Stateless
public class EmployeeTitleManager implements Serializable {

    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    /**
     * Find a Emptitle by id.
     * @param id id
     * @return Emptitle
     */
    public Emptitle find(int id) {
        Emptitle foundTitle = em.find(Emptitle.class, id);
        
        return (foundTitle != null) ? foundTitle : new Emptitle();
    }
    
    /**
     * Find a Emptitle by EmptitleId.
     * @param id EmptitleId
     * @return Emptitle
     */
    public Emptitle find(EmptitleId id) {
        Emptitle foundTitle = em.find(Emptitle.class, id);
        
        return (foundTitle != null) ? foundTitle : new Emptitle();
    }

    /**
     * Persist a Emptitle.
     * @param empTitle Emptitle
     */
    public void persist(Emptitle empTitle) {
        em.persist(empTitle);
    }

    /**
     * Update a Emptitle.
     * @param empTitle Emptitle
     */
    public void update(Emptitle empTitle) {
        em.merge(empTitle);
    }

    /**
     * Merge a Emptitle.
     * @param empTitle emptitle.
     */
    public void merge(Emptitle empTitle) {
        em.merge(empTitle);
    }

    /**
     * Remove a emptitle.
     * @param empTitle emptitle
     */
    public void remove(Emptitle empTitle) {
        em.remove(empTitle);
    }
    
    public List<Emptitle> getAllEmptitles(Employee e) {
        TypedQuery<Emptitle> query = em.createQuery("select s from Emptitle s where s.id.etEmpId=:code", Emptitle.class);
        query.setParameter("code", e.getEmpId());
        List<Emptitle> emptitles = query.getResultList();

        return (emptitles != null) ? emptitles: new ArrayList<Emptitle>();
    }
    
    public void removeAllTitles(Employee e) {
        for (Emptitle et : getAllEmptitles(e)) {
            remove(et);
        }
    }

}
