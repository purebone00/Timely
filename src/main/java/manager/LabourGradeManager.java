package manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Labgrd;

@SuppressWarnings("serial")
@Dependent
@Stateless
public class LabourGradeManager implements Serializable {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    public List<Labgrd> getAll() {
        TypedQuery<Labgrd> query = em.createQuery("select s from Labgrd s", Labgrd.class);
        List<Labgrd> labourGrades = query.getResultList();

        return (labourGrades != null) ? labourGrades : new ArrayList<Labgrd>();
    }

    public Labgrd find(String lgId) {
        TypedQuery<Labgrd> query = em.createQuery("select s from Labgrd s where s.lgId=:code", Labgrd.class);
        query.setParameter("code", lgId);
        Labgrd foundLabgrd = query.getSingleResult();
        
        return (foundLabgrd != null) ? foundLabgrd : new Labgrd();
    }
}
