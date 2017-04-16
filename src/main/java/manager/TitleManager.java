package manager;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Title;

/**
 * Does CRUD for Titles.
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Dependent
@Stateless
public class TitleManager implements Serializable {

    /**
     * Entity manager.
     */
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;
    
    /**
     * Find a title by id.
     * @param id id
     * @return Title
     */
    public Title find(short id) {
        Title foundTitle = em.find(Title.class, id);
        
        return (foundTitle != null) ? foundTitle : new Title();
    }

    /**
     * Persist a title.
     * @param title title
     */
    public void persist(Title title) {
        em.persist(title);
    }

    /**
     * Update a title.
     * @param title title
     */
    public void update(Title title) {
        em.merge(title);
    }

    /**
     * Merge a title.
     * @param title title
     */
    public void merge(Title title) {
        em.merge(title);
    }

    /**
     * Remove a title.
     * @param title title
     */
    public void remove(Title title) {
        em.remove(title);
    }

}





