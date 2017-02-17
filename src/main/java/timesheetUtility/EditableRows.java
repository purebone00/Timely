package timesheetUtility;

import java.io.Serializable;

import model.Tsrow;

public class EditableRows implements Serializable {

    private Tsrow thisRow;
    
    public EditableRows(EditableTimesheet ts) {
        this.thisRow = new Tsrow(
                    ts.getTimesheet().getTsEmpId(),
                    ts.getTimesheet().getTsWkEnd(),
                    0,
                    "",
                    (short)0,
                    EditableTimesheet.getCurrentDate(),
                    EditableTimesheet.getCurrentDate()
                );
        
    }

    public Tsrow getThisRow() {
        return thisRow;
    }

    public void setThisRow(Tsrow thisRow) {
        this.thisRow = thisRow;
    }
    
}
