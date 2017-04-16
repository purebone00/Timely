package manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Tsrow;
import model.Workpack;

/**
 * Does CRUD for Tsrows.
 * @author Timely
 * @version 1.0
 *
 */
@Dependent
@Stateless
public class TsrowManager {
    /**
     * Entity manager.
     */
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    /**
     * Get a Tsrow by id.
     * @param id id
     * @return Tsrow
     */
    public Tsrow find(int id) {
        Tsrow foundRow = em.find(Tsrow.class, id);
        return (foundRow != null) ? foundRow : new Tsrow();
    }

    /**
     * Persist a Tsrow.
     * @param tsrow tsrow
     */
    public void persist(Tsrow tsrow) {
        em.persist(tsrow);
    }

    /**
     * Update a Tsrow.
     * @param tsrow tsrow
     */
    public void update(Tsrow tsrow) {
        em.merge(tsrow);
    }

    /**
     * Merge a Tsrow.
     * @param tsrow tsrow
     */
    public void merge(Tsrow tsrow) {
        em.merge(tsrow);
    }

    /**
     * Remove a Tsrow.
     * @param tsrow tsrow
     */
    public void remove(Tsrow tsrow) {
        em.remove(tsrow);
    }

    /**
     * No longer used. JPA Relational Mapping means we don't need to query for
     * timesheet rows if timesheets already hold a list of timesheet rows. I am
     * leaving this code here in case shit hits the fan
     * 
     * @author Joe Fong
     * @return list of Tsrows
     */
    public List<Tsrow> getAll() {
        TypedQuery<Tsrow> query = em.createQuery("select s from Tsrow s", Tsrow.class);
        List<Tsrow> Tsrow = query.getResultList();
        return (Tsrow != null) ? Tsrow : new ArrayList<Tsrow>();
    }

    /**
     * No longer used. JPA Relational Mapping means we don't need to query for
     * timesheet rows if timesheets already hold a list of timesheet rows. I am
     * leaving this code here in case shit hits the fan
     * 
     * @author Joe Fong
     * @param name name
     * @param wkEnd week end
     * @return list of Tsrows
     */
    public ArrayList<Tsrow> getAllForTable(int name, String wkEnd) {
        TypedQuery<Tsrow> query = em
                .createQuery("select s from Tsrow s where s.tsrEmpId = ?1 and s.tsrWkEnd like :wkEnding", Tsrow.class)
                .setParameter(1, name).setParameter("wkEnding", "%" + wkEnd + "%");
        ArrayList<Tsrow> Tsrow = new ArrayList<>(query.getResultList().size());
        Tsrow.addAll(query.getResultList());
        return (Tsrow != null) ? Tsrow : new ArrayList<Tsrow>();
    }

    /**
     * Gets the total Person-Days charged for an employee for a given work
     * package up to a given week.
     * 
     * @param workpack
     *            The work package.
     * @param employee
     *            The employee.
     * @param week
     *            The week.
     * @return Total Person-Days charged.
     */
    public BigDecimal getTotalDaysForEmpWP(Workpack workpack, Employee employee, String week) {
        Query query = em.createNativeQuery(
                "select SUM(s.tsrSat + s.tsrSun + s.tsrMon + s.tsrTue + s.tsrWed + s.tsrThu + s.tsrFri)"
                        + " from Tsrow s INNER JOIN Employee w ON s.tsrEmpID = w.empID"
                        + " WHERE s.tsrProjNo=:code1 AND s.tsrWpNo=:code2 AND s.tsrWkEnd=:code3"
                        + " AND s.tsrEmpID=:code4");

        query.setParameter("code1", workpack.getId().getWpProjNo());
        query.setParameter("code2", workpack.getId().getWpNo());
        query.setParameter("code3", week);
        query.setParameter("code4", employee.getEmpId());

        try {
            BigDecimal totalDays = (BigDecimal) query.getSingleResult();
            if (totalDays == null) {
                totalDays = BigDecimal.ZERO;
            }
            return totalDays.divide(new BigDecimal(8));
        } catch (NoResultException e) {
            return new BigDecimal(0.0);
        }
    }

    /**
     * Gets all Tsrows for a Workpackage.
     * @param w Workpack
     * @return list of Tsrows
     */
    public List<Tsrow> find(Workpack w) {
        TypedQuery<Tsrow> query = em.createQuery("SELECT s from Tsrow s WHERE s.tsrProjNo = ?1 " + "AND s.tsrWpNo = ?2",
                Tsrow.class);

        query.setParameter(1, w.getId().getWpProjNo());
        query.setParameter(2, w.getId().getWpNo());
        List<Tsrow> tsrows = query.getResultList();

        return (tsrows != null) ? tsrows : new ArrayList<Tsrow>();
    }
    
    /**
     * Gets all Tsrows for a Workpackage up to and including a given week.
     * @param w work package.
     * @param week week.
     * @return list of tsrows
     */
    public List<Tsrow> find(Workpack w, String week) {
        TypedQuery<Tsrow> query = em.createQuery("SELECT s from Tsrow s WHERE s.tsrProjNo = ?1 " + "AND s.tsrWpNo = ?2"
                + " AND s.tsrWkEnd <= ?3",
                Tsrow.class);

        query.setParameter(1, w.getId().getWpProjNo());
        query.setParameter(2, w.getId().getWpNo());
        query.setParameter(3, week);
        List<Tsrow> tsrows = query.getResultList();

        return (tsrows != null) ? tsrows : new ArrayList<Tsrow>();
    }

}
