package utility.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import model.Tsrow;
import model.Wpstarep;

/**
 * Represents a weekly report.
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class WeeklyReport implements Serializable {
    
    /**
     * Total Costs Currently Spent.
     */
    private BigDecimal curTotalCosts;
    
    /**
     * Total Hours Currently Completed.
     */
    private BigDecimal curTotalHours;
    
    /**
     * Estimated Costs Remaining.
     */
    private BigDecimal estCostsRemaining;
    
    /**
     * Estimated Hours Remaining.
     */
    private BigDecimal estHoursRemaining;
    
    /**
     * Overtime hours worked.
     */
    private BigDecimal overtimeHrs;
    
    /**
     * Visited flag.
     */
    private int visited;
    
    /**
     * Creates a Weekly Report.
     * @param tsrows List of Tsrows
     * @param report {@link Wpstarep} of the {@link Workpack} for the week.
     * @param rateMap rateMap Map of Labour Grades and their rates.
     */
    public WeeklyReport(List<Tsrow> tsrows, Wpstarep report, HashMap<String, BigDecimal> rateMap) {
        curTotalCosts = BigDecimal.ZERO;
        curTotalHours = BigDecimal.ZERO;
        estCostsRemaining = BigDecimal.ZERO;
        estHoursRemaining = BigDecimal.ZERO;
        overtimeHrs = BigDecimal.ZERO;
        visited = 0;
        
        for (Tsrow t : tsrows) {
            BigDecimal rate = t.getTimesheet().getTsPayGrade() == null ?
                    t.getTimesheet().getEmployee().getEmpLabGrd().getLgRate() :
                    t.getTimesheet().getTsPayGrade().getLgRate();
            curTotalCosts = curTotalCosts.add(t.getTotal().multiply(rate));
            curTotalHours = curTotalHours.add(t.getTotal());
            overtimeHrs = overtimeHrs.add(t.getTsrOt() == null ? BigDecimal.ZERO : t.getTsrOt());
        }
        
        if (report != null) {
            String fields = report.getWsrEstDes();
            String[] rows = fields.split(",");

            // The list of "labour grades : hours" is stored as a single String
            // in the database,
            // this loop parses the String.
            for (String s : rows) {
                String[] columns = s.split(":");
                BigDecimal op1 = new BigDecimal(Double.parseDouble(columns[1]) * 8);
                BigDecimal op2 = rateMap.get(columns[0]);
                estCostsRemaining = estCostsRemaining.add(op1.multiply(op2));
                estHoursRemaining = estHoursRemaining.add(op1);
            }
            
        } else {
            estCostsRemaining = null;
            estHoursRemaining = null;
        }
    }

    /**
     * Get curTotalCosts.
     * @return curTotalCosts
     */
    public BigDecimal getCurTotalCosts() {
        return curTotalCosts;
    }

    /**
     * Set curTotalCosts.
     * @param curTotalCosts curTotalCosts
     */
    public void setCurTotalCosts(BigDecimal curTotalCosts) {
        this.curTotalCosts = curTotalCosts;
    }

    /**
     * Get curTotalHours.
     * @return curTotalHours
     */
    public BigDecimal getCurTotalHours() {
        return curTotalHours;
    }

    /**
     * Set curTotalHours.
     * @param curTotalHours curTotalHours
     */
    public void setCurTotalHours(BigDecimal curTotalHours) {
        this.curTotalHours = curTotalHours;
    }
    
    /**
     * Get getCurTotalDays.
     * @return getCurTotalDays
     */
    public BigDecimal getCurTotalDays() {
        return this.getCurTotalHours().divide(new BigDecimal(8));
    }
    
    /**
     * Set getCurTotalDays.
     * @param curTotalDays getCurTotalDays
     */
    public void setCurTotalDays(BigDecimal curTotalDays) {
        this.setCurTotalHours(curTotalDays.multiply(new BigDecimal(8)));
    }

    /**
     * Get estCostsRemaining.
     * @return estCostsRemaining
     */
    public BigDecimal getEstCostsRemaining() {
        return estCostsRemaining;
    }

    /**
     * Set estCostsRemaining.
     * @param estCostsRemaining estCostsRemaining
     */
    public void setEstCostsRemaining(BigDecimal estCostsRemaining) {
        this.estCostsRemaining = estCostsRemaining;
    }

    /**
     * Get estHoursRemaining.
     * @return estHoursRemaining
     */
    public BigDecimal getEstHoursRemaining() {
        return estHoursRemaining;
    }

    /**
     * Set estHoursRemaining.
     * @param estHoursRemaining estHoursRemaining
     */
    public void setEstHoursRemaining(BigDecimal estHoursRemaining) {
        this.estHoursRemaining = estHoursRemaining;
    }
    
    /**
     * Get getEstDaysRemaining.
     * @return getEstDaysRemaining
     */
    public BigDecimal getEstDaysRemaining() {
        return this.getEstHoursRemaining() == null ? null : this.getEstHoursRemaining().divide(new BigDecimal(8));
    }
    
    /**
     * Set getEstDaysRemaining.
     * @param estDaysRemaining getEstDaysRemaining
     */
    public void setEstDaysRemaining(BigDecimal estDaysRemaining) {
        this.setEstHoursRemaining(estDaysRemaining.multiply(new BigDecimal(8)));
    }
    
    /**
     * Get getOvertimeHrs.
     * @return getOvertimeHrs
     */
    public BigDecimal getOvertimeHrs() {
        return overtimeHrs;
    }

    /**
     * Set getOvertimeHrs.
     * @param overtimeHrs getOvertimeHrs
     */
    public void setOvertimeHrs(BigDecimal overtimeHrs) {
        this.overtimeHrs = overtimeHrs;
    }
    
    /**
     * Get getOvertimeDays.
     * @return getOvertimeDays
     */
    public BigDecimal getOvertimeDays() {
        return this.getOvertimeHrs().divide(new BigDecimal(8));
    }

    /**
     * Get visited.
     * @return visited
     */
    public int getVisited() {
        return this.visited;
    }
    
    /**
     * Set visited.
     * @param visited visited
     */
    public void setVisited(int visited) {
        this.visited = visited;
    }
    
}
