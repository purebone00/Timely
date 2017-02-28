package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
}
