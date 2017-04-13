package utility.models;

import java.util.ArrayList;
import java.util.List;

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
    
    public MonthlyReport() {
        rows = new ArrayList<MonthlyReportRow>();
        visited = 0;
    }
    
    public MonthlyReport(String month) {
        rows = new ArrayList<MonthlyReportRow>();
        this.month = month;
        visited = 0;
    }
    
    public MonthlyReport(List<MonthlyReportRow> rows, String month) {
        this.rows = rows;
        this.month = month;
        visited = 0;
    }

    public List<MonthlyReportRow> getRows() {
        return rows;
    }

    public void setRows(List<MonthlyReportRow> rows) {
        this.rows = rows;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }
    
    /**
     * Add a row to the monthly report.
     * @param row
     */
    public void addRow(MonthlyReportRow row) {
        rows.add(row);
    }

    @Override
    public int compareTo(MonthlyReport o) {
        return o.getMonth().compareTo(this.getMonth());
    }
}
