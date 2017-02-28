package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import manager.LabourGradeManager;
import manager.TsrowManager;
import manager.WorkPackageManager;
import manager.WpstarepManager;
import model.Employee;
import model.Labgrd;
import model.Workpack;
import model.Wpstarep;
import model.WpstarepId;

@SessionScoped
@Named("RE")
public class ResponsibleEngineerController implements Serializable {
	@Inject WorkPackageManager workPackageManager;
	@Inject TsrowManager tsRowManager;
	@Inject LabourGradeManager labourGradeManager;
	@Inject WpstarepManager wpstarepManager;
	
	private Workpack selectedWorkPackage;
	@Inject private Wpstarep workPackageReport;
	
	private List<String[]> labourGradeDays;
	
	private BigDecimal totalCost;
	private BigDecimal totalHours;
	
	public List<Workpack> listOfWorkPackages(Employee emp) {
		return workPackageManager.getResponsibleWorkPackages(emp.getEmpId());
	}
	
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
	
	public String selectWorkPackage(Workpack w) {
		setSelectedWorkPackage(w);
		labourGradeDays = new ArrayList<String[]>();
		for (Labgrd l : labourGradeManager.getAll()) {
			labourGradeDays.add(new String[] {l.getLgId(), ""});
		}
		return "responsibleengineerreport";
	}
	
	public List<Object[]> listOfWpPersonHours() {
		List<Object[]> list = tsRowManager.getAllForWP(getSelectedWorkPackage().getId().getWpProjNo(), getSelectedWorkPackage().getId().getWpNo());
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalHours = BigDecimal.ZERO;
		for (Object[] obj : list) {
			BigDecimal op1 = (BigDecimal) obj[1];
			BigDecimal op2 = (BigDecimal) obj[2];
			totalCost = totalCost.add(op1.multiply(op2));
			totalHours = totalHours.add(op1);
		}
		setTotalCost(totalCost);
		setTotalHours(totalHours);
		return list;
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
	
	public String submitReport() {
		String labourDays = "";
		for (int i = 0; i <= labourGradeDays.size() - 1; i++) {
			labourDays = labourDays + "\"" + labourGradeDays.get(i)[0] + "\":" + labourGradeDays.get(i)[1];
			if (i != labourGradeDays.size() - 1) {
				labourDays = labourDays + ", ";
			}
		}
		workPackageReport.setId(new WpstarepId(selectedWorkPackage.getId().getWpProjNo(), selectedWorkPackage.getId().getWpNo()));
		workPackageReport.setWsrWriter(selectedWorkPackage.getWpResEng());
		workPackageReport.setWsrEstDes(labourDays);
		workPackageReport.setWsrInsDt(new Date());
		workPackageReport.setWsrUpDt(new Date());
		wpstarepManager.persist(workPackageReport);
		return "responsibleengineer";
	}
	
	public List<String[]> getLabourGradeDays() {
		return labourGradeDays;
	}
	
	public void setLabourGradeDays(List<String[]> labourGradeDays) {
		this.labourGradeDays = labourGradeDays;
	}
}
