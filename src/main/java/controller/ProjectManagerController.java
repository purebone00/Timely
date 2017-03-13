package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import manager.ProjectManager;
import manager.WorkPackageManager;
import manager.WplabManager;
import model.Employee;
import model.Project;
import model.Workpack;
import model.WorkpackId;
import model.Wplab;
import model.WplabId;

@Stateful
@Named("ProjMan")
public class ProjectManagerController {
	@Inject WorkPackageManager workPackageManager;
	@Inject ProjectManager projectManager;
	@Inject WplabManager wplabManager;
	
	private Project selectedProject;
	private Workpack selectedWorkPackage;
	
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
	
	public Project getSelectedProject() {
		return this.selectedProject;
	}
	
	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
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
}
