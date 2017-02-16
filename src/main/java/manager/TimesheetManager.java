package manager;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
@Stateless
public class TimesheetManager {
    @PersistenceContext(unitName="Timely-persistence-unit") EntityManager em;
    
    
    
}
