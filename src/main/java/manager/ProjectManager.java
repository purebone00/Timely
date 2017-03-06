package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Project;

@Dependent
@Stateless
public class ProjectManager {
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
	
	public List<Project> getManagedProjects(int employeeId) {
		TypedQuery<Project> query = em.createQuery("select s from Project s where s.projMan=:code", Project.class);
		query.setParameter("code", employeeId);
		List<Project> projects = query.getResultList();
		
		return projects;
	}
}
