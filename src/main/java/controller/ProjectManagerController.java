package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.LabourGradeManager;
import manager.ProjectManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WplabManager;
import manager.WpstarepManager;
import model.Employee;
import model.Labgrd;
import model.Project;
import model.Workpack;
import model.WorkpackId;
import model.Wplab;
import model.WplabId;
import model.Wpstarep;
import model.WpstarepId;
import utility.DateTimeUtility;

@Stateful
@Named("ProjMan")
public class ProjectManagerController {
    @Inject
    WorkPackageManager workPackageManager;
    @Inject
    ProjectManager projectManager;
    @Inject
    WplabManager wplabManager;
    @Inject
    TsrowManager tsRowManager;
    @Inject
    WpstarepManager wpstarepManager;
    @Inject
    LabourGradeManager labgrdManager;

    private Project selectedProject;
    private Workpack selectedWorkPackage;
    private String selectedWeek;
    private String newWpName;

    private List<Wplab> wpPlanHours;
    
    
    //TODO: When getting the list of your employees, remember to check the empdel flag to see if they are deleted
    
    

    /**
     * Gets a list of {@link Project}'s that an {@link Employee} manages.
     * 
     * @param emp
     *            The {@link Employee} that manages the {@link Project}'s you
     *            want to get.
     * @return A list of {@link Project}'s.
     */
    public List<Project> listOfProjects(Employee emp) {
        return projectManager.getManagedProjects(emp.getEmpId());
    }

    public String selectProject(Project p) {
        setSelectedProject(p);
        
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            Wpstarep initial = wpstarepManager.getInitialEst(w.getId().getWpProjNo(), w.getId().getWpNo());
            if (initial != null) {
                w.setInitialEst(new HashMap<String, BigDecimal>());
                String fields = initial.getWsrEstDes();
                String[] rows = fields.split(",");
                
                for (String s : rows) {
                    String[] columns = s.split(":");
                    w.getInitialEst().put(columns[0], new BigDecimal(columns[1]));
                }
            }
        }

