package utility.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import manager.TsrowManager;
import model.Wplab;
import model.Wpstarep;

public class MonthlyReport {
    
    /**
     * Total Hours Budgeted.
     */
    BigDecimal budgetTotalHours;
    
    /**
     * Total Costs Budgeted.
     */
    BigDecimal budgetTotalCosts;
    
    /**
     * Total Hours Currently Completed.
     */
    BigDecimal curTotalHours;
    
    /**
     * Total Costs Currently Spent.
     */
    BigDecimal curTotalCosts;
    
    /**
     * Projected Total Hours.
     */
    BigDecimal projTotalHours;
    
    /**
     * Projected Total Costs.
     */
    BigDecimal projTotalCosts;
    
    /**
     * Variance between projTotalHours and budgetTotalHours.
     */
    BigDecimal varTime;
    
    /**
     * Variance between projTotalCosts and budgetTotalCosts.
     */
    BigDecimal varCosts;
    
    /**
     * Creates a Monthly Report.
     * @param tsrowHours Output of {@link TsrowManager#getAllForWP(model.Workpack, String)}.
     * @param wplabs {@link Wplab}'s of the {@link Workpack} to generate the report for.
     * @param report {@link Wpstarep} of the {@link Workpack} for the week.
     * @param rateMap Map of Labour Grades and their rates.
     */
    public MonthlyReport(List<Object[]> tsrowHours, Set<Wplab> wplabs, Wpstarep report, HashMap<String, BigDecimal> rateMap) {
        budgetTotalHours = BigDecimal.ZERO;
        budgetTotalCosts = BigDecimal.ZERO;
        curTotalHours = BigDecimal.ZERO;
        curTotalCosts = BigDecimal.ZERO;
        projTotalHours = BigDecimal.ZERO;
        projTotalCosts = BigDecimal.ZERO;
        varTime = BigDecimal.ZERO;
        varCosts = BigDecimal.ZERO;
        
        for (Object[] obj : tsrowHours) {
            BigDecimal op1 = obj[1] == null ? BigDecimal.ZERO : (BigDecimal) obj[1];
            BigDecimal op2 = (BigDecimal) obj[2];
            curTotalCosts = curTotalCosts.add(op1.multiply(op2));
            curTotalHours = curTotalHours.add(op1);
        }
        
        for (Wplab w : wplabs) {
            BigDecimal op1 = w.getWlPlanHrs();
            BigDecimal op2 = rateMap.get(w.getId().getWlLgId());
            budgetTotalHours = budgetTotalHours.add(w.getWlPlanHrs());
            budgetTotalCosts = budgetTotalCosts.add(op1.multiply(op2));
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
                projTotalCosts = projTotalCosts.add(op1.multiply(op2));
                projTotalHours = projTotalHours.add(op1);
            }
            
            projTotalCosts = projTotalCosts.add(curTotalCosts);
            projTotalHours = projTotalHours.add(curTotalHours);
        } else {
            projTotalCosts = null;
            projTotalHours = null;
        }
        
        if (projTotalCosts != null && projTotalHours != null) {            
            varCosts = ((projTotalCosts.subtract(budgetTotalCosts)).divide(budgetTotalCosts, 2, RoundingMode.HALF_EVEN));
            varTime = ((projTotalHours.subtract(budgetTotalHours)).divide(budgetTotalHours, 2, RoundingMode.HALF_EVEN));        
        } else {
            varCosts = null;
            varTime = null;
        }
    }

    public BigDecimal getBudgetTotalHours() {
        return budgetTotalHours;
    }

    public void setBudgetTotalHours(BigDecimal budgetTotalHours) {
        this.budgetTotalHours = budgetTotalHours;
    }
    
    public BigDecimal getBudgetTotalDays() {
        return this.getBudgetTotalHours().divide(new BigDecimal(8));
    }
    
    public void setBudgetTotalDays(BigDecimal budgetTotalDays) {
        this.setBudgetTotalHours(budgetTotalDays.multiply(new BigDecimal(8)));
    }

    public BigDecimal getBudgetTotalCosts() {
        return budgetTotalCosts;
    }

    public void setBudgetTotalCosts(BigDecimal budgetTotalCosts) {
        this.budgetTotalCosts = budgetTotalCosts;
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

    public BigDecimal getCurTotalCosts() {
        return curTotalCosts;
    }

    public void setCurTotalCosts(BigDecimal curTotalCosts) {
        this.curTotalCosts = curTotalCosts;
    }

    public BigDecimal getProjTotalHours() {
        return projTotalHours;
    }

    public void setProjTotalHours(BigDecimal projTotalHours) {
        this.projTotalHours = projTotalHours;
    }
    
    public BigDecimal getProjTotalDays() {
        return this.getProjTotalHours() == null ? null : this.getProjTotalHours().divide(new BigDecimal(8));
    }
    
    public void setProjTotalDays(BigDecimal projTotalDays) {
        this.setProjTotalHours(projTotalDays.multiply(new BigDecimal(8)));
    }

    public BigDecimal getProjTotalCosts() {
        return projTotalCosts;
    }

    public void setProjTotalCosts(BigDecimal projTotalCosts) {
        this.projTotalCosts = projTotalCosts;
    }

    public BigDecimal getVarTime() {
        return varTime;
    }

    public void setVarTime(BigDecimal varTime) {
        this.varTime = varTime;
    }

    public BigDecimal getVarCosts() {
        return varCosts;
    }

    public void setVarCosts(BigDecimal varCosts) {
        this.varCosts = varCosts;
    }
    
}
