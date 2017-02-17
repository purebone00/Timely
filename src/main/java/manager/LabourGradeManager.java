package manager;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Labgrd;

public class LabourGradeManager implements Serializable {
	@PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
	
	public List<Labgrd> getAll() {
		TypedQuery<Labgrd> query = em.createQuery("select s from Labgrd s",
                Labgrd.class); 
        List<Labgrd> labourGrades = query.getResultList();
        
        return labourGrades;
	}
}
