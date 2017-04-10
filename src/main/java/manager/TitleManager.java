package manager;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Title;

@SuppressWarnings("serial")
@Dependent
@Stateless
public class TitleManager implements Serializable {

    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;
    
    public Title find(short id) {
        Title foundTitle = em.find(Title.class, id);
        
        return (foundTitle != null) ? foundTitle : new Title();
    }

    public void persist(Title title) {
        em.persist(title);
    }

    public void update(Title title) {
        em.merge(title);
    }

    public void merge(Title title) {
        em.merge(title);
    }

    public void remove(Title title) {
        em.remove(title);
    }

}





