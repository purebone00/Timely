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
	/**
	 * Used for accessing work package data in database (Workpack table).
	 */
    @Inject
    WorkPackageManager workPackageManager;
    /**
     * Used for accessing project data in database (Project table).
     */
    @Inject
    ProjectManager projectManager;
    /**
     * Used for accessing timesheet row data in database (Tsrow table).
     */
    @Inject
    WplabManager wplabManager;
    /**
     * Used for accessing Work package-labour grade association table data in database (Wplab table).
     */
    @Inject
    TsrowManager tsRowManager;
    /**
     * Used for accessing work package status report data in database (Wpstarep table).
     */
    @Inject
    WpstarepManager wpstarepManager;
    /**
     * Used for accessing labour grade data in database (Labgrd table).
     */
    @Inject
    LabourGradeManager labgrdManager;
    /**
     * Used for accessing employee data in database (Employee table).
     */
    @Inject
    EmployeeManager employeeManager;
    /**
    * Represents employee whose information is being altered.
    * @HasGetter
    * @HasSetter
    */
    private Employee emp;
    
    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }
    /**
     * Represents the currently selected project to display details on.
     */
    private Project selectedProject;
    /**
     * Exists so that viewing projects doesn't conflict with notifications.
     * @HasGetter
     * @HasSetter
     */
    private Project selectedProjectForViewing;
    /**
     * Represents the currently selected work package to display details on.
     * @HasGetter
     * @HasSetter
     */
    private Workpack selectedWorkPackage;
    /**
     * The currently selected week.
     * @HasGetter
     * @HasSetter
     */
    private String selectedWeek;
    /**
     * Name of a newly-generated work package.
     * @HasGetter
     * @HasSetter
     */
    private String newWpName;

    /**
     * Returns list of employees who have been assigned to the currently selected project.
     * @return List<Employees> the employees on the currently selected project.
     */
    public List<Employee> getEmployeesOnProject() {
        return employeeManager.getEmployeesOnProject(selectedProject.getProjNo());
    }

    /**
     * Display list of work packages within currently selected project.
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
     * @param p Project that has been selected
     * @return String navigation string that takes user to view that displays the selected project's details
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
    /**
     * Select project for managing (creating WP's, setting budget and estimates).
     * Sets both selectedProject and selectedProjectForViewing.
     * @return String navigation string that takes user to view that displays the selected project's details
     */
    public String selectProjectForManaging(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        
        return "manageproject";
    }
    
    /**
     * Select a project to show the weekly reports list for.
     * @param p Project to show weekly reports list for.
     * @return Sting navigation string that takes user to list of weekly reports for a given project
     */
    public String selectProjectForReport(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        return "weeklyReportsList";
    }
    
    /**
     * Select a report from the weekly reports list to see the details for.
     * @param week
     * @return String navigation string that takes user to detailed view of a single weekly report.
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
     * @param p Project to see the monthly report for.
     * @return String navigation string that takes user to view of a single monthly report.
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
    /**
     * Returns a list of monthly reports.
     * @return List<MonthlyReport>
     */
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
            BigDecimal op1 = (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            totalCost = totalCost.add(op1.multiply(op2));
            totalHours = totalHours.add(op1);
        }
        // setTotalCost(totalCost);
        // setTotalHours(totalHours);
        return list;
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
        String endDate = year + month + day;

        if (flag == 0) {
            endDate = year + month + day;
            List<String> listWeeks = dtu.getListOfAllWeekEnds(startDate, endDate);

          
            return listWeeks;
        } else if (flag == 1) {
            endDate = getSelectedWeek();
            return dtu.getListOfWeekEnds(startDate, endDate);
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
     * Moves an employee onto a project.
     * 
     * @return String navigation string for refreshing the current page.
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
     * @return String navigation string for refreshing the current page.
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
     * 
     */
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
    /**
     * Returns map of labour grades and pay rates.
     * @return HashMap<String,BigDecimal> key = labour grade. value = pay rate.
     */
    public HashMap<String, BigDecimal> getRateMap() {
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<Labgrd> list = labgrdManager.getAll();
        for (Labgrd l : list) {
            map.put(l.getLgId(), l.getLgRate());
        }
        return map;
    }

    /**
     * Returns whether or not any charges have been made to a given work package.
     * @return boolean true if charges have been made to the WP. False otherwise.
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
