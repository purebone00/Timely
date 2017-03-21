package manager;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Labgrd;

@Dependent
@Stateless
public class LabourGradeManager implements Serializable {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    public List<Labgrd> getAll() {
        TypedQuery<Labgrd> query = em.createQuery("select s from Labgrd s", Labgrd.class);
        List<Labgrd> labourGrades = query.getResultList();

        return labourGrades;
    }

    public Labgrd find(String lgId) {
        TypedQuery<Labgrd> query = em.createQuery("select s from Labgrd s where s.lgId=:code", Labgrd.class);
        query.setParameter("code", lgId);
        return query.getSingleResult();
        // return em.find(Labgrd.class, lgId);
    }
}
