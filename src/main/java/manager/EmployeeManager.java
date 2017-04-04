package manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;

@Dependent
@Stateless
public class EmployeeManager implements Serializable {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

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
        TypedQuery<Employee> query = em.createQuery("select s from Employee s", Employee.class);
        List<Employee> employees = query.getResultList();

        return employees;
    }
    
    /**
     * Get all employees that have not been deleted
     * @return list of employees 
     */
    public List<Employee> getActiveEmps() {
        TypedQuery<Employee> query = em.createQuery("select s from Employee s where s.empDel = 0", Employee.class);
        List<Employee> employees = query.getResultList();

        return employees;
    }
    
    public Map<String, Employee> getActiveEmpMap() {
        Map<String, Employee> employeeMap = new TreeMap<String, Employee>();
        TypedQuery<Employee> query = em.createQuery("select s from Employee s where s.empDel = 0", Employee.class);
        List<Employee> employees = query.getResultList();
        for(Employee e : employees) {
            employeeMap.put(e.getEmpLnm(), e);
        }
        return employeeMap;
    }
    
    
    /**
     * Deletes the employee given by the parameter, by changing its empdel flag.
     * @param employee the employee to be deleted 
     */
    public void delete(Employee employee){
    	 employee.setEmpDel((short) 1);
    }

    /**
     * Restores the employee given by the parameter, by changing its empdel flag.
     * @param employee the employee to be deleted 
     */
    public void restore(Employee employee){
   	 employee.setEmpDel((short) 0);
   }
    
}
