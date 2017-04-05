package utility.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import manager.TsrowManager;
import model.Wpstarep;

@SuppressWarnings("serial")
public class WeeklyReport implements Serializable {
    
    /**
     * Total Costs Currently Spent.
     */
    BigDecimal curTotalCosts;
    
    /**
     * Total Hours Currently Completed.
     */
    BigDecimal curTotalHours;
    
    /**
     * Estimated Costs Remaining.
     */
    BigDecimal estCostsRemaining;
    
    /**
     * Estimated Hours Remaining.
     */
    BigDecimal estHoursRemaining;
    
    int visited;
    
    /**
     * Creates a Weekly Report.
     * @param tsrowHours Output of {@link TsrowManager#getAllForWP(model.Workpack, String)}.
     * @param report {@link Wpstarep} of the {@link Workpack} for the week.
     * @param rateMap rateMap Map of Labour Grades and their rates.
     */
    public WeeklyReport(List<Object[]> tsrowHours, Wpstarep report, HashMap<String, BigDecimal> rateMap) {
        curTotalCosts = BigDecimal.ZERO;
        curTotalHours = BigDecimal.ZERO;
        estCostsRemaining = BigDecimal.ZERO;
        estHoursRemaining = BigDecimal.ZERO;
        visited = 0;
        
        for (Object[] obj : tsrowHours) {
            BigDecimal op1 = obj[1] == null ? BigDecimal.ZERO : (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            curTotalCosts = curTotalCosts.add(op1.multiply(op2));
            curTotalHours = curTotalHours.add(op1);
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

    public BigDecimal getCurTotalCosts() {
        return curTotalCosts;
    }

    public void setCurTotalCosts(BigDecimal curTotalCosts) {
        this.curTotalCosts = curTotalCosts;
    }

    public BigDecimal getCurTotalHours() {
        return curTotalHours;
    }

    public void setCurTotalHours(BigDecimal curTotalHours) {
        this.curTotalHours = curTotalHours;
    }
    
    public BigDecimal getCurTotalDays() {
        return this.getCurTotalHours().divide(new BigDecimal(8));
    }
    
    public void setCurTotalDays(BigDecimal curTotalDays) {
        this.setCurTotalHours(curTotalDays.multiply(new BigDecimal(8)));
    }

    public BigDecimal getEstCostsRemaining() {
        return estCostsRemaining;
    }

    public void setEstCostsRemaining(BigDecimal estCostsRemaining) {
        this.estCostsRemaining = estCostsRemaining;
    }

    public BigDecimal getEstHoursRemaining() {
        return estHoursRemaining;
    }

    public void setEstHoursRemaining(BigDecimal estHoursRemaining) {
        this.estHoursRemaining = estHoursRemaining;
    }
    
    public BigDecimal getEstDaysRemaining() {
        return this.getEstHoursRemaining() == null ? null : this.getEstHoursRemaining().divide(new BigDecimal(8));
    }
    
    public void setEstDaysRemaining(BigDecimal estDaysRemaining) {
        this.setEstHoursRemaining(estDaysRemaining.multiply(new BigDecimal(8)));
    }
    
    public int getVisited() {
        return this.visited;
    }
    
    public void setVisited(int visited) {
        this.visited = visited;
    }
    
}
