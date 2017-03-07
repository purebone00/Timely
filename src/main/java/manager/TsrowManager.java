package manager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Tsrow;
import model.Workpack;


@Dependent
@Stateless
public class TsrowManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    
    public Tsrow find(int id) {
        return em.find(Tsrow.class, id);
    }

    public void persist(Tsrow tsrow) {
        em.persist(tsrow);
    }
    
    public void update(Tsrow tsrow) {
        em.merge(tsrow);  
    }
   
    public void merge(Tsrow tsrow) {
        em.merge(tsrow);
    } 
    
    public void remove(Tsrow tsrow) {
        em.remove(tsrow);
    }
    
    /**
     * No longer used. JPA Relational Mapping means we don't need to query for timesheet rows 
     * if timesheets already hold a list of timesheet rows. I am leaving this code here in case
     * shit hits the fan
     * @author Joe Fong
     * @param employeeID
     * @return
     */
    public List<Tsrow> getAll() {
        TypedQuery<Tsrow> query = em.createQuery("select s from Tsrow s",
                Tsrow.class); 
        List<Tsrow> Tsrow = query.getResultList();
        return Tsrow;
    }
    
    /**
     * No longer used. JPA Relational Mapping means we don't need to query for timesheet rows 
     * if timesheets already hold a list of timesheet rows. I am leaving this code here in case
     * shit hits the fan
     * @author Joe Fong
     * @param employeeID
     * @return
     */
    public ArrayList<Tsrow> getAllForTable(int name, String wkEnd) {
        TypedQuery<Tsrow> query = em.createQuery("select s from Tsrow s where s.tsrEmpId = ?1 and s.tsrWkEnd like :wkEnding",
                Tsrow.class)
                .setParameter(1, name)
                .setParameter("wkEnding", "%"+wkEnd+"%"); 
        ArrayList<Tsrow> Tsrow = new ArrayList<>(query.getResultList().size());
        Tsrow.addAll(query.getResultList());
        return Tsrow;
    }

    public List<Object[]> getAllForWP(int projNo, String wpNo) {
    	Query query = em.createNativeQuery("select e.lgID, SUM(s.tsrSat + s.tsrSun + s.tsrMon + s.tsrTue + s.tsrWed + s.tsrThu + s.tsrFri),"
    			+ " e.lgRate from Tsrow s INNER JOIN Employee w ON s.tsrEmpID = w.empID"
    			+ " INNER JOIN Labgrd e ON w.empLabGrd = e.lgID"
    			+ " where s.tsrProjNo=:code1 AND s.tsrWpNo=:code2"
    			+ " GROUP BY e.lgID");
//    	TypedQuery<Tsrow> query = em.createQuery("select e.lgID, SUM(), SUM(), SUM(), SUM(), SUM(), SUM(), SUM()"
//    			+ " from Tsrow s INNER JOIN Employee w ON s.tsrEmpID = w.empID"
//    			+ " INNER JOIN Labgrd e ON w.empLabGrd = e.lgID "
//    			+ " where s.tsrProjNo=:code1 AND s.tsrWpNo=:code2", Tsrow.class);
		
    	query.setParameter("code1", projNo);
		query.setParameter("code2", wpNo);
		List<Object[]> workpackages = query.getResultList();
        
    	return workpackages;
    }
}
