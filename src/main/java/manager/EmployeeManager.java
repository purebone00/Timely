package manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Labgrd;
import model.Project;
import model.Title;
import model.Workpack;

@SuppressWarnings("serial")
@Stateful
public class EmployeeManager implements Serializable {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    
    //Note used ?? Need to test
//    /* Who knows if this'll work.See ProjectManagerController. */
//    public List<Employee> getEmployeesOnProject(int pid) {
//        Query query = em.createNativeQuery("SELECT empChNo FROM Empproj WHERE projNo = ?1 ");
//        // TypedQuery<Employee> query = em.createQuery("select s from Empproj s
//        // where s.projectNo=:code", Employee.class);
//        // query.setParameter("code", pid);
//
//        query.setParameter(1, pid);
//        
//        @SuppressWarnings("unchecked")
//        List<Employee> employees = query.getResultList();
//  
//        return (employees != null) ? employees : new ArrayList<Employee>();
//    }

    public Employee find(int id) {
        Employee foundEmployee = em.find(Employee.class,id);
        
        return (foundEmployee != null) ? foundEmployee : new Employee();
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
        TypedQuery<Employee> query = em.createQuery("select s from Employee s", Employee.class);
        List<Employee> employees = query.getResultList();

        return (employees != null) ? employees : new ArrayList<Employee>();
    }

    /**
     * Get all employees that have not been deleted
     * 
     * @return list of employees
     */
    public List<Employee> getActiveEmps() {
        TypedQuery<Employee> query = em.createQuery("select s from Employee s where s.empDel = 0", Employee.class);
        List<Employee> employees = query.getResultList();

        return (employees != null) ? employees : new ArrayList<Employee>();
    }

    public Map<Integer, Employee> getActiveEmpMap() {
        Map<Integer, Employee> employeeMap = new TreeMap<Integer, Employee>();
        TypedQuery<Employee> query = em.createQuery("select s from Employee s where s.empDel = 0", Employee.class);
        List<Employee> employees = query.getResultList();
        for (Employee e : employees) {
            employeeMap.put(e.getEmpId(), e);
        }
        return (employeeMap != null) ? employeeMap : new HashMap<Integer, Employee>();
    }

    /**
     * Deletes the employee given by the parameter, by changing its empdel flag.
     * 
     * @param employee
     *            the employee to be deleted
     */
    public void delete(Employee employee) {
        employee.setEmpDel((short) 1);
    }

    /**
     * Restores the employee given by the parameter, by changing its empdel
     * flag.
     * 
     * @param employee
     *            the employee to be deleted
     */
    public void restore(Employee employee) {
        employee.setEmpDel((short) 0);
    }

    /**
     * Gets all projects the given employee is assigned to.
     * 
     * @param emp
     *            employee
     * @return list of projects
     */
    public Set<Project> getProjects(Employee emp) {
        return emp.getProjects();
    }

    public List<Employee> getEmpNotProj(Project p) {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e" + ", Project AS p "
                + "WHERE p = :selectProject AND p " + "NOT MEMBER OF e.projects AND e.empDel != 1", Employee.class);
        query.setParameter("selectProject", p);
        List<Employee> employees = query.getResultList();
        return (employees != null) ? employees : new ArrayList<Employee>();
    }

    public List<Employee> getEmpProj(Project p) {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e" + ", Project AS p "
                + "WHERE p = :selectProject AND p " + "MEMBER OF e.projects AND e.empDel != 1", Employee.class);
        query.setParameter("selectProject", p);
        List<Employee> employees = query.getResultList();

        return (employees != null) ? employees : new ArrayList<Employee>();
    }
    
    
    /**
     * Gets employees not supervised by given employee
     * @param e supervisor
     * @return list of employees not supervised by given employee
     */
    public List<Employee> getEmpNotSup(Employee e) {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e "
                + "WHERE  e != :me AND e.empDel != 1 AND e.empSupId IS NULL OR e.empSupId != :sup ", Employee.class);
        query.setParameter("sup", e.getEmpId()).setParameter("me", e);
        List<Employee> employees = query.getResultList();
        return (employees != null) ? employees : new ArrayList<Employee>();
    }
    
    /**
     * Gets employees supervised by given employee
     * @param e supervisor
     * @return list of employees supervised by given employee
     */
    public List<Employee> getEmpSup(Employee e) {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e "
                + "WHERE e.empSupId = :sup AND e.empDel != 1 ", Employee.class);
        query.setParameter("sup", e.getEmpId());
        List<Employee> employees = query.getResultList();
        return (employees != null) ? employees : new ArrayList<Employee>();
    }
    
    public List<Employee> getTaApprovers() {
        Query q = em.createNativeQuery("select * from employee INNER JOIN emptitle ON employee.empID = emptitle.etEmpID WHERE emptitle.etTitID = 6 AND employee.empDel != 1"
                , Employee.class);
        @SuppressWarnings("unchecked")
        List<Employee> taApprovers = q.getResultList();
        
        return (taApprovers != null) ? taApprovers : new ArrayList<Employee>();
    }
    
    public Employee find(String e) {
        TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.empLnm=:code", Employee.class);
        query.setParameter("code", e);
        Employee employee = query.getSingleResult();
        
        return (employee != null) ? employee : new Employee();
    }
    
    /**
     * Get a list of employees not on the currently selected work package.
     * Who knows if this will work.
     * 
     * @param wp Work package that returned list of employees does not belong to
     * @return List<Employee> List of employees that do not belong to given work package.
     * 
     * */    
    public List<Employee> getEmpNotWP(Workpack wp) {
    	TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e" +	
			", Workpack AS wp " +
			"WHERE wp = :selectWP AND wp " +
			"NOT MEMBER OF e.workpackages AND e.empDel != 1", 
    			Employee.class); 
    	query.setParameter("selectWP", wp);
        List<Employee> emps = query.getResultList();
        return emps;
     }
    

    /**
     * Get a list of employees on the currently selected work package.
     * Who knows if this will work.
     * 
     * @param wp Work package that returned list of employees belongs to
     * @return List<Employee> List of employees that belong to given work package.
     * 
     * */    
    public List<Employee> getEmpWP(Workpack wp){
    	TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee AS e" +	
    			", Workpack AS wp " +
    			"WHERE wp = :selectWP AND wp " +
    			"MEMBER OF e.workpackages AND e.empDel != 1", 
        			Employee.class); 
    	query.setParameter("selectWP", wp);
        List<Employee> employees = query.getResultList();
         
        return employees;
    }
    
    /**
     * Removes given title t from employee e
     * @param e employee getting title removed
     * @param t title to be removed
     */
    public void removeTitle(Employee e, Title t){
        em.createNativeQuery("DELETE FROM Emptitle WHERE Emptitle.etEmpID = ?1 AND Emptitle.etTitID = ?2")
        .setParameter(1, e.getEmpId()).setParameter(2, t.getTitId()).executeUpdate();
    }
    
    /**
     * Removes all references to the given employee as a supervisor.
     * @param supervisor
     */
    public void removeSupervisorReferences(Employee supervisor) {
        for (Employee e : getEmpSup(supervisor)) {
            e.setEmpSupId(null);
            merge(e);
        }
    }
    
}
