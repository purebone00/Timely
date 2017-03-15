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

    /**
     * Gets a list of arrays representing the hours worked per labour grade for the work package with the given projNo and wpNo.<br>
	 * Each array contains:
	 * 		<ul>
	 * 		<li>Index 0: Labour grade ID (String)</li>
	 * 		<li>Index 1: Total person hours worked for the labour grade in index 0 for the WP (BigDecimal)</li>
	 * 		<li>Index 2: Pay rate for the labour grade in index 0 (BigDecimal)</li>
	 * 		</ul>
     * @param projNo The project number of the work package.
     * @param wpNo The work package number of the work package.
     * @return The list of arrays.
     */
    public List<Object[]> getAllForWP(int projNo, String wpNo) {
    	Query query = em.createNativeQuery("select e.lgID, SUM(s.tsrSat + s.tsrSun + s.tsrMon + s.tsrTue + s.tsrWed + s.tsrThu + s.tsrFri),"
    			+ " e.lgRate from Tsrow s INNER JOIN Employee w ON s.tsrEmpID = w.empID"
    			+ " INNER JOIN Labgrd e ON w.empLabGrd = e.lgID"
    			+ " where s.tsrProjNo=:code1 AND s.tsrWpNo=:code2"
    			+ " GROUP BY e.lgID");
		
    	query.setParameter("code1", projNo);
		query.setParameter("code2", wpNo);
		List<Object[]> workpackages = query.getResultList();
        
    	return workpackages;
    }
    
    /**
     * Pretty much the same as {@link #getAllForWP(int, String)} but only searches rows up to a specified week.
     * @param workpack Work package.
     * @param week A string representing the week to search up to (inclusive). Format: 'YYYYMMDD'.
     * @return The list of arrays.
     */
    public List<Object[]> getAllForWP(Workpack workpack, String week) {
    	Query query = em.createNativeQuery("select e.lgID, SUM(s.tsrSat + s.tsrSun + s.tsrMon + s.tsrTue + s.tsrWed + s.tsrThu + s.tsrFri),"
    			+ " e.lgRate from Tsrow s INNER JOIN Employee w ON s.tsrEmpID = w.empID"
    			+ " INNER JOIN Labgrd e ON w.empLabGrd = e.lgID"
    			+ " where s.tsrProjNo=:code1 AND s.tsrWpNo=:code2 AND s.tsrWkEnd <=:code3"
    			+ " GROUP BY e.lgID");
		
    	query.setParameter("code1", workpack.getId().getWpProjNo());
		query.setParameter("code2", workpack.getId().getWpNo());
		query.setParameter("code3", week);
		List<Object[]> workpackages = query.getResultList();
        
    	return workpackages;
    }
    
    /**
     * Gets a list of weeks from Tsrow table.
     * @param workpack Workpack to get weeks for.
     * @param week End week.
     * @return List of weeks.
     */
    public List<String> getWeekList(Workpack workpack, String week) {
    	Query query = em.createNativeQuery("SELECT DISTINCT tsrWkEnd FROM Tsrow"
    			+ " WHERE tsrProjNo=:code1 AND tsrWpNo=:code2 AND tsrWkEnd <=:code3"
    			+ " ORDER BY tsrWkEnd ASC");
    	
    	query.setParameter("code1", workpack.getId().getWpProjNo());
		query.setParameter("code2", workpack.getId().getWpNo());
		query.setParameter("code3", week);
		List<Object[]> weekResultList = query.getResultList();
		ArrayList<String> weeks = new ArrayList<String>();
		
		for (Object[] o : weekResultList) {
			weeks.add((String) o[0]);
		}
		
		return weeks;
    }
}
