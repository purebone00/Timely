package manager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Tsrow;


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
    
    public List<Tsrow> getAll() {
        TypedQuery<Tsrow> query = em.createQuery("select s from Tsrow s",
                Tsrow.class); 
        List<Tsrow> Tsrow = query.getResultList();
        return Tsrow;
    }
    
    public ArrayList<Tsrow> getAllForTable(int name, String wkEnd) {
        TypedQuery<Tsrow> query = em.createQuery("select s from Tsrow s where s.tsrEmpId = ?1 and s.tsrWkEnd like :wkEnding",
                Tsrow.class)
                .setParameter(1, name)
                .setParameter("wkEnding", "%"+wkEnd+"%"); 
        ArrayList<Tsrow> Tsrow = new ArrayList<>(query.getResultList().size());
        Tsrow.addAll(query.getResultList());
        return Tsrow;
    }

    
}
