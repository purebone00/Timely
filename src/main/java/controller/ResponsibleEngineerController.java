package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.TsrowManager;
import manager.WorkPackageManager;
import model.Employee;
import model.Workpack;

@SessionScoped
@Named("RE")
public class ResponsibleEngineerController implements Serializable {
	@Inject WorkPackageManager workPackageManager;
	@Inject TsrowManager tsRowManager;
	
	Workpack selectedWorkPackage;
	
	public List<Workpack> listOfWorkPackages(Employee e) {
		return workPackageManager.getResponsibleWorkPackages(e.getEmpId());
	}
	
	public Workpack getSelectedWorkPackage() {
		return selectedWorkPackage;
	}
	
	public void setSelectedWorkPackage(Workpack w) {
		selectedWorkPackage = w;
	}
	
	public String selectWorkPackage(Workpack w) {
		setSelectedWorkPackage(w);
		return "responsibleengineerreport";
	}
	
	public List<Object[]> getWpPersonHours() {
		return tsRowManager.getAllForWP(getSelectedWorkPackage().getId().getWpProjNo(), getSelectedWorkPackage().getId().getWpNo());
	}
}
