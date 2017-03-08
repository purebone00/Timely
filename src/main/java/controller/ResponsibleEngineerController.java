package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	
	/**
	 * Get a list of {@link Workpack}'s that an {@link Employee} is the responsible engineer for.
	 * @param emp The {@link Employee} responsible for the {@link Workpack}'s.
	 * @return list of {@link Workpack}'s.
	 */
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
	
	/**
	 * Opens the weekly status report page for a work package.
	 * @param w The work package to open the weekly status report for.
	 * @return The navigation string.
	 */
	public String selectWorkPackage(Workpack w) {
		setSelectedWorkPackage(w);
		
		// creates an arraylist to hold the "labour grade : hour" values
		labourGradeDays = new ArrayList<String[]>();
		for (Labgrd l : labourGradeManager.getAll()) {
			labourGradeDays.add(new String[] {l.getLgId(), ""});
		}
		
		Wpstarep i = wpstarepManager.find(w.getId().getWpProjNo(), w.getId().getWpNo(), getEndOfWeek());
		
		if (i != null) { // if the weekly status report for this work package already exists, display it
			preExisting = true;
			setWorkPackageReport(i);
			labourGradeDays = new ArrayList<String[]>();
			String fields = i.getWsrEstDes();
			String[] rows = fields.split(",");
			
			// The list of "labour grades : hours" is stored as a single String in the database,
			// this loop parses the String.
			for (String s : rows) {
				String[] columns = s.split(":");
				labourGradeDays.add(new String[] {columns[0], columns[1]});
			}
		} else {
			preExisting = false;
		}
		
		return "responsibleengineerreport";
	}
	
	/**
	 * Gets a list of arrays representing the hours worked per labour grade for the {@link #selectedWorkPackage}.<br>
	 * Each array contains:
	 * 		<ul>
	 * 		<li>Index 0: Labour grade ID (String)</li>
	 * 		<li>Index 1: Total person hours worked for the labour grade in index 0 for the selected WP (BigDecimal)</li>
	 * 		<li>Index 2: Pay rate for the labour grade in index 0 (BigDecimal)</li>
	 * 		</ul>
	 * @return The list of arrays.
	 */
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
	
	/**
	 * The total cost of the work done in {@link #selectedWorkPackage}.
	 * @return
	 */
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	
	/**
	 * The total hours of work done in {@link #selectedWorkPackage}.
	 * @return navigation string
	 */
	public BigDecimal getTotalHours() {
		return totalHours;
	}
	
	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}
	
	/**
	 * Submits the weekly status report.
	 * @return the navigation string.
	 */
	public String submitReport() {
		String labourDays = "";
		
		// we have a single VARCHAR column called wsrEstDes in the Wpstarep table that stores the weekly estimate.
		// this for-loop takes the list of labour grade estimates from the page and stores it as a single String with format:
		// "labourGrade1:hours1, labourGrade2:hours2, labourGrade3:hours3, etc", so that it can be put into the database.
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
		
		// checks if this is the first time the estimate is being made so it knows
		// whether to update the existing entry in the database or to create a new entry
		if (preExisting) {
			wpstarepManager.merge(workPackageReport);
		} else {
			wpstarepManager.persist(workPackageReport);
		}
		return "responsibleengineer";
	}
	
	/**
	 * Used to hold the 'labour grade : hours' fields in the weekly status report page.<br>
	 * Index 0 holds the labour grade, and index 1 holds the hours.
	 * @return The list of strings representing the 'labour grade : hours" fields.
	 */
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
