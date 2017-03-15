package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.ProjectManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WplabManager;
import manager.WpstarepManager;
import model.Employee;
import model.Project;
import model.Workpack;
import model.WorkpackId;
import model.Wplab;
import model.WplabId;
import model.Wpstarep;
import utility.DateTimeUtility;

@Stateful
@Named("ProjMan")
public class ProjectManagerController {
	@Inject WorkPackageManager workPackageManager;
	@Inject ProjectManager projectManager;
	@Inject WplabManager wplabManager;
	@Inject TsrowManager tsRowManager;
	@Inject WpstarepManager wpstarepManager;
	
	private Project selectedProject;
	private Workpack selectedWorkPackage;
	private String selectedWeek;
	
	private List<Wplab> wpPlanHours;
	
	/**
	 * Gets a list of {@link Project}'s that an {@link Employee} manages.
	 * @param emp The {@link Employee} that manages the {@link Project}'s you want to get.
	 * @return A list of {@link Project}'s.
	 */
	public List<Project> listOfProjects(Employee emp) {
		return projectManager.getManagedProjects(emp.getEmpId());
	}
	
	
	public String selectProject(Project p) {
		setSelectedProject(p);
		
		return "manageproject";
	}
	
	public String selectProjectForReport(Project p) {
		setSelectedProject(p);
		
		return "weeklyReportsList";
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
	 * @return
	 */
	public List<Wplab> wpPlanHours() {
		if (wpPlanHours == null) {
			wpPlanHours = wplabManager.getWorkPackagePlannedHours(selectedWorkPackage.getId().getWpProjNo(), selectedWorkPackage.getId().getWpNo());
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
	 * @param w The {@link Workpack} to create the row for.
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
	 * @return empty String.
	 */
	public String createNewWP() {
		String newWpNo;
		if ((newWpNo = getNextAvailableWPName("")) == null) {
			return "";
		}
		Workpack newWp = new Workpack();
		WorkpackId newWpId = new WorkpackId(selectedProject.getProjNo(), newWpNo);
		newWp.setId(newWpId);
		newWp.setWpNm("");
		short i = 0;
		newWp.setWpDel(i);
		selectedProject.getWorkPackages().add(newWp);
		return "";
	}
	
	/**
	 * Create a child {@link Workpack}.
	 * @param parent The {@link Workpack} to create a child for.
	 * @return empty String.
	 */
	public String createChildWP(Workpack parent) {
		String newChildWpNo;
		if ((newChildWpNo = getNextAvailableWPName(parent.getId().getWpNo())) == null) {
			return "";
		}
		Workpack newChildWp = new Workpack();
		WorkpackId newChildWpId = new WorkpackId(selectedProject.getProjNo(), newChildWpNo);
		newChildWp.setId(newChildWpId);
		newChildWp.setWpNm("");
		short i = 0;
		newChildWp.setWpDel(i);
		selectedProject.getWorkPackages().add(newChildWp);
		parent.setRemoveWplabs(true);
		return "";
	}
	
	/**
	 * Finds the next available number for a {@link Workpack}. Returns null if none available.
	 * @param parentName The parent {@link Workpack}. Pass in an empty String for a top level {@link Workpack}.
	 * @return The next available {@link Workpack} number. Null if none available.
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
				
				if (6-s.length() == 0) {
					return s;
				} else {
					return String.format("%s"+"%0"+(6-s.length())+"d", s, 0);
				}
			}
		}
		
		// not able to find an available WP number
		return null;
	}
	
	/**
	 * Checks if a {@link Workpack} is a leaf node.
	 * @param workPackage {@link Workpack} to check.
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
		
		return "weeklyReport";
	}
	
	/**
	 * Saves changes made (new/modified {@link Workpack}'s, {@link Wplab}'s).
	 * @return String for previous page.
	 */
	public String save() {
		workPackageManager.merge(getSelectedProject().getWorkPackages());
		
		// if there are child WP's, make sure their parent WP's Wplabs are removed
		ArrayList<Workpack> toBeRemoved = new ArrayList<Workpack>();
		for (Workpack wp : getSelectedProject().getWorkPackages()) {
			if (wp.getRemoveWplabs()) {
				toBeRemoved.add(wp);
			}
		}
		if (!toBeRemoved.isEmpty())
			wplabManager.removeByWp(toBeRemoved);
		
		return "viewmanagedprojects";
	}
	
	/**
	 * Copy and paste of {@link ResponsibleEngineerController#listOfWpPersonHours()} but this one takes in a {@link Workpack}
	 * as argument.<br>
	 * Gets a list of arrays representing the hours worked per labour grade for the {@link #selectedWorkPackage}.<br>
	 * Each array contains:
	 * 		<ul>
	 * 		<li>Index 0: Labour grade ID (String)</li>
	 * 		<li>Index 1: Total person hours worked for the labour grade in index 0 for the selected WP (BigDecimal)</li>
	 * 		<li>Index 2: Pay rate for the labour grade in index 0 (BigDecimal)</li>
	 * 		</ul>
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
//		setTotalCost(totalCost);
//		setTotalHours(totalHours);
		return list;
	}
	
	
	public Wpstarep getResEngReport(Workpack w) {
		return wpstarepManager.find(w.getId().getWpProjNo(), w.getId().getWpNo(), getSelectedWeek());
	}
	
	public List<String[]> getResEngReportHrs(Workpack w) {
		Wpstarep wst = getResEngReport(w);
		ArrayList<String[]> labourGradeDays = new ArrayList<String[]>();
		
		if (wst != null) {			
			String fields = wst.getWsrEstDes();
			String[] rows = fields.split(",");
			
			// The list of "labour grades : hours" is stored as a single String in the database,
			// this loop parses the String.
			for (String s : rows) {
				String[] columns = s.split(":");
				labourGradeDays.add(new String[] {columns[0], columns[1]});
			}
		}
		
		return labourGradeDays;
	}
	
	public List<Workpack> getLeafWorkpacks() {
		ArrayList<Workpack> leafs = new ArrayList<Workpack>();
		for (Workpack wo : getSelectedProject().getWorkPackages()) {
			if (isLeaf(wo)) {
				leafs.add(wo);
			}
		}
		return leafs;
	}
	
	public List<String> getListOfWeeks() {
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
        
        return dtu.getListOfWeekEnds(startDate, endDate);
	}
}
