package timsheetUtility;

import java.util.List;

import javax.inject.Named;

import model.Timesheet;

@Named
public class EditableTimesheet {
    
    private Timesheet timesheet;
    private List<EditableRows> rows;
    
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
    
}
