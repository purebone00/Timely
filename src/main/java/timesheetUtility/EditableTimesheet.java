package timesheetUtility;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Timesheet;

public class EditableTimesheet implements Serializable {
    
    private Timesheet timesheet;
    private List<EditableRows> rows;
    
    public EditableTimesheet() {
        this.timesheet = new Timesheet();
        timesheet.setTsWkEnd(getEndOfWeek());
        timesheet.setTsDel((short) 0);
        timesheet.setTsUpDt(getCurrentDate());
        timesheet.setTsInsDt(getCurrentDate());
    }
    
    public Timesheet getTimesheet() {
        return timesheet;
    }
    
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }
    
    public List<EditableRows> getRows() {
        return rows;
    }
    
    public void setRows(List<EditableRows> rows) {
        this.rows = rows;
    }
    
    public void setEmpId(int employeeId) {
        this.timesheet.setTsEmpId(employeeId);
    }
    
    public static String getEndOfWeek() {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date friday = new Date();
        calendar.setTime(friday);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        
        return dateFormat.format(calendar.getTime());
    }
    
    public static Date getCurrentDate() {
        return new Date();
    }
    
    public List<EditableRows> createNewRows(int numOfRows) {
        List<EditableRows> newRows = new ArrayList<>();
        for (int i = 0 ; i < numOfRows ; i ++) {
            newRows.add(new EditableRows(this));
        }
        
        return newRows;
    }
}
