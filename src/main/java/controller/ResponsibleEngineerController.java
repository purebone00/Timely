package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.LabourGradeManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WpstarepManager;
import model.Employee;
import model.Labgrd;
import model.Tsrow;
import model.Workpack;
import model.Wpstarep;
import model.WpstarepId;
import utility.DateTimeUtility;

@SuppressWarnings("serial")
@Stateful
@Named("RE")
public class ResponsibleEngineerController implements Serializable {
    @Inject
    private WorkPackageManager workPackageManager;
    @Inject
    private TsrowManager tsRowManager;
    @Inject
    private LabourGradeManager labourGradeManager;
    @Inject
    private WpstarepManager wpstarepManager;
    @Inject
    private Wpstarep workPackageReport;

    private Workpack selectedWorkPackage;
    
    /**
     * Holds the estimated days of work remaining for a labour grade.
     */
    private HashMap<String, BigDecimal> estLabourGradeDays;
    
    /**
     * Holds the completed days of work for a labour grade.
     */
    private HashMap<String, BigDecimal> curLabourGradeDays;
    
    /**
     * Holds the initial work estimate for a labour grade.
     */
    private HashMap<String, BigDecimal> initialEstimate;
    
    /**
     * Holds the rate for a labour grade.
     */
    private HashMap<Labgrd, BigDecimal> labgrdRate;
    
    /**
     * List of labour grades.
     */
    private List<Labgrd> labourGrades;

    /**
     * Total cost of the work done in the selected WP.
     */
    private BigDecimal totalCost;
    
    /**
     * Total hours of the work done in the selected WP.
     */
    private BigDecimal totalHours;

    /**
     * Flag used to decide whether to persist or merge report.
     */
    private boolean preExisting;

    public Workpack getSelectedWorkPackage() {
        return selectedWorkPackage;
    }

    public void setSelectedWorkPackage(Workpack w) {
        selectedWorkPackage = w;
    }

    public Wpstarep getWorkPackageReport() {
        return workPackageReport;
    }

    public void setWorkPackageReport(Wpstarep workPackageReport) {
        this.workPackageReport = workPackageReport;
    }
    
    public HashMap<Labgrd, BigDecimal> getLabgrdRate() {
        return labgrdRate;
    }

    public void setLabgrdRate(HashMap<Labgrd, BigDecimal> labgrdRate) {
        this.labgrdRate = labgrdRate;
    }

    public List<Labgrd> getLabourGrades() {
        return labourGrades;
    }

    public void setLabourGrades(List<Labgrd> labourGrades) {
        this.labourGrades = labourGrades;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }
    
    /**
     * The total days of work done in the WP.
     * @return
     */
    public BigDecimal getTotalDays() {
        return totalHours.divide(new BigDecimal(8));
    }
    
    public HashMap<String, BigDecimal> getEstLabourGradeDays() {
        return estLabourGradeDays;
    }

    public void setEstLabourGradeDays(HashMap<String, BigDecimal> labourGradeDays) {
        this.estLabourGradeDays = labourGradeDays;
    }

    public HashMap<String, BigDecimal> getCurLabourGradeDays() {
        return curLabourGradeDays;
    }

    public void setCurLabourGradeDays(HashMap<String, BigDecimal> curLabourGradeDays) {
        this.curLabourGradeDays = curLabourGradeDays;
    }

    public HashMap<String, BigDecimal> getInitialEstimate() {
        return initialEstimate;
    }

    public void setInitialEstimate(HashMap<String, BigDecimal> initialEstimate) {
        this.initialEstimate = initialEstimate;
    }
    
    /**
     * Get a list of {@link Workpack}'s that an {@link Employee} is the
     * responsible engineer for.
     * 
     * @param emp
     *            The {@link Employee} responsible for the {@link Workpack}'s.
     * @return list of {@link Workpack}'s.
     */
    public List<Workpack> listOfWorkPackages(Employee emp) {
       try { 
           return workPackageManager.getResponsibleWorkPackages(emp.getEmpId());       
       } catch (NullPointerException e) {
           return new ArrayList<Workpack>();
       }
    }

