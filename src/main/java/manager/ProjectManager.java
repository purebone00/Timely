package manager;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Project;
import model.Workpack;


@SuppressWarnings("serial")
@Dependent
@Stateless
public class ProjectManager implements Serializable{
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

    public Project find(int id) {
        return em.find(Project.class, id);
    }

    public void persist(Project project) {
        em.persist(project);
    }
    
    public void flush() {
        em.flush();
    }
    
    public void update(Project project) {
        em.merge(project);  
    }
   
    public void merge(Project project) {
        em.merge(project);
    } 
    
    public void remove(Project project) {
        project = find(project.getProjNo());
        em.remove(project);
    }


    public List<Project> getManagedProjects(int employeeId) {
        TypedQuery<Project> query = em.createQuery("select s from Project s where s.projMan=:code", Project.class);
        query.setParameter("code", employeeId);
        List<Project> projects = query.getResultList();
        
        return projects;
    }
    
    public List<Project> getAllProjects() {
        TypedQuery<Project> query = em.createQuery("select s from Project s", Project.class);
        List<Project> projects = query.getResultList();
        
        return projects;
    }
    
    public void removeFromProject(Project p, Employee e) {
    	 em.createNativeQuery("DELETE FROM Empproj WHERE Empproj.epEmpId = ?1 AND Empproj.epProjNo = ?2")
    				.setParameter(1, e.getEmpId()).setParameter(2, p.getProjNo()).executeUpdate();
    }
    
    public void removeFromProjectWithWp(Project p, Employee e) {
    	
    	for(Workpack w:e.getWorkpackages()){
    		Query query = em.createNativeQuery("DELETE FROM Empwp WHERE Empwp.ewEmpId = :id AND Empwp.ewProjNo = :no AND Empwp.ewWpNo = :wp");
       		query.setParameter("id", e.getEmpId()).setParameter("no", p.getProjNo()).setParameter("wp", w.getId().getWpNo());
        	query.executeUpdate();
    	}
    	
    }
   
    
}
