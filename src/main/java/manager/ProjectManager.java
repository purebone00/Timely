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
import model.Labgrd;
import model.Project;
import model.Workpack;

/**
 * Does CRUD for Projects.
 * @author Timely
 * @version 1.0
 */
@SuppressWarnings("serial")
@Dependent
@Stateless
public class ProjectManager implements Serializable{
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

    /**
     * Get a project by id.
     * @param id project id
     * @return project
     */
    public Project find(int id) {
        
        Project foundProject = em.find(Project.class, id);
        
        return (foundProject != null) ? foundProject : new Project();
    }

    /**
     * Persist a project.
     * @param project project
     */
    public void persist(Project project) {
        em.persist(project);
    }
    
    /**
     * Flush.
     */
    public void flush() {
        em.flush();
    }
    
    /**
     * Update a project.
     * @param project project
     */
    public void update(Project project) {
        em.merge(project);  
    }
   
    /**
     * Merge a project.
     * @param project project
     */
    public void merge(Project project) {
        em.merge(project);
    } 
    
    /**
     * Remove a project.
     * @param project project
     */
    public void remove(Project project) {
        project = find(project.getProjNo());
        em.remove(project);
    }

    /**
     * Get all projects managed by a given employee.
     * @param employeeId Employee.
     * @return List of projects managed by a given employee.
     */
    public List<Project> getManagedProjects(int employeeId) {
        TypedQuery<Project> query = em.createQuery("select s from Project s where s.projMan=:code", Project.class);
        query.setParameter("code", employeeId);
        List<Project> projects = query.getResultList();
        
        return projects;
    }
    
    /**
     * Get all projects in the system.
     * @return List of all projects.
     */
    public List<Project> getAllProjects() {
        TypedQuery<Project> query = em.createQuery("select s from Project s", Project.class);
        List<Project> projects = query.getResultList();
        
        return projects;
    }
    
    /**
     * Remove an employee from a project.
     * @param p The project to remove from.
     * @param e The employee to remove.
     */
    public void removeFromProject(Project p, Employee e) {
    	 em.createNativeQuery("DELETE FROM Empproj WHERE Empproj.epEmpId = ?1 AND Empproj.epProjNo = ?2")
    				.setParameter(1, e.getEmpId()).setParameter(2, p.getProjNo()).executeUpdate();
    }
    
    /**
     * Remove an employee from all WP's within a given project.
     * @param p The project to remove the employee from.
     * @param e The employee to remove.
     */
    public void removeFromProjectWithWp(Project p, Employee e) {
    	
    	for (Workpack w : e.getWorkpackages()) {
    		Query query = em.createNativeQuery("DELETE FROM Empwp WHERE Empwp.ewEmpId = :id AND Empwp.ewProjNo = :no AND Empwp.ewWpNo = :wp");
       		query.setParameter("id", e.getEmpId()).setParameter("no", p.getProjNo()).setParameter("wp", w.getId().getWpNo());
        	query.executeUpdate();
    	}
    	
    }
   
    
}
