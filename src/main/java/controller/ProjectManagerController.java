package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import manager.LabourGradeManager;
import manager.EmployeeManager;
import manager.EmployeeTitleManager;
import manager.ProjectManager;
import manager.TitleManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WplabManager;
import manager.WpstarepManager;
import model.Employee;
import model.Emptitle;
import model.EmptitleId;
import model.Labgrd;
import model.Project;
import model.Title;
import model.Tsrow;
import model.Workpack;
import model.WorkpackId;
import model.Wplab;
import model.WplabId;
import model.Wpstarep;
import model.WpstarepId;
import utility.DateTimeUtility;
import utility.ReportUtility;
import utility.models.MonthlyReport;
import utility.models.MonthlyReportRow;
import utility.models.WeeklyReport;

@Stateful
@Named("projMan")
public class ProjectManagerController {
    private static final short PROJ_MAN_TIT_ID = 2;
    private static final short RES_ENG_TIT_ID = 3;
    
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
     * Used for accessing title data in the database (Title table)
     */
    @Inject
    TitleManager titleManager;
    /**
     * Used for accessing employee title data in the database (Emptitle table)
     */
    @Inject
    EmployeeTitleManager emptitleManager;
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
    public String selectProjectForManaging(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
        
        for (Workpack w : getSelectedProject().getWorkPackages()) {
            Wpstarep initial = wpstarepManager.getInitialEst(w.getId().getWpProjNo(), w.getId().getWpNo());
            if (initial != null) {
                w.setInitialEst(ReportUtility.parseWsrEstDes(initial.getWsrEstDes()));
            }
        }
        
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
        String empLastVisitWeek = emp.getEmpLastVisitedWeekReport() == null ?
                "00000000" : emp.getEmpLastVisitedWeekReport();
        
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
        String lastVisitMonth = emp.getEmpLastVisitedMonthReport() == null ?
                "000000" : emp.getEmpLastVisitedMonthReport();

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
        if (!getNewWpName().matches("^[a-zA-Z]$")) {
            FacesMessage message = new FacesMessage("Invalid WP name. Must be a letter between A-Z.");
            FacesContext.getCurrentInstance()
                .addMessage("workpackList:addWP", message);
            return null;
        }
        
        String newWpNo = isWpNameValid(getNewWpName());
        if (newWpNo == null) { // user entered invalid wp name
            FacesMessage message = new FacesMessage("Invalid WP name. Cannot be a duplicate.");
            FacesContext.getCurrentInstance()
                .addMessage("workpackList:addWP", message);
            return ""; // stay on same page
        }
        Workpack newWp = new Workpack();
        newWp.setId(new WorkpackId(selectedProject.getProjNo(), newWpNo));
        newWp.setWpNm("");
        newWp.setWpDel((short) 0);
        newWp.setWpStatus((short) 0);
        
        newWp.setWplabs(new HashSet<Wplab>());
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            newRow.setId(new WplabId(newWp.getId().getWpProjNo(), newWp.getId().getWpNo(), l.getLgId()));
            newRow.setWlDel((short) 0);
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
        if (!parent.getChildName().matches("^[a-zA-Z]$")) {
            FacesMessage message = new FacesMessage("Invalid WP name. Must be a letter between A-Z.");
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent component = UIComponent.getCurrentComponent(context);
            String clientID = component.getClientId();
            FacesContext.getCurrentInstance()
                .addMessage(clientID, message);
            return null;
        }
        String newChildWpNo = isWpNameValid(parent.getNamePrefix() + parent.getChildName());
        if (newChildWpNo == null) { // user entered invalid wp name
            FacesMessage message = new FacesMessage("Invalid WP name. Cannot be a duplicate.");
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent component = UIComponent.getCurrentComponent(context);
            String clientID = component.getClientId();
            FacesContext.getCurrentInstance()
                .addMessage(clientID, message);
            return null; // stay on same page
        }
        
        Workpack newChildWp = new Workpack();
        newChildWp.setId(new WorkpackId(selectedProject.getProjNo(), newChildWpNo));
        newChildWp.setWpNm("");
        newChildWp.setWpDel((short) 0);
        newChildWp.setWpStatus((short) 0);
        
        newChildWp.setWplabs(new HashSet<Wplab>());
        for (Labgrd l : labgrdManager.getAll()) {            
            Wplab newRow = new Wplab();
            newRow.setId(new WplabId(newChildWp.getId().getWpProjNo(), newChildWp.getId().getWpNo(), l.getLgId()));
            newRow.setWlDel((short) 0);
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
                String labourDays = ReportUtility.unparseWsrEstDes(w.getInitialEst());
                
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
        workpack.setCharged(isWpCharged(workpack));
        return new MonthlyReportRow(workpack, tsrowList, workpack.getWplabs(), report, getRateMap(), month);
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
	 * Action method that leads to page where you can assign employees to a project
	 * @param projectID id of the project
	 * @return String navigation string
	 */
	public String assignEmployeeToProject(int projectID){
		setSelectedProject(projectManager.find(projectID));
		setSelectedProjectForViewing(projectManager.find(projectID));
		//System.out.println("assignEmployee: Project id = " + projectID + "; selected project name: " + selectedProject.getProjNm());
		return "assignEmployees";
	}
	
	
	/**
     * Adds the employee with the given ID to the currently selected project.
     * @return String navigation string.
     * @param empID ID of employee to put on project.
     * */
    public String assignEmployeeToProject2(String empID){
    	Employee emp = employeeManager.find(Integer.parseInt(empID));
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
                       
        if (selectedProject.getProjMan() != null && selectedProject.getProjMan().equals(Integer.parseInt(empID))) {
            selectedProject.setProjMan(null);
            projectManager.merge(selectedProject);
        }

        for (Workpack w : selectedProject.getWorkPackages()) {
            if (w.getWpResEng() != null && w.getWpResEng().equals(Integer.parseInt(empID))) {
                w.setWpResEng(null);
                workPackageManager.merge(w);
            }
        }

        return null;
    }
    
	
	/**
     * Adds the employee with the given ID to the currently selected work package.
     * @return String navigation string.
     * @param empID ID of employee to put on project.
     * */
    public String assignEmployeeToWP(String empID){
    	Employee emp = employeeManager.find(Integer.parseInt(empID));
        //get a reference to the selected project
        selectedWorkPackage.getEmployees().add(emp);
        //update on employee side
        emp.getWorkpackages().add(selectedWorkPackage);
        //update database
        workPackageManager.merge(selectedWorkPackage);
        workPackageManager.flush();
        employeeManager.merge(emp);
        employeeManager.flush();
        //refresh the page
        return null;
    }
    
    /**
     * Removes given employee from selected work package.
     * @return String navigation string. 
     * @param empID ID of employee to put on work package.
     * */
    public String removeEmployeeFromWP(String empID){

    	Employee e = employeeManager.find(Integer.parseInt(empID));
    	if (e.getEmpId().equals(selectedWorkPackage.getWpResEng())) {
    	    unassignEmployeeAsRE(e);
    	}
    	workPackageManager.removeFromWP(selectedWorkPackage, e);
        e.getWorkpackages().remove(selectedWorkPackage);
        selectedWorkPackage.getEmployees().remove(e);
        
        return null;
    }
    
    
    public Set<String> getListOfMonths() {
        DateTimeUtility dtu = new DateTimeUtility();
        Date curDt = new Date();
        Date staDt = getSelectedProject().getProjStaDt();
        Date endDt = curDt.before(getSelectedProject().getProjEndDt()) ? curDt : getSelectedProject().getProjEndDt();

        return dtu.getListOfMonths(dtu.getDateString(staDt), dtu.getDateString(endDt));
    }
    /**
     * Gets list of employees in the selected project that the logged in supervisor supervises.
     */
    public List<Employee> allEmpInProject(Employee supervisor) {
        // return selectedProject.getEmployees();
        List<Employee> empInProject = employeeManager.getEmpProj(selectedProject);
        List<Employee> supervisedEmpInProj = new ArrayList<Employee>();
        
        //only keep the employees that the logged in supervisor supervises
        for (Employee e : empInProject) {
            if (e.getEmpSupId() != null && e.getEmpSupId().equals(supervisor.getEmpId())) {
                supervisedEmpInProj.add(e);
            }
        }
        return supervisedEmpInProj;
    }

    /**
     * Gets a list of employees not in the given project and supervised by a supervisor.
     * 
     * @param proj
     *            a project
     * @return list of employees
     */
    public List<Employee> NotEmpInProject(Employee supervisor) {
        List<Employee> empNotProj = employeeManager.getEmpNotProj(selectedProject);
        List<Employee> supervisedEmpNotProj = new ArrayList<Employee>();
        
        // only keep the employees that are supervised by the given supervisor.
        for (Employee e : empNotProj) {
            if (e.getEmpSupId() != null && e.getEmpSupId().equals(supervisor.getEmpId())) {
                supervisedEmpNotProj.add(e);
            }
        }
        return supervisedEmpNotProj;
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
     * Checks if a workpack has been charged to.
     * @param w
     * @return
     */
    public boolean isWpCharged(Workpack w) {
        return !tsRowManager.find(w).isEmpty();
    }
   
	 /**
     * Gets a list of employees in the given work package.
     * @return list of employees
     */
    public List<Employee> allEmpInWP(){
    	
    	 //return selectedProject.getEmployees();
    	return employeeManager.getEmpWP(selectedWorkPackage);
    }
    
    /**
     * Gets a list of employees not in the given work package but in the selected project.
     * @return list of employees
     */
    public List<Employee> notEmpInWP(){
    	List<Employee> empNotInWp = employeeManager.getEmpNotWP(selectedWorkPackage);
    	List<Employee> empInProjNotInWp = new ArrayList<Employee>();
    	
    	// only keep the employees that are in the selected project.
    	for (Employee e : empNotInWp) {
    	    for (Project p : e.getProjects()) {
    	        if (p.getProjNo().equals(selectedProject.getProjNo())) {
    	            empInProjNotInWp.add(e);
    	            break;
    	        }
    	    }
    	}
    	return empInProjNotInWp;
    }
    
    /**
     * Assign an employee to as a Responsible Engineer to the selected Work Package.
     * @param e The employee to assign.
     * @return null
     */
    public String assignEmployeeAsRE(Employee e) {
        selectedWorkPackage.setWpResEng(e.getEmpId());
        boolean titleExists = false;
        for (Title et : e.getTitles()) {
            if (et.getTitId() == RES_ENG_TIT_ID) {
                // check if the employee already is a RE
                titleExists = true;
            }
        }
        
        if (!titleExists) {
            // only need to add the title if the employee
            // doesn't already have it
            Title t = titleManager.find(RES_ENG_TIT_ID);
            e.getTitles().add(t);
            employeeManager.merge(e);
        }
        
        workPackageManager.merge(selectedWorkPackage);
        workPackageManager.flush();
        return null;
    }
    
    /**
     * Unassign an employee as a Responsible Engineer from the selected Work Package.
     * @param e The employee to unassign.
     * @return null.
     */
    public String unassignEmployeeAsRE(Employee e) {
        selectedWorkPackage.setWpResEng(null);
        boolean removeTitle = true;
        for (Workpack w : e.getWorkpackages()) {
            if (!selectedWorkPackage.getId().equals(w.getId())) { // ignore the selected wp
                if (w.getWpResEng() != null && w.getWpResEng().equals(e.getEmpId())) {
                    // if the employee is RE for any other WP, don't remove the title
                    removeTitle = false;
                }
             }
        }
        
        if (removeTitle) {
            // only remove the title if this is the only WP employee is RE for
            Emptitle et = emptitleManager.find(new EmptitleId(e.getEmpId(), RES_ENG_TIT_ID));
            emptitleManager.remove(et);
        }
        
        workPackageManager.merge(selectedWorkPackage);
        workPackageManager.flush();
        return null;
    }
    
    /**
     * Checks if an employee is a responsible engineer for any work packages.
     * @param e
     * @return
     */
    public boolean isResEng(Employee e) {
        return (e.getEmpId().equals(selectedWorkPackage.getWpResEng()));
    }
    
    /**
     * check if the selectedWorkPackage has an responsible engineer
     * @return
     */
    public boolean resEngAssigned() {
        return selectedWorkPackage.getWpResEng() != null;
    }
    
    /**
     * TODO move this and a bunch of other stuff to supervisorcontroller
     * assign an employee as PM to the selected Project
     * @param e
     * @return
     */
    public String assignEmployeeAsPM(Employee e) {
        selectedProject.setProjMan(e.getEmpId());
        boolean titleExists = false;
        for (Title et : e.getTitles()) {
            if (et.getTitId() == PROJ_MAN_TIT_ID) {
                // check if the employee already is a PM
                titleExists = true;
            }
        }
        
        if (!titleExists) {
            // only need to add the title if the employee
            // doesn't already have it
            Title t = titleManager.find(PROJ_MAN_TIT_ID);
            e.getTitles().add(t);
            employeeManager.merge(e);
        }
        
        projectManager.merge(selectedProject);
        projectManager.flush();
        return null;
    }
    
    /**
     * TODO move this and a bunch of other stuff to supervisorcontroller
     * unassign an employee as PM from the selected Project
     * @param e
     * @return
     */
    public String unassignEmployeeAsPM(Employee e) {
        selectedProject.setProjMan(null);
        boolean removeTitle = true;
        for (Project p : e.getProjects()) {
            if (!selectedProject.getProjNo().equals(p.getProjNo())) { // ignore the selected wp
                if (p.getProjMan() != null && p.getProjMan().equals(e.getEmpId())) {
                    // if the employee is PM for any other WP, don't remove the title
                    removeTitle = false;
                }
             }
        }
        
        if (removeTitle) {
            // only remove the title if this is the only WP employee is PM for
            Emptitle et = emptitleManager.find(new EmptitleId(e.getEmpId(), PROJ_MAN_TIT_ID));
            emptitleManager.remove(et);
        }
        
        projectManager.merge(selectedProject);
        projectManager.flush();
        return null;
    }
    
    /**
     * TODO move this and a bunch of other stuff to supervisorcontroller
     * check if an employee is a PM for any projects.
     * @param e
     * @return
     */
    public boolean isProjMan(Employee e) {
        return (e.getEmpId().equals(selectedProject.getProjMan()));
    }
    
    /**
     * TODO move this and a bunch of other stuff to supervisorcontroller
     * check if the selected Project has a PM.
     * @return
     */
    public boolean projManAssigned() {
        return selectedProject.getProjMan() != null;
    }
    
    /**
     * Go to the page to assign employees to WP
     * @param p
     * @return
     */
    public String selectProjectForWPAssigning(Project p) {
        setSelectedProject(p);
        setSelectedProjectForViewing(p);
                
        return "assignEmpToWP";
    }
    
    /**
     * Sets a workpack as closed.
     * @param w
     * @return
     */
    public String closeWorkpack(Workpack w) {
        w.setWpStatus((short) 1);
        w.setWpEndDt(new Date());
        workPackageManager.merge(w);
        return null;
    }
    
    /**
     * Checks if the WP is open
     * @param w
     * @return
     */
    public boolean wpIsOpen(Workpack w) {
        return w.getWpStatus() == null || w.getWpStatus() != 1;
    }
    
}
