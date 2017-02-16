package manager;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Employee;
import model.Emptitle;

@Dependent
@Stateless
public class EmployeeTitleManager implements Serializable {

    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    public Emptitle find(int id) {
        return em.find(Emptitle.class, id);
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
