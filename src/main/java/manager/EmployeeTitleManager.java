package manager;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Emptitle;
import model.EmptitleId;
import model.Labgrd;

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

}
