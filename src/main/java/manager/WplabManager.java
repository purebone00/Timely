package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Workpack;
import model.Wplab;
import model.WplabId;

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
	
	public Wplab find(WplabId id) {
		return em.find(Wplab.class, id);
	}
	
	public void update(Wplab wplab) {
		em.merge(wplab);
	}
	
	public void remove(Wplab w) {
		w = find(w.getId());
		em.remove(w);
	}
	
	/**
	 * Removes all the {@link Wplab}'s associated with a list of {@link Workpack}'s.
	 * @param w List of {@link Workpack}'s to remove {@link Wplab}'s for.
	 */
	public void removeByWp(List<Workpack> w) {
		// removing using em.find(), em.remove() wasn't working 
		// (no errors, but row wasn't getting removed from database),
		// so using native query instead
		for (Workpack wp : w) {
			Query query = em.createNativeQuery("DELETE FROM Wplab WHERE Wplab.wlProjNo = ?1 AND Wplab.wlWpNo = ?2")
					.setParameter(1, wp.getId().getWpProjNo()).setParameter(2, wp.getId().getWpNo());
			query.executeUpdate();
		}
	}
}
