package manager;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Project;


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
}