        return "manageproject";
    }

    public String selectProjectForReport(Project p) {
        setSelectedProject(p);

        return "weeklyReportsList";
    }
    
    public String selectProjectForWeeklyReport(String week) {
        setSelectedWeek(week);
        
        return "weeklyReport";
    }
    
    public String selectProjectForMonthlyReport(Project p) {
        setSelectedProject(p);
        
        return "monthlyReport";
    }

    public Project getSelectedProject() {
        return this.selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public String getSelectedWeek() {
        return this.selectedWeek;
    }

    public void setSelectedWeek(String selectedWeek) {
        this.selectedWeek = selectedWeek;
    }

    /**
     * Gets a list of {@link Wplab} for {@link #selectedWorkPackage}.
     * 
     * @return
     */
    public List<Wplab> wpPlanHours() {
        if (wpPlanHours == null) {
            wpPlanHours = wplabManager.getWorkPackagePlannedHours(selectedWorkPackage.getId().getWpProjNo(),
                    selectedWorkPackage.getId().getWpNo());
        }

        if (wpPlanHours.isEmpty()) {
            wpPlanHours.add(new Wplab());
        }

        return wpPlanHours;
    }

    public String selectWorkPackage(Workpack workPackage) {
        setSelectedWorkPackage(workPackage);

        return "workpackage";
    }

    public Workpack getSelectedWorkPackage() {
        return this.selectedWorkPackage;
    }

    public void setSelectedWorkPackage(Workpack workPackage) {
        this.selectedWorkPackage = workPackage;
    }

    /**
     * Add a new {@link Wplab} row.
     * 
     * @param w
     *            The {@link Workpack} to create the row for.
     * @return empty String.
     */
    public String addWplabRow(Workpack w) {
        Wplab newRow = new Wplab();
        WplabId id = new WplabId(w.getId().getWpProjNo(), w.getId().getWpNo(), "");
        newRow.setId(id);
        short i = 0;
        newRow.setWlDel(i);
        newRow.setEditable(true);
        if (w.getWplabs() == null) {
            w.setWplabs(new HashSet<Wplab>());
        }
        w.getWplabs().add(newRow);
        return "";
    }

    /**
     * Create a new {@link Workpack}.
     * 
     * @return empty String.
     */
    public String createNewWP() {
        String newWpNo = isWpNameValid(getNewWpName());
//        if ((newWpNo = getNextAvailableWPName("")) == null) {
//            return "";
//        }
        if (newWpNo == null) {
            return "";
        }
        Workpack newWp = new Workpack();
        WorkpackId newWpId = new WorkpackId(selectedProject.getProjNo(), newWpNo);
        newWp.setId(newWpId);
        newWp.setWpNm("");
        short i = 0;
        newWp.setWpDel(i);
        
        newWp.setWplabs(new HashSet<Wplab>());
        // newWp.setInitialEst(new HashMap<String, BigDecimal>());
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            WplabId id = new WplabId(newWp.getId().getWpProjNo(), newWp.getId().getWpNo(), l.getLgId());
            newRow.setId(id);
            i = 0;
            newRow.setWlDel(i);
            newRow.setWlPlanHrs(BigDecimal.ZERO);
            newWp.getWplabs().add(newRow);
            
            // newWp.getInitialEst().put(l.getLgId(), BigDecimal.ZERO);
        }
        
        selectedProject.getWorkPackages().add(newWp);
        return "";
    }

    /**
     * Create a child {@link Workpack}.
     * 
     * @param parent
     *            The {@link Workpack} to create a child for.
     * @return empty String.
     */
    public String createChildWP(Workpack parent) {
        String newChildWpNo = isWpNameValid(parent.getNamePrefix() + parent.getChildName());
//        if ((newChildWpNo = getNextAvailableWPName(parent.getId().getWpNo())) == null) {
//            return "";
//        }
        if (newChildWpNo == null) {
            return "";
        }
        
        Workpack newChildWp = new Workpack();
        WorkpackId newChildWpId = new WorkpackId(selectedProject.getProjNo(), newChildWpNo);
        newChildWp.setId(newChildWpId);
        newChildWp.setWpNm("");
        short i = 0;
        newChildWp.setWpDel(i);
        
        newChildWp.setWplabs(new HashSet<Wplab>());
        // newChildWp.setInitialEst(new HashMap<String, BigDecimal>());
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            WplabId id = new WplabId(newChildWp.getId().getWpProjNo(), newChildWp.getId().getWpNo(), l.getLgId());
            newRow.setId(id);
            i = 0;
            newRow.setWlDel(i);
            newRow.setWlPlanHrs(BigDecimal.ZERO);
            newChildWp.getWplabs().add(newRow);
            
            // newChildWp.getInitialEst().put(l.getLgId(), BigDecimal.ZERO);
        }
        
        selectedProject.getWorkPackages().add(newChildWp);
        parent.setRemoveWplabs(true);
        return "";
    }
    
    public String addInitialEstimate(Workpack w) {
        w.setInitialEst(new HashMap<String, BigDecimal>());
        for (Labgrd l : labgrdManager.getAll()) {
            w.getInitialEst().put(l.getLgId(), BigDecimal.ZERO);
        }
        return "";
    }

    /**
     * Finds the next available number for a {@link Workpack}. Returns null if
     * none available.
     * 
     * @param parentName
     *            The parent {@link Workpack}. Pass in an empty String for a top
     *            level {@link Workpack}.
     * @return The next available {@link Workpack} number. Null if none
     *         available.
     */
    public String getNextAvailableWPName(String parentName) {
        String parentNameFormatted = parentName.replace("0", "");

        // can't go more than 6 levels deep of WP's
        if (parentNameFormatted.length() >= 6) {
            return null;
        }

        for (char i = 'A'; i <= 'Z'; i++) {
            boolean goToNextLetter = false;
            String s = parentNameFormatted + Character.toString(i);

            if (workPackageManager.getWorkPackage(getSelectedProject().getProjNo(), s).isEmpty()) {
                for (Workpack w : getSelectedProject().getWorkPackages()) {
                    if (w.getId().getWpNo().matches(s + ".*")) {
                        goToNextLetter = true;
                        break;
                    }
                }

                if (goToNextLetter) {
                    continue;
                }

                if (6 - s.length() == 0) {
                    return s;
                } else {
                    return String.format("%s" + "%0" + (6 - s.length()) + "d", s, 0);
                }
            }
        }

        // not able to find an available WP number
        return null;
    }
    
    /**
     * Checks if a WP name is valid (no duplicates).
     * @param wpName name to check.
     * @return the name if valid, else returns false.
     */
    public String isWpNameValid(String wpName) {
        if (wpName.length() > 6) {
            return null;
        }
        
        if (workPackageManager.getWorkPackage(getSelectedProject().getProjNo(), wpName).isEmpty()) {
            for (Workpack w : getSelectedProject().getWorkPackages()) {
                if (w.getId().getWpNo().matches(wpName + ".*")) {
                    return null;
                }
            }

            if (6 - wpName.length() == 0) {
                return wpName;
            } else {
                return String.format("%s" + "%0" + (6 - wpName.length()) + "d", wpName, 0);
            }
        }
        
        return null;
    }

    /**
     * Checks if a {@link Workpack} is a leaf node.
     * 
     * @param workPackage
     *            {@link Workpack} to check.
     * @return True if it is a leaf node.
     */
    public boolean isLeaf(Workpack workPackage) {
        String nameFormatted = workPackage.getId().getWpNo().replace("0", "");
        if (nameFormatted.length() == 6) {
            return true;
        }

        // check database for children
        if (workPackageManager.getWorkPackage(workPackage.getId().getWpProjNo(), nameFormatted).size() > 1) {
            return false;
        }

        int matchCount = 0;

        // check if child has been created but not persisted yet
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            if (w.getId().getWpNo().matches(nameFormatted + ".*")) {
                matchCount++;
            }
            if (matchCount > 1) {
                return false;
            }
        }

        return true;
    }

    public String selectWeeklyReport(String week) {
        setSelectedWeek(week);

        return "weeklyStatistics";
    }

    /**
     * Saves changes made (new/modified {@link Workpack}'s, {@link Wplab}'s).
     * 
     * @return String for previous page.
     */
    public String save() {
        workPackageManager.merge(getSelectedProject().getWorkPackages());

        // if there are child WP's, make sure their parent WP's Wplabs are
        // removed
        ArrayList<Workpack> toBeRemoved = new ArrayList<Workpack>();
        for (Workpack wp : getSelectedProject().getWorkPackages()) {
            if (wp.getRemoveWplabs()) {
                toBeRemoved.add(wp);
            }
        }
        if (!toBeRemoved.isEmpty())
            wplabManager.removeByWp(toBeRemoved);
        
        List<Wpstarep> initialEstimates = new ArrayList<Wpstarep>();
        
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            if (w.getInitialEst() != null) {                
                Wpstarep workPackageReport = new Wpstarep();
                String labourDays = "";
                
                for (Map.Entry<String, BigDecimal> entry : w.getInitialEst().entrySet()) {
                    labourDays = labourDays + entry.getKey() + ":" + entry.getValue().toString() + ",";
                }
                
                labourDays = labourDays.substring(0, labourDays.length()-1);
                
                workPackageReport.setWsrInsDt(new Date());
                workPackageReport.setWsrUpDt(new Date());
                workPackageReport.setId(
                        new WpstarepId(w.getId().getWpProjNo(), w.getId().getWpNo(), "00000000"));
                workPackageReport.setWsrEstDes(labourDays);
                
                initialEstimates.add(workPackageReport);
            }
        }
        
        for (Wpstarep ws : initialEstimates) {
            wpstarepManager.merge(ws);
        }

        return "viewmanagedprojects";
    }

    /**
     * Copy and paste of
     * {@link ResponsibleEngineerController#listOfWpPersonHours()} but this one
     * takes in a {@link Workpack} as argument.<br>
     * Gets a list of arrays representing the hours worked per labour grade for
     * the {@link #selectedWorkPackage}.<br>
     * Each array contains:
     * <ul>
     * <li>Index 0: Labour grade ID (String)</li>
     * <li>Index 1: Total person hours worked for the labour grade in index 0
     * for the selected WP (BigDecimal)</li>
     * <li>Index 2: Pay rate for the labour grade in index 0 (BigDecimal)</li>
     * </ul>
     * 
     * @return The list of arrays.
     */
    public List<Object[]> listOfWpPersonHours(Workpack workpack) {
        List<Object[]> list = tsRowManager.getAllForWP(workpack, getSelectedWeek());
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalHours = BigDecimal.ZERO;
        for (Object[] obj : list) {
            BigDecimal op1 = obj[1] == null ? BigDecimal.ZERO : (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            totalCost = totalCost.add(op1.multiply(op2));
            totalHours = totalHours.add(op1);
        }
        // setTotalCost(totalCost);
        // setTotalHours(totalHours);
        return list;
    }
    
    /**
     * Gets the monthly-report information for a given workpackage for a given month.<br> 
     * Returns an array of size 8:
     * <ul>
     * <li>index 0 = estimated total hours</li>
     * <li>index 1 = estimated total dollars</li>
     * <li>index 2 = current hours</li>
     * <li>index 3 = current dollars</li>
     * <li>index 4 = projected total hours</li>
     * <li>index 5 = projected total dollars</li>
     * <li>index 6 = variance hours</li>
     * <li>index 7 = variance dollars</li>
     * </ul>
     * 
     * @param workpack The workpackage to get the monthly-report information for.
     * @param month The month to get the monthly-report information for.
     * @return The monthly-report information.
     */
    public Object[] getReportForWpMonth(Workpack workpack, String month) {
        
        DateTimeUtility dtu = new DateTimeUtility();
        String endDate = dtu.getEndOfWeek(dtu.getEndOfMonth(month + "01"));
        Date curDt = new Date();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDt);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        curDt = cal.getTime();
        
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();
        
        
        cal.setTime(endDt);
        int year = cal.get(Calendar.YEAR);
        String monthStr = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        
        String endDate2 = dtu.getEndOfWeek(year + monthStr + day);
        
        if (endDate2.compareTo(endDate) <= 0) {
            endDate = endDate2;
        }
        
        List<Object[]> list = tsRowManager.getAllForWP(workpack, endDate);
        
        BigDecimal curTotalCosts = BigDecimal.ZERO;
        BigDecimal curTotalHours = BigDecimal.ZERO;
        BigDecimal estTotalCosts = BigDecimal.ZERO;
        BigDecimal estTotalHours = BigDecimal.ZERO;
        BigDecimal projTotalCosts = BigDecimal.ZERO;
        BigDecimal projTotalHours = BigDecimal.ZERO;
        BigDecimal varianceCosts = BigDecimal.ZERO;
        BigDecimal varianceHours = BigDecimal.ZERO;
        
        for (Object[] obj : list) {
            BigDecimal op1 = obj[1] == null ? BigDecimal.ZERO : (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            curTotalCosts = curTotalCosts.add(op1.multiply(op2));
            curTotalHours = curTotalHours.add(op1);
        }
        
        for (Wplab w : workpack.getWplabs()) {
            BigDecimal op1 = w.getWlPlanHrs();
            BigDecimal op2 = labgrdManager.find(w.getId().getWlLgId()).getLgRate();
            estTotalHours = estTotalHours.add(w.getWlPlanHrs());
            estTotalCosts = estTotalCosts.add(op1.multiply(op2));
        }
        
        Wpstarep report = wpstarepManager.find(workpack.getId().getWpProjNo(), workpack.getId().getWpNo(), endDate);

        if (report != null) {
            String fields = report.getWsrEstDes();
            String[] rows = fields.split(",");

            // The list of "labour grades : hours" is stored as a single String
            // in the database,
            // this loop parses the String.
            for (String s : rows) {
                String[] columns = s.split(":");
                BigDecimal op1 = new BigDecimal(columns[1]);
                BigDecimal op2 = labgrdManager.find(columns[0]).getLgRate();
                projTotalCosts = projTotalCosts.add(op1.multiply(op2));
                projTotalHours = projTotalHours.add(op1);
            }
            
            projTotalCosts = projTotalCosts.add(curTotalCosts);
            projTotalHours = projTotalHours.add(curTotalHours);
        } else {
            projTotalCosts = null;
            projTotalHours = null;
        }
        
        if (projTotalCosts != null && projTotalHours != null) {            
            varianceCosts = ((projTotalCosts.subtract(estTotalCosts)).divide(estTotalCosts, 2, RoundingMode.HALF_EVEN));
            varianceHours = ((projTotalHours.subtract(estTotalHours)).divide(estTotalHours, 2, RoundingMode.HALF_EVEN));        
        } else {
            varianceCosts = null;
            varianceHours = null;
        }
        
        Object[] results = {estTotalHours, estTotalCosts, curTotalHours, curTotalCosts, projTotalHours, projTotalCosts, varianceHours, varianceCosts};
        return results;
    }
    
    /**
     * Gets the weekly-report information for a given workpackage for the past week.<br> 
     * Returns an array of size 4:
     * <ul>
     * <li>index 0 = current total work done</li>
     * <li>index 1 = current total dollars</li>
     * <li>index 2 = projected work remaining</li>
     * <li>index 3 = projected costs remaining</li>
     * </ul>
     * 
     * @param workpack The workpackage to get the weekly-report information for.
     * @return
     */
    public Object[] getReportForWpWeek(Workpack workpack) {
        
        List<Object[]> list = tsRowManager.getAllForWP(workpack, getSelectedWeek());
        
        BigDecimal curTotalCosts = BigDecimal.ZERO;
        BigDecimal curTotalHours = BigDecimal.ZERO;
        BigDecimal estCostsRemaining = BigDecimal.ZERO;
        BigDecimal estHoursRemaining = BigDecimal.ZERO;
        
        for (Object[] obj : list) {
            BigDecimal op1 = obj[1] == null ? BigDecimal.ZERO : (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            curTotalCosts = curTotalCosts.add(op1.multiply(op2));
            curTotalHours = curTotalHours.add(op1);
        }
        
        Wpstarep report = wpstarepManager.find(workpack.getId().getWpProjNo(), workpack.getId().getWpNo(), getSelectedWeek());

        if (report != null) {
            String fields = report.getWsrEstDes();
            String[] rows = fields.split(",");

            // The list of "labour grades : hours" is stored as a single String
            // in the database,
            // this loop parses the String.
            for (String s : rows) {
                String[] columns = s.split(":");
                BigDecimal op1 = new BigDecimal(columns[1]);
                BigDecimal op2 = labgrdManager.find(columns[0]).getLgRate();
                estCostsRemaining = estCostsRemaining.add(op1.multiply(op2));
                estHoursRemaining = estHoursRemaining.add(op1);
            }
            
        } else {
            estCostsRemaining = null;
            estHoursRemaining = null;
        }
        
        Object[] results = {curTotalHours, curTotalCosts, estHoursRemaining, estCostsRemaining};
        return results;
    }

    /**
     * Gets the Responsible Engineer's Report for a given {@link Workpack} for
     * the {@link #selectedWeek}.
     * 
     * @param w
     *            The {@link Workpack} to get the Responsible Engineer's Report
     *            for.
     * @return Responsible Engineer's Report.
     */
    public Wpstarep getResEngReport(Workpack w) {
        return wpstarepManager.find(w.getId().getWpProjNo(), w.getId().getWpNo(), getSelectedWeek());
    }

    /**
     * Gets the Responsible Engineer's Estimated Remaining Hours for the
     * {@link #selectedWeek}.
     * 
     * @param w
     *            The {@link Workpack} to get the Responsible Engineer's
     *            Estimated Remaining Hours for.
     * @return A list of Estimated Remaining Hours. Each list element contains
     *         an String array where index 0 is the Labour Grade, and index 1 is
     *         the Hours.
     */
    public List<String[]> getResEngReportHrs(Workpack w) {
        Wpstarep wst = getResEngReport(w);
        ArrayList<String[]> labourGradeDays = new ArrayList<String[]>();

        if (wst != null) {
            String fields = wst.getWsrEstDes();
            String[] rows = fields.split(",");

            // The list of "labour grades : hours" is stored as a single String
            // in the database,
            // this loop parses the String.
            for (String s : rows) {
                String[] columns = s.split(":");
                labourGradeDays.add(new String[] { columns[0], columns[1] });
            }
        }

        return labourGradeDays;
    }

    /**
     * Gets a list of leaf {@link Workpack}'s.
     * 
     * @return A list of leaf {@link Workpack}'s.
     */
    public List<Workpack> getLeafWorkpacks() {
        ArrayList<Workpack> leafs = new ArrayList<Workpack>();
        for (Workpack wo : getSelectedProject().getWorkPackages()) {
            if (isLeaf(wo)) {
                leafs.add(wo);
            }
        }
        return leafs;
    }

    /**
     * Gets a list of the end of weeks for the {@link #selectedProject} in the
     * format 'YYYYMMDD', from the Project's start date to the Project's end
     * date. If the current date comes before the Project's end date, the last
     * date in the list will be the end of the current week.
     * 
     * @param flag 0 for list of all weeks up until today, 1 for list of weeks in pattern for weekly reports up until selectedWeek
     * 
     * @return A list of end of weeks in the format 'YYYYMMDD'.
     */
    public List<String> getListOfWeeks(int flag) {
        DateTimeUtility dtu = new DateTimeUtility();
        Date curDt = new Date();
        Date staDt = getSelectedProject().getProjStaDt();
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();

        Calendar cal = Calendar.getInstance();

        cal.setTime(staDt);
        int year = cal.get(Calendar.YEAR);
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String startDate = year + month + day;

        cal.setTime(endDt);
        year = cal.get(Calendar.YEAR);
        month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        
        String endDate = "";
        
        if (flag == 0) {            
            endDate = year + month + day;
            return dtu.getListOfAllWeekEnds(startDate, endDate);
        } else if (flag == 1) {
            endDate = getSelectedWeek();
            return dtu.getListOfWeekEnds(startDate, endDate);
        }

        return null;
    }
    
    /**
     * Gets a list of months for the currently selected project.
     * @return A list of months for the currently selected project.
     */
    public Set<String> getListOfMonths() {
        DateTimeUtility dtu = new DateTimeUtility();
        Date curDt = new Date();
        Date staDt = getSelectedProject().getProjStaDt();
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();
        
        Calendar cal = Calendar.getInstance();

        cal.setTime(staDt);
        int year = cal.get(Calendar.YEAR);
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String startDate = year + month + day;

        cal.setTime(endDt);
        year = cal.get(Calendar.YEAR);
        month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String endDate = year + month + day;

        return dtu.getListOfMonths(startDate, endDate);
    }
    
    /**
     * Gets the total person days charged by a given employee for a given work package up to a given week.
     * @param workpack The work package.
     * @param employee The employee.
     * @param week The week.
     * @return The total person days charged.
     */
    public BigDecimal getPersonDaysCharged(Workpack workpack, Employee employee, String week) {
        return tsRowManager.getTotalDaysForEmpWP(workpack, employee, week);
    }
    
    public String getNewWpName() {
        return this.newWpName;
    }
    
    public void setNewWpName(String newWpName) {
        this.newWpName = newWpName;
    }

}
