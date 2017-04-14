package manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Project;
import model.Workpack;
import model.WorkpackId;

/**
 * Does CRUD for work packages.
 * @author Timely
 * @version 1.0
 *
 */
@Dependent
@Stateless
public class WorkPackageManager {
    @PersistenceContext(unitName = "Timely-persistence-unit")
    EntityManager em;

    /**
     * Get all work packages a given employee is responsible for.
     * @param employeeId the employee.
     * @return List of responsible work packages.
     */
    public List<Workpack> getResponsibleWorkPackages(int employeeId) {
        TypedQuery<Workpack> query = em.createQuery("select s from Workpack s where s.wpResEng=:code", Workpack.class);
        query.setParameter("code", employeeId);
        List<Workpack> workpackages = query.getResultList();

        return (workpackages != null) ? workpackages : new ArrayList<Workpack>();
    }

    /**
     * Gets a list of {@link Workpack}'s in a given {@link Project} with a given
     * work package number prefix.
     * 
     * @param projNo
     *            The project number of the project to look in.
     * @param workpackPrefix
     *            The prefix of the work package number.
     * @return A list of {@link Workpack}'s.
     */
    public List<Workpack> getWorkPackage(int projNo, String workpackPrefix) {
        TypedQuery<Workpack> query = em
                .createQuery("select s from Workpack s where s.id.wpProjNo=?1 and upper(s.id.wpNo) like ?2",
                        Workpack.class)
                .setParameter(1, projNo).setParameter(2, workpackPrefix.toUpperCase() + "%");
        List<Workpack> workpackages = query.getResultList();

        return (workpackages != null) ? workpackages : new ArrayList<Workpack>();
    }
    
    /**
     * Get a work package by WorkpackId.
     * @param wpId WorkpackId.
     * @return Work package.
     */
    public Workpack find(WorkpackId wpId) {
        return em.find(Workpack.class, wpId);
    }

    /**
     * An attempt to get a list of all work packages inside a project.
     * 
     * @param projNo
     *            ID of {@link Project}.
     * @return A list of all {@link Workpack}s in a single project.
     */
    public List<Workpack> getWorkPackagesInProject(int projNo) {
        TypedQuery<Workpack> query = em.createQuery("select s from Workpack s where s.id.wpProjNo=?1", Workpack.class)
                .setParameter(1, projNo);
        List<Workpack> workpackages = query.getResultList();

        return (workpackages != null) ? workpackages : new ArrayList<Workpack>();
    }

    /**
     * Persist a workpack.
     * @param w workpack
     */
    public void persist(Workpack w) {
        em.persist(w);
    }

    /**
     * Merge a workpack.
     * @param w workpack
     */
    public void merge(Workpack w) {
        em.merge(w);
    }

    /**
     * Merge a set of workpacks.
     * @param w set of workpacks
     */
    public void merge(Set<Workpack> w) {
        for (Workpack wo : w) {
            em.merge(wo);
        }
    }
    
    /**
     * Flush.
     */
    public void flush() {
        em.flush();
    }
    
    /*ripped off from ProjectManager > removeFromProject() */
    /**
     * Remove an employee from a work package.
     * @param wp work package.
     * @param e employee.
     */
    public void removeFromWP(Workpack wp, Employee e) {
        em.createNativeQuery("DELETE FROM Empwp WHERE Empwp.ewEmpId = ?1 AND Empwp.ewWpNo = ?2 AND Empwp.ewProjNo = ?3")
            .setParameter(1, e.getEmpId()).setParameter(2, wp.getId().getWpNo()).setParameter(3, wp.getId().getWpProjNo()).executeUpdate();
    }

}