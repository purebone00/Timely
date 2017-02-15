package manager;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;

@Dependent
@Stateless
public class EmployeeManager implements Serializable{
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

    public Employee find(int id) {
        return em.find(Employee.class, id);
    }

    public void persist(Employee employee) {
        em.persist(employee);
    }
    
    public void update(Employee employee) {
        em.merge(employee);  
    }
   
    public void merge(Employee supplier) {
        em.merge(supplier);
    } 
    
    public void remove(Employee employee) {
        employee = find(employee.getEmpId());
        em.remove(employee);
    }
    
    public Employee[] getAll() {
        TypedQuery<Employee> query = em.createQuery("select s from Employee s",
                Employee.class); 
        java.util.List<Employee> employees = query.getResultList();
        Employee[] emparray = new Employee[employees.size()];
        for (int i=0; i < emparray.length; i++) {
            emparray[i] = employees.get(i);
        }
        return emparray;
    }
    
    
}
