package manager;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Wpstarep;

@Dependent
@Stateless
public class WpstarepManager {
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

	public void persist(Wpstarep wpstarep) {
        em.persist(wpstarep);
    }
}
