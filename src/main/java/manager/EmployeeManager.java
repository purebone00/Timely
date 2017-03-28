package manager;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Project;

@Dependent
@Stateless
public class EmployeeManager implements Serializable{
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

    /*Who knows if this'll work.See ProjectManagerController.*/
    public List<Employee> getEmployeesOnProject(int pid) {
    	Query query = em.createNativeQuery("SELECT empChNo FROM Empproj WHERE projNo = ?1");
    //	TypedQuery<Employee> query = em.createQuery("select s from Empproj s where s.projectNo=:code", Employee.class);
		//query.setParameter("code", pid);
    	
    	query.setParameter(1, pid);                                        
		List<Employee> employees = query.getResultList();
		
		return employees;
}

    public Employee find(int id) {
        return em.find(Employee.class, id);
    }

    public void flush() {
        em.flush();
    }
    
    public void persist(Employee employee) {
        em.persist(employee);
    }
   
    public void merge(Employee employee) {
        em.merge(employee);
    } 
    
    public void remove(Employee employee) {
        employee = find(employee.getEmpId());
        em.remove(employee);
    }
    
    public List<Employee> getAll() {
        TypedQuery<Employee> query = em.createQuery("select s from Employee s",
                Employee.class); 
        List<Employee> employees = query.getResultList();
        
        return employees;
    }
    
    /**
     * Gets all projects the given employee is assigned to.
     * @param emp employee 
     * @return list of projects
     */
    public Set<Project> getProjects(Employee emp) {
       return  emp.getProjects();
    }
}
