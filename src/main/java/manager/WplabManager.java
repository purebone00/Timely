package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Wplab;

@Dependent
@Stateless
public class WplabManager {
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
	
	public List<Wplab> getWorkPackagePlannedHours(int projNo, String wpNo) {
		 TypedQuery<Wplab> query = em.createQuery("select s from Wplab s where s.id.wlProjNo=:code AND s.id.wlWpNo=:code2", Wplab.class);
		 query.setParameter("code", projNo);
		 query.setParameter("code2", wpNo);
		 List<Wplab> plannedHours = query.getResultList();
	        
		 return plannedHours;
	}
	
	public void update(Wplab wplab) {
		em.merge(wplab);
	}
}
