package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	
	private boolean preExisting;
	
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
		Wpstarep i = wpstarepManager.find(w.getId().getWpProjNo(), w.getId().getWpNo(), getEndOfWeek());
		if (i != null) {
			preExisting = true;
			setWorkPackageReport(i);
			labourGradeDays = new ArrayList<String[]>();
			String fields = i.getWsrEstDes();
			String[] rows = fields.split(",");
			for (String s : rows) {
				String[] columns = s.split(":");
				labourGradeDays.add(new String[] {columns[0], columns[1]});
			}
		} else {
			preExisting = false;
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
			labourDays = labourDays + labourGradeDays.get(i)[0] + ":" + labourGradeDays.get(i)[1];
			if (i != labourGradeDays.size() - 1) {
				labourDays = labourDays + ",";
			}
		}
		workPackageReport.setWsrRepDt(getEndOfWeek());
		workPackageReport.setId(new WpstarepId(selectedWorkPackage.getId().getWpProjNo(), selectedWorkPackage.getId().getWpNo()));
		workPackageReport.setWsrWriter(selectedWorkPackage.getWpResEng());
		workPackageReport.setWsrEstDes(labourDays);
		if (preExisting) {
			wpstarepManager.merge(workPackageReport);
		} else {
			wpstarepManager.persist(workPackageReport);
		}
		return "responsibleengineer";
	}
	
	public List<String[]> getLabourGradeDays() {
		return labourGradeDays;
	}
	
	public void setLabourGradeDays(List<String[]> labourGradeDays) {
		this.labourGradeDays = labourGradeDays;
	}
	
	public String getEndOfWeek() {
		Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK) != 7 
                ? c.get(Calendar.DAY_OF_WEEK) : 0;
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endWeek);
        int year = cal.get(Calendar.YEAR);
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        
		return year + month + day;
	}
}
