package manager;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Project;
import model.Workpack;

@Dependent
@Stateless
public class WorkPackageManager {
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
	
	public List<Workpack> getResponsibleWorkPackages(int employeeId) {
		 TypedQuery<Workpack> query = em.createQuery("select s from Workpack s where s.wpResEng=:code", Workpack.class);
		 query.setParameter("code", employeeId);
		 List<Workpack> workpackages = query.getResultList();
	        
		 return workpackages;
	}
	
	/**
	 * Gets a list of {@link Workpack}'s in a given {@link Project} with a given work package number prefix.
	 * @param projNo The project number of the project to look in.
	 * @param workpackPrefix The prefix of the work package number.
	 * @return A list of {@link Workpack}'s.
	 */
	public List<Workpack> getWorkPackage(int projNo, String workpackPrefix) {
		TypedQuery<Workpack> query = em.createQuery("select s from Workpack s where s.id.wpProjNo=?1 and upper(s.id.wpNo) like ?2", Workpack.class)
				.setParameter(1, projNo).setParameter(2, workpackPrefix.toUpperCase() + "%");
		List<Workpack> workpackages = query.getResultList();
		
		return workpackages;
	}
	
	public void persist(Workpack w) {
		em.persist(w);
	}
	
	public void merge(Workpack w) {
		em.merge(w);
	}
	
	public void merge(Set<Workpack> w) {
		for (Workpack wo : w) {
			em.merge(wo);
		}
	}
	
}
