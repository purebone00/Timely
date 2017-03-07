package controller;

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
	
	public String submitEstimate() {
		for (Wplab wplab : wpPlanHours) {
			WplabId id = new WplabId(selectedWorkPackage.getId().getWpProjNo(), selectedWorkPackage.getId().getWpNo(), wplab.getId().getWlLgId());
			wplab.setId(id);
			wplabManager.update(wplab);
		}
		
		return "manageproject";
	}
	
	public String addWplabRow() {
		Wplab newRow = new Wplab();
		WplabId id = new WplabId(selectedWorkPackage.getId().getWpProjNo(), selectedWorkPackage.getId().getWpNo(), "");
		newRow.setId(id);
		short i = 0;
		newRow.setWlDel(i);
		wpPlanHours.add(newRow);
		return "";
	}
	
	public String createNewWP() {
		return "";
	}
	
	public String getNextAvailableWPName(String parentName) {
		if (parentName.length() >= 6) {
			return null;
		}
		return "";
	}
	
	
}