    /**
     * Opens the weekly status report page for a work package.
     * 
     * @param w
     *            The work package to open the weekly status report for.
     * @return The navigation string.
     */
    public String selectWorkPackage(Workpack w) {
        DateTimeUtility dtu = new DateTimeUtility();
        setSelectedWorkPackage(w);
        
        labourGrades = labourGradeManager.getAll();
        labgrdRate = new HashMap<Labgrd, BigDecimal>();
        for (Labgrd l : labourGrades) {
            labgrdRate.put(l, l.getLgRate());
        }

        initialEstimate = new HashMap<String, BigDecimal>();
        curLabourGradeDays = getWpPersonDays();
        estLabourGradeDays = new HashMap<String, BigDecimal>();
        Wpstarep initial = wpstarepManager.getInitialEst(w.getId().getWpProjNo(), w.getId().getWpNo());        
        Wpstarep current = wpstarepManager.find(w.getId().getWpProjNo(), w.getId().getWpNo(), dtu.getEndOfWeek());

        if (initial != null) {
            initialEstimate = parseWsrEstDes(initial.getWsrEstDes());
        }

        if (current != null) { // if the weekly status report for this work package
                         // already exists, display it
            preExisting = true;
            setWorkPackageReport(current);
            estLabourGradeDays = parseWsrEstDes(current.getWsrEstDes());
        } else {
            for (Labgrd l : labourGrades) {
                estLabourGradeDays.put(l.getLgId(), BigDecimal.ZERO);
            }
            preExisting = false;
        }

        return "responsibleengineerreport";
    }
    
    /**
     * Generates a map between labour grade and days worked for the selected work package.
     * @return A map.
     */
    private HashMap<String, BigDecimal> getWpPersonDays() {
        List<Tsrow> tsrows = tsRowManager.find(getSelectedWorkPackage());
        List<Labgrd> list = labourGradeManager.getAll();
        
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (Labgrd l : list) {
            map.put(l.getLgId(), BigDecimal.ZERO);
        }
        
        totalCost = BigDecimal.ZERO;
        totalHours = BigDecimal.ZERO;
        
        for (Tsrow t : tsrows) {
            BigDecimal rowRate = t.getTimesheet().getTsPayGrade().getLgRate();
            BigDecimal rowHours = t.getTotal();
            
            String labGrd = t.getTimesheet().getTsPayGrade().getLgId();
            map.put(labGrd, map.get(labGrd).add(rowHours.divide(new BigDecimal(8))));
            
            totalCost = totalCost.add(rowRate.multiply(rowHours));
            totalHours = totalHours.add(rowHours);
        }
        
        return map;
    }

    /**
     * Submits the weekly status report.
     * 
     * @return the navigation string.
     */
    public String submitReport() {
        DateTimeUtility dtu = new DateTimeUtility();
        String labourDays = unparseWsrEstDes(estLabourGradeDays);

        workPackageReport.setId(new WpstarepId(selectedWorkPackage.getId().getWpProjNo(),
                selectedWorkPackage.getId().getWpNo(), dtu.getEndOfWeek()));
        workPackageReport.setWsrWriter(selectedWorkPackage.getWpResEng());
        workPackageReport.setWsrEstDes(labourDays);
        
        if (workPackageReport.getWsrProbLw() == null || workPackageReport.getWsrProbLw().trim().equals("")) {
            workPackageReport.setWsrProbLw("No problems this week.");
        }
        
        if (workPackageReport.getWsrProbNw() == null || workPackageReport.getWsrProbNw().trim().equals("")) {
            workPackageReport.setWsrProbNw("No anticipated problems next week.");
        }

        // checks if this is the first time the estimate is being made so it
        // knows
        // whether to update the existing entry in the database or to create a
        // new entry
        if (preExisting) {
            wpstarepManager.merge(workPackageReport);
        } else {
            wpstarepManager.persist(workPackageReport);
        }
        return "responsibleengineer";
    }
    
    /**
     * The list of "labour grade : person days" estimates is stored as a single string in the database. 
     * This method parses that string into a map.
     * 
     * TODO: This and {@link ProjectManagerController#parseWsrEstDes(String)} are identical and should be factored out.
     * @param wsrEstDes
     * @return
     */
    public HashMap<String, BigDecimal> parseWsrEstDes(String wsrEstDes) {
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        
        String[] rows = wsrEstDes.split(",");

        for (String s : rows) {
            String[] columns = s.split(":");
            map.put(columns[0], new BigDecimal(columns[1]));
        }
        
        return map;
    }
    
    /**
     * The list of "labour grade : person days" estimates is stored as a single string in the database.
     * This method generates that string from a map.
     * 
     * TODO: This and {@link ProjectManagerController#unparseWsrEstDes(HashMap)} are identical and should be factored out.
     * @param map
     * @return
     */
    public String unparseWsrEstDes(HashMap<String, BigDecimal> map) {
        String string = "";
        
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            string = string + entry.getKey() + ":" + entry.getValue().toString() + ",";
        }

        return string.substring(0, string.length() - 1);
    }
}
