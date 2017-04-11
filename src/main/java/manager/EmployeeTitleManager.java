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

@SuppressWarnings("serial")
@Dependent
@Stateless
public class EmployeeTitleManager implements Serializable {

    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    public Emptitle find(int id) {
        Emptitle foundTitle = em.find(Emptitle.class, id);
        
        return (foundTitle != null) ? foundTitle : new Emptitle();
    }
    
    public Emptitle find(EmptitleId id) {
        Emptitle foundTitle = em.find(Emptitle.class, id);
        
        return (foundTitle != null) ? foundTitle : new Emptitle();
    }

    public void persist(Emptitle empTitle) {
        em.persist(empTitle);
    }

    public void update(Emptitle empTitle) {
        em.merge(empTitle);
    }

    public void merge(Emptitle empTitle) {
        em.merge(empTitle);
    }

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
