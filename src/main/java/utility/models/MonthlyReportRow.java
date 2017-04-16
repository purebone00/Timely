package utility.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import manager.TsrowManager;
import model.Tsrow;
import model.Workpack;
import model.Wplab;
import model.Wpstarep;
import utility.DateTimeUtility;

/**
 * Represents a row of a monthly report.
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class MonthlyReportRow implements Serializable, Comparable<MonthlyReportRow> {

    /**
     * Total Hours Budgeted.
     */
    private BigDecimal budgetTotalHours;

    /**
     * Total Costs Budgeted.
     */
    private BigDecimal budgetTotalCosts;

    /**
     * Total Hours Currently Completed.
     */
    private BigDecimal curTotalHours;

    /**
     * Total Costs Currently Spent.
     */
    private BigDecimal curTotalCosts;

    /**
     * Projected Total Hours.
     */
    private BigDecimal projTotalHours;

    /**
     * Projected Total Costs.
     */
    private BigDecimal projTotalCosts;

    /**
     * Variance between projTotalHours and budgetTotalHours.
     */
    private BigDecimal varTime;

    /**
     * Variance between projTotalCosts and budgetTotalCosts.
     */
    private BigDecimal varCosts;
    
    /**
     * Overtime worked.
     */
    private BigDecimal overtimeHrs;

    /**
     * The {@link Workpack} this report is for.
     */
    private Workpack workpack;

    /**
     * Visited flag.
     */
    private int visited;

    /**
     * If report is an aggregate report.
     */
    private boolean aggregate;

    /**
     * Default ctor.
     */
    public MonthlyReportRow() {
        budgetTotalHours = BigDecimal.ZERO;
        budgetTotalCosts = BigDecimal.ZERO;
        curTotalHours = BigDecimal.ZERO;
        curTotalCosts = BigDecimal.ZERO;
        projTotalHours = BigDecimal.ZERO;
        projTotalCosts = BigDecimal.ZERO;
        varTime = BigDecimal.ZERO;
        varCosts = BigDecimal.ZERO;
        overtimeHrs = BigDecimal.ZERO;
        visited = 0;
    }

    /**
     * Creates a Monthly Report.
     * 
     * @param tsrows
     *            List of Tsrows
     * @param wplabs
     *            {@link Wplab}'s of the {@link Workpack} to generate the report
     *            for.
     * @param report
     *            {@link Wpstarep} of the {@link Workpack} for the week.
     * @param rateMap
     *            Map of Labour Grades and their rates.
     */
    public MonthlyReportRow(Workpack workpack, List<Tsrow> tsrows, Set<Wplab> wplabs, Wpstarep report,
            HashMap<String, BigDecimal> rateMap, String month) {
        this.workpack = workpack;
        budgetTotalHours = BigDecimal.ZERO;
        budgetTotalCosts = BigDecimal.ZERO;
        curTotalHours = BigDecimal.ZERO;
        curTotalCosts = BigDecimal.ZERO;
        projTotalHours = BigDecimal.ZERO;
        projTotalCosts = BigDecimal.ZERO;
        varTime = BigDecimal.ZERO;
        varCosts = BigDecimal.ZERO;
        overtimeHrs = BigDecimal.ZERO;
        visited = 0;
        aggregate = false;
        
        for (Tsrow t : tsrows) {
            BigDecimal rate = t.getTimesheet().getTsPayGrade() == null ? 
                    t.getTimesheet().getEmployee().getEmpLabGrd().getLgRate() :
                    t.getTimesheet().getTsPayGrade().getLgRate();
            curTotalCosts = curTotalCosts.add(t.getTotal().multiply(rate));
            curTotalHours = curTotalHours.add(t.getTotal());
            overtimeHrs = overtimeHrs.add(t.getTsrOt() == null ? BigDecimal.ZERO : t.getTsrOt());
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
        
        if (!workpack.getCharged()) {
            projTotalCosts = budgetTotalCosts;
            projTotalHours = budgetTotalHours;
        }
        
        if (workpack.getWpStatus() != null && workpack.getWpStatus() == (short) 1) {
            DateTimeUtility dtu = new DateTimeUtility();
            if (dtu.getDateString(workpack.getWpEndDt()).compareTo(dtu.getEndOfMonth(month + "01")) <= 0) {
                projTotalCosts = curTotalCosts;
                projTotalHours = curTotalHours;
            }
        }

        if (projTotalCosts != null && projTotalHours != null) {
            if (budgetTotalCosts.doubleValue() == 0) {
                varCosts = new BigDecimal(1);
            } else {
                varCosts = ((projTotalCosts.subtract(budgetTotalCosts)).divide(budgetTotalCosts, 2,
                        RoundingMode.HALF_EVEN));
            }
            
            if (budgetTotalHours.doubleValue() == 0) {
                varTime = new BigDecimal(1);
            } else {                
                varTime = ((projTotalHours.subtract(budgetTotalHours)).divide(budgetTotalHours, 2, RoundingMode.HALF_EVEN));
            }
        } else {
            varCosts = null;
            varTime = null;
        }
    }

    /**
     * Get workpack.
     * @return workpack
     */
    public Workpack getWorkpack() {
        return this.workpack;
    }

    /**
     * Set workpack.
     * @param workpack workpack
     */
    public void setWorkpack(Workpack workpack) {
        this.workpack = workpack;
    }

    /**
     * Get budgetTotalHours.
     * @return budgetTotalHours
     */
    public BigDecimal getBudgetTotalHours() {
        return budgetTotalHours;
    }

    /**
     * Set budgetTotalHours.
     * @param budgetTotalHours budgetTotalHours
     */
    public void setBudgetTotalHours(BigDecimal budgetTotalHours) {
        this.budgetTotalHours = budgetTotalHours;
    }

    /**
     * Get getBudgetTotalDays.
     * @return getBudgetTotalDays
     */
    public BigDecimal getBudgetTotalDays() {
        return this.getBudgetTotalHours().divide(new BigDecimal(8));
    }

    /**
     * Set getBudgetTotalDays.
     * @param budgetTotalDays getBudgetTotalDays
     */
    public void setBudgetTotalDays(BigDecimal budgetTotalDays) {
        this.setBudgetTotalHours(budgetTotalDays.multiply(new BigDecimal(8)));
    }

    /**
     * Get budgetTotalCosts.
     * @return budgetTotalCosts
     */
    public BigDecimal getBudgetTotalCosts() {
        return budgetTotalCosts;
    }

    /**
     * Set budgetTotalCosts.
     * @param budgetTotalCosts budgetTotalCosts
     */
    public void setBudgetTotalCosts(BigDecimal budgetTotalCosts) {
        this.budgetTotalCosts = budgetTotalCosts;
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
     * Get projTotalHours.
     * @return projTotalHours
     */
    public BigDecimal getProjTotalHours() {
        return projTotalHours;
    }

    /**
     * Set projTotalHours.
     * @param projTotalHours projTotalHours
     */
    public void setProjTotalHours(BigDecimal projTotalHours) {
        this.projTotalHours = projTotalHours;
    }

    /**
     * Get getProjTotalDays.
     * @return getProjTotalDays
     */
    public BigDecimal getProjTotalDays() {
        return this.getProjTotalHours() == null ? null : this.getProjTotalHours().divide(new BigDecimal(8));
    }

    /**
     * Set getProjTotalDays.
     * @param projTotalDays getProjTotalDays
     */
    public void setProjTotalDays(BigDecimal projTotalDays) {
        this.setProjTotalHours(projTotalDays.multiply(new BigDecimal(8)));
    }

    /**
     * Get projTotalCosts.
     * @return projTotalCosts
     */
    public BigDecimal getProjTotalCosts() {
        return projTotalCosts;
    }

    /**
     * Set projTotalCosts.
     * @param projTotalCosts projTotalCosts
     */
    public void setProjTotalCosts(BigDecimal projTotalCosts) {
        this.projTotalCosts = projTotalCosts;
    }

    /**
     * Get varTime.
     * @return varTime
     */
    public BigDecimal getVarTime() {
        return varTime;
    }

    /**
     * Set varTime.
     * @param varTime varTime
     */
    public void setVarTime(BigDecimal varTime) {
        this.varTime = varTime;
    }

    /**
     * Get varCosts. 
     * @return varCosts
     */
    public BigDecimal getVarCosts() {
        return varCosts;
    }

    /**
     * Set varCosts.
     * @param varCosts varCosts
     */
    public void setVarCosts(BigDecimal varCosts) {
        this.varCosts = varCosts;
    }

    /**
     * Get overtimeHrs.
     * @return overtimeHrs
     */
    public BigDecimal getOvertimeHrs() {
        return overtimeHrs;
    }

    /**
     * Set overtimeHrs
     * @param overtimeHrs overtimeHrs
     */
    public void setOvertimeHrs(BigDecimal overtimeHrs) {
        this.overtimeHrs = overtimeHrs;
    }
    
    /**
     * Get getOvertimeDays.
     * @return getOvertimeDays
     */
    public BigDecimal getOvertimeDays() {
        return this.overtimeHrs.divide(new BigDecimal(8));
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

    /**
     * Get aggregate.
     * @return aggregate
     */
    public boolean getAggregate() {
        return this.aggregate;
    }

    /**
     * Set aggregate.
     * @param aggregate aggregate
     */
    public void setAggregate(boolean aggregate) {
        this.aggregate = aggregate;
    }

    /**
     * Generate a monthly report for a parent work package, which is an aggregate of a number of child work packages.
     * @param workpack The work package to generate the report for.
     * @param reports The list of child work package reports to generate this parent report with.
     * @return
     */
    public static MonthlyReportRow generateAggregate(Workpack workpack, List<MonthlyReportRow> reports) {
        MonthlyReportRow report = new MonthlyReportRow();

        for (MonthlyReportRow m : reports) {
            report.setBudgetTotalCosts(report.getBudgetTotalCosts().add(m.getBudgetTotalCosts()));
            report.setBudgetTotalHours(report.getBudgetTotalHours().add(m.getBudgetTotalHours()));
            report.setCurTotalCosts(report.getCurTotalCosts().add(m.getCurTotalCosts()));
            report.setCurTotalHours(report.getCurTotalHours().add(m.getCurTotalHours()));
            report.setOvertimeHrs(report.getOvertimeHrs().add(m.getOvertimeHrs()));

            if (m.getProjTotalCosts() == null || m.getProjTotalHours() == null) {
                report.setProjTotalCosts(null);
                report.setProjTotalHours(null);
            } else {
                if (report.getProjTotalCosts() == null) {
                    report.setProjTotalCosts(BigDecimal.ZERO);
                }
                if (report.getProjTotalHours() == null) {
                    report.setProjTotalHours(BigDecimal.ZERO);
                }
                report.setProjTotalCosts(report.getProjTotalCosts().add(m.getProjTotalCosts()));
                report.setProjTotalHours(report.getProjTotalHours().add(m.getProjTotalHours()));
            }
        }

        if (report.getProjTotalCosts() != null && report.getProjTotalHours() != null) {
            if (report.getProjTotalCosts().doubleValue() == 0) {
                report.setVarCosts(new BigDecimal(1));
            } else {
                report.setVarCosts(((report.getProjTotalCosts().subtract(report.getBudgetTotalCosts()))
                        .divide(report.getBudgetTotalCosts(), 2, RoundingMode.HALF_EVEN)));                
            }
            if (report.getProjTotalHours().doubleValue() == 0) {
                report.setVarTime(new BigDecimal(1));
            } else {                
                report.setVarTime(((report.getProjTotalHours().subtract(report.getBudgetTotalHours()))
                        .divide(report.getBudgetTotalHours(), 2, RoundingMode.HALF_EVEN)));
            }
        } else {
            report.setVarCosts(null);
            report.setVarTime(null);
        }

        report.setWorkpack(workpack);
        report.setAggregate(true);

        return report;
    }

    @Override
    public int compareTo(MonthlyReportRow o) {
        return this.getWorkpack().getId().getWpNo().compareTo(o.getWorkpack().getId().getWpNo());
    }

}
