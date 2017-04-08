package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import manager.EmployeeManager;
import manager.ProjectManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WplabManager;
import manager.WpstarepManager;
import model.Employee;
import model.Labgrd;
import model.Project;
import model.Tsrow;
import model.Workpack;
import model.WorkpackId;
import model.Wplab;
import model.WplabId;
import model.Wpstarep;
import model.WpstarepId;
import utility.DateTimeUtility;
import utility.models.MonthlyReport;
import utility.models.MonthlyReportRow;
import utility.models.WeeklyReport;

@Stateful
@Named("projMan")
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
    // fuck it
    @Inject
    EmployeeManager employeeManager;

    private Employee emp;
    
    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    private Project selectedProject;
    // this exists so that viewing projects doesnt conflict with notifications
    private Project selectedProjectForViewing;
    private Workpack selectedWorkPackage;
    private String selectedWeek;
    private String newWpName;

    /* I am a sad plant. */
    public List<Employee> getEmployeesOnProject() {
        return employeeManager.getEmployeesOnProject(selectedProject.getProjNo());
    }

    /**
     * Does not work. Display list of work packages within currently selected
     * project.
     * 
     * @return A list of {@link Workpack}s in selected project.
     */
    public List<Workpack> listOfProjectWPs() {
        return workPackageManager.getWorkPackagesInProject(selectedProject.getProjNo());
    }

    /**
     * Gets a list of {@link Project}'s that an {@link Employee} manages.
     * 
     * @param emp
     *            The {@link Employee} that manages the {@link Project}'s you
     *            want to get.
     * @return A list of {@link Project}'s.
     */
    public List<Project> listOfProjects(Employee emp) {
        try {
            setEmp(emp);  
            return projectManager.getManagedProjects(emp.getEmpId());
        } catch (NullPointerException e) {
            return new ArrayList<Project>();
        }
    }

    /**
     * Select project for managing (creating WP's, setting budget and estimates)
     * @param p
     * @return
     */
    public String selectProject(Project p) {
        setSelectedProject(p);
        
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            Wpstarep initial = wpstarepManager.getInitialEst(w.getId().getWpProjNo(), w.getId().getWpNo());
            if (initial != null) {
                w.setInitialEst(parseWsrEstDes(initial.getWsrEstDes()));
            }
        }

        return "manageproject";
    }
    
    public String selectProjectForManaging(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        
        return "manageproject";
    }
    
    /**
     * Select a project to show the weekly reports list for.
     * @param p
     * @return
     */
    public String selectProjectForReport(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        return "weeklyReportsList";
    }
    
    /**
     * Select a report from the weekly reports list to see the details for.
     * @param week
     * @return
     */
    public String selectProjectForWeeklyReport(String week) {
        String empLastVisitWeek = emp.getEmpLastVisitedWeekReport();
        
        Integer visitWeek = new Integer(empLastVisitWeek);
        Integer curWeek = new Integer(week);
        
        if(visitWeek.intValue() < curWeek.intValue()) {
            emp.setEmpLastVisitedWeekReport(curWeek.toString());
            employeeManager.merge(emp);
        }
        
        setSelectedWeek(week);
        
        return "weeklyReport";
    }
    
    /**
     * Select a project to see the monthly report for.
     * @param p
     * @return
     */
    public String selectProjectForMonthlyReport(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        
        List<MonthlyReport> reports = getMonthlyReports();
        
        String latestMonth = reports.get(0).getMonth();
        String lastVisitMonth = emp.getEmpLastVisitedMonthReport();

        Integer visitMonth = new Integer(lastVisitMonth);
        Integer curMonth = new Integer(latestMonth);
        
        if(visitMonth.intValue() < curMonth.intValue()) {
            emp.setEmpLastVisitedMonthReport(curMonth.toString());
            employeeManager.merge(emp);
        }
        
        
        return "monthlyReport";
    }

    public Project getSelectedProject() {
        return this.selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public Project getSelectedProjectForViewing() {
        return selectedProjectForViewing;
    }

    public void setSelectedProjectForViewing(Project selectedProjectForViewing) {
        this.selectedProjectForViewing = selectedProjectForViewing;
    }

    public String getSelectedWeek() {
        return this.selectedWeek;
    }

    public void setSelectedWeek(String selectedWeek) {
        this.selectedWeek = selectedWeek;
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
     * Create a new {@link Workpack}.
     * 
     * @return empty String.
     */
    public String createNewWP() {
        String newWpNo = isWpNameValid(getNewWpName());
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
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            WplabId id = new WplabId(newWp.getId().getWpProjNo(), newWp.getId().getWpNo(), l.getLgId());
            newRow.setId(id);
            i = 0;
            newRow.setWlDel(i);
            newRow.setWlPlanHrs(BigDecimal.ZERO);
            newWp.getWplabs().add(newRow);
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
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            WplabId id = new WplabId(newChildWp.getId().getWpProjNo(), newChildWp.getId().getWpNo(), l.getLgId());
            newRow.setId(id);
            i = 0;
            newRow.setWlDel(i);
            newRow.setWlPlanHrs(BigDecimal.ZERO);
            newChildWp.getWplabs().add(newRow);
        }
        
        selectedProject.getWorkPackages().add(newChildWp);
        parent.setRemoveWplabs(true);
        return "";
    }
    
    /**
     * Add an initial estimate to a work package.
     * @param w
     * @return
     */
    public String addInitialEstimate(Workpack w) {
        w.setInitialEst(new HashMap<String, BigDecimal>());
        for (Labgrd l : labgrdManager.getAll()) {
            w.getInitialEst().put(l.getLgId(), BigDecimal.ZERO);
        }
        return "";
    }
    
    /**
     * Checks if a WP name is valid (no duplicates).
     * @param wpName name to check.
     * @return the name if valid, else returns null.
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

    /**
     * Select a report from the reports list to see the statistics for.
     * @param week
     * @return
     */
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
                String labourDays = unparseWsrEstDes(w.getInitialEst());
                
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
     * Gets the monthly report for a given workpackage for a given month.<br> 
     * 
     * @param workpack The workpackage to get the monthly report for.
     * @param month The month to get the monthly-report information for.
     * @return The monthly report.
     */
    private MonthlyReportRow getReportForWpMonth(Workpack workpack, String month) {
        
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
        
        List<Tsrow> tsrowList = tsRowManager.find(workpack, endDate);
        Wpstarep report = wpstarepManager.find(workpack.getId().getWpProjNo(), workpack.getId().getWpNo(), endDate);
       
        return new MonthlyReportRow(workpack, tsrowList, workpack.getWplabs(), report, getRateMap());
    }

    /**
     * Generates the list of monthly reports.
     * @param month
     * @return
     */
    private List<MonthlyReportRow> getReportsForWpMonth(String month) {
        List<Workpack> leafs = new ArrayList<Workpack>();
        List<Workpack> parents = new ArrayList<Workpack>();
        List<MonthlyReportRow> leafReports = new ArrayList<MonthlyReportRow>();
        List<MonthlyReportRow> parentReports = new ArrayList<MonthlyReportRow>();
        
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            if (isLeaf(w)) {
                leafs.add(w);
            } else {
                parents.add(w);
            }
        }
        
        for (Workpack w : leafs) {
            leafReports.add(getReportForWpMonth(w, month));
        }
        
        for (Workpack w : parents) {
            List<MonthlyReportRow> childReports = new ArrayList<MonthlyReportRow>();
            for (MonthlyReportRow r : leafReports) {
                if (r.getWorkpack().getId().getWpNo().matches("^" + w.getNamePrefix() + ".*")) {
                    childReports.add(r);
                }
            }
            parentReports.add(MonthlyReportRow.generateAggregate(w, childReports));
        }
        
        leafReports.addAll(parentReports);
        Collections.sort(leafReports);
        
        return leafReports;
    }
    
    public List<MonthlyReport> getMonthlyReports() {
        ArrayList<MonthlyReport> reports = new ArrayList<MonthlyReport>();
        
        for (String month : getListOfMonths()) {
            List<MonthlyReportRow> reportRows = getReportsForWpMonth(month);
            MonthlyReport report = new MonthlyReport(reportRows, month);
            reports.add(report);
        }
        Collections.sort(reports);
        
        return reports;
    }

    /**
     * Gets the weekly report for a given workpackage for the past week.<br>
     * 
     * @param workpack
     *            The workpackage to get the weekly report for.
     * @return The Weekly Report.
     */
    public WeeklyReport getReportForWpWeek(Workpack workpack) {

        List<Tsrow> tsrows = tsRowManager.find(workpack, getSelectedWeek());

        Wpstarep report = wpstarepManager.find(workpack.getId().getWpProjNo(), workpack.getId().getWpNo(),
                getSelectedWeek());

        return new WeeklyReport(tsrows, report, getRateMap());
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
     * @return A list of end of weeks in the format 'YYYYMMDD'.
     */
    public List<String> getListOfWeeks(int flag) {
        DateTimeUtility dtu = new DateTimeUtility();
        Date curDt = new Date();
        Date staDt = getSelectedProject().getProjStaDt();
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();

        if (flag == 0) { // get list of all weeks
            return dtu.getListOfAllWeekEnds(dtu.getDateString(staDt), dtu.getDateString(endDt));
        } else if (flag == 1) { // get list of past 4 weeks, past 12 months, and then past years
            return dtu.getListOfWeekEnds(dtu.getDateString(staDt), getSelectedWeek());
        }
        return null;
    }

    /**
     * Action method that leads to page where you can assign employees to a
     * project
     * 
     * @param projectID
     *            id of the project
     * @return String navigation string
     */
    public String assignEmployeeToProject(int projectID) {
        setSelectedProject(projectManager.find(projectID));
        // System.out.println("assignEmployee: Project id = " + projectID + ";
        // selected project name: " + selectedProject.getProjNm());
        return "assignEmployees";
    }

    /**
     * Jen's bullshit. For moving employees onto a project
     * 
     * @return String navigation string. Just refresh the page bro.
     * @param empID
     *            ID of employee to put on project.
     */
    public String assignEmployeeToProject2(String empID) {
        Employee emp = employeeManager.find(Integer.parseInt(empID));
        // get a reference to the selected project
        selectedProject.getEmployees().add(emp);
        // update on employee side
        emp.getProjects().add(selectedProject);
        // update database
        projectManager.update(selectedProject);
        projectManager.flush();
        employeeManager.merge(emp);
        employeeManager.flush();
        // refresh the page
        return null;
    }

    /**
     * Removes given employee from selected project
     * 
     * @return String navigation string. Just refresh the page bro.
     * @param empID
     *            ID of employee to put on project.
     */
    public String removeEmployee(String empID) {

        Employee e = employeeManager.find(Integer.parseInt(empID));
        if (!e.getWorkpackages().isEmpty()) {
            projectManager.removeFromProjectWithWp(selectedProject, e);

        }
        projectManager.removeFromProject(selectedProject, e);
        e.getProjects().remove(selectedProject);
        selectedProject.getEmployees().remove(e);

        // projectManager.update(selectedProject);
        // projectManager.flush();
        // employeeManager.merge(e);
        // employeeManager.flush();
        //
        // projectManager.find(selectedProject.getProjNo());
        // employeeManager.find(Integer.parseInt(empID));
        // refresh the page

        return null;
    }

    /**
     * Gets a list of months for the currently selected project.
     * 
     * @return A list of months for the currently selected project. /** Gets a
     *         list of employees in the given project.
     * @param proj
     *            a project
     * @return list of employees
     */
    public Set<String> getListOfMonths() {
        DateTimeUtility dtu = new DateTimeUtility();
        Date curDt = new Date();
        Date staDt = getSelectedProject().getProjStaDt();
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();

        return dtu.getListOfMonths(dtu.getDateString(staDt), dtu.getDateString(endDt));
    }

    public List<Employee> allEmpInProject() {

        // return selectedProject.getEmployees();
        return employeeManager.getEmpProj(selectedProject);
    }

    /**
     * Gets a list of employees not in the given project.
     * 
     * @param proj
     *            a project
     * @return list of employees
     */
    public List<Employee> NotEmpInProject() {
        return employeeManager.getEmpNotProj(selectedProject);
    }

    /**
     * Gets the total person days charged by a given employee for a given work
     * package up to a given week.
     * 
     * @param workpack
     *            The work package.
     * @param employee
     *            The employee.
     * @param week
     *            The week.
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

    public HashMap<String, BigDecimal> getRateMap() {
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<Labgrd> list = labgrdManager.getAll();
        for (Labgrd l : list) {
            map.put(l.getLgId(), l.getLgRate());
        }
        return map;
    }

    /**
     * Checks if a workpack has been charged to.
     * @param w
     * @return
     */
    public boolean isWpCharged(Workpack w) {
        return !tsRowManager.find(w).isEmpty();
    }
    
    /**
     * The list of "labour grade : person days" estimates is stored as a single string in the database. 
     * This method parses that string into a map.
     * 
     * TODO: This and {@link ResponsibleEngineerController#parseWsrEstDes(String)} are identical and should be factored out.
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
     * TODO: This and {@link ResponsibleEngineerController#unparseWsrEstDes(HashMap)} are identical and should be factored out.
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
