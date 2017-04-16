package utility.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents monthly reports.
 * @author Timely
 * @version 1.0
 *
 */
public class MonthlyReport implements Comparable<MonthlyReport> {
    /**
     * The rows that make up the monthly report.
     */
    List<MonthlyReportRow> rows;
    
    /**
     * The month of the monthly report.
     */
    String month;
    
    /**
     * Flag indicating whether this monthly report has been viewed or not.
     */
    int visited;
    
    /**
     * Default ctor.
     */
    public MonthlyReport() {
        rows = new ArrayList<MonthlyReportRow>();
        visited = 0;
    }
    
    /**
     * Make a monthly report.
     * @param month month
     */
    public MonthlyReport(String month) {
        rows = new ArrayList<MonthlyReportRow>();
        this.month = month;
        visited = 0;
    }
    
    /**
     * Make a monthly report.
     * @param rows rows of the report.
     * @param month month
     */
    public MonthlyReport(List<MonthlyReportRow> rows, String month) {
        this.rows = rows;
        this.month = month;
        visited = 0;
    }

    /**
     * Get rows.
     * @return rows
     */
    public List<MonthlyReportRow> getRows() {
        return rows;
    }

    /**
     * Set rows.
     * @param rows rows
     */
    public void setRows(List<MonthlyReportRow> rows) {
        this.rows = rows;
    }

    /**
     * Get month.
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Set month.
     * @param month month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Get visited.
     * @return visited
     */
    public int getVisited() {
        return visited;
    }

    /**
     * Set visited.
     * @param visited visited
     */
    public void setVisited(int visited) {
        this.visited = visited;
    }
    
    /**
     * Add a row to the monthly report.
     * @param row row to add
     */
    public void addRow(MonthlyReportRow row) {
        rows.add(row);
    }

    @Override
    public int compareTo(MonthlyReport o) {
        return o.getMonth().compareTo(this.getMonth());
    }
}
