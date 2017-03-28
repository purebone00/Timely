package manager;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Labgrd;
import model.Wpstarep;

@Dependent
@Stateless
public class WpstarepManager {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    public void persist(Wpstarep wpstarep) {
        em.persist(wpstarep);
    }

    public void merge(Wpstarep wpstarep) {
        em.merge(wpstarep);
    }

    /**
     * Get a Responsible Engineer's Weekly Status Report for a given Work
     * Package.
     * 
     * @param wsrProjNo
     *            The Project Number of the Work Package.
     * @param wsrWpNo
     *            The Work Package Number of the Work Package.
     * @param wsrRepDt
     *            The week of the Weekly Status Report.
     * @return Weekly Status Report.
     */
    public Wpstarep find(int wsrProjNo, String wsrWpNo, String wsrRepDt) {
        TypedQuery<Wpstarep> query = em.createQuery(
                "select s from Wpstarep s where s.id.wsrProjNo=:code AND s.id.wsrWpNo=:code2 AND s.wsrRepDt=:code3",
                Wpstarep.class);
        query.setParameter("code", wsrProjNo);
        query.setParameter("code2", wsrWpNo);
        query.setParameter("code3", wsrRepDt);
        List<Wpstarep> workStatusReports = query.getResultList();
        return (workStatusReports.isEmpty() ? null : workStatusReports.get(0));
    }
    
    /**
     * Gets the first Wpstarep ever submitted (in terms of time) for the given work package.
     * @param wsrProjNo The Project Number of the Work Package.
     * @param wsrWpNo The Work Package Number of the Work Package.
     * @return Weekly Status Report.
     */
    public Wpstarep getInitialEst(int wsrProjNo, String wsrWpNo) {
        TypedQuery<Wpstarep> query = em.createQuery(
                "select s from Wpstarep s where s.id.wsrProjNo=:code AND s.id.wsrWpNo=:code2"
                + " order by s.wsrRepDt asc",
                Wpstarep.class);
        query.setParameter("code", wsrProjNo);
        query.setParameter("code2", wsrWpNo);
        List<Wpstarep> workStatusReports = query.getResultList();
        return (workStatusReports.isEmpty() ? null : workStatusReports.get(0));
    }
}
