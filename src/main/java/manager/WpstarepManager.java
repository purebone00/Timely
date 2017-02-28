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
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;

	public void persist(Wpstarep wpstarep) {
        em.persist(wpstarep);
    }
	
	public void merge(Wpstarep wpstarep) {
		em.merge(wpstarep);
	}
	
	public Wpstarep find(int wsrProjNo, String wsrWpNo, String wsrRepDt) {
		TypedQuery<Wpstarep> query = em.createQuery("select s from Wpstarep s where s.id.wsrProjNo=:code AND s.id.wsrWpNo=:code2 AND s.wsrRepDt=:code3",
                Wpstarep.class); 
		query.setParameter("code", wsrProjNo);
		query.setParameter("code2", wsrWpNo);
		query.setParameter("code3", wsrRepDt); 
        List<Wpstarep> workStatusReports = query.getResultList();
        return (workStatusReports.isEmpty() ? null : workStatusReports.get(0));
	}
}
