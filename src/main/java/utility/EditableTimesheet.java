package utility;

import java.math.BigDecimal;
import java.util.Date;

import model.Timesheet;
import model.TimesheetId;

public class EditableTimesheet extends Timesheet{
    
    private boolean isEditable;

    private boolean editing;
    
    public EditableTimesheet(TimesheetId id, short tsDel, Date tsInsDt, Date tsUpDt) {
        super(id, tsDel, tsInsDt, tsUpDt);
    }

    public EditableTimesheet(TimesheetId id, BigDecimal tsTotal, BigDecimal tsOverTm, BigDecimal tsFlexTm, Date tsSignDt,
            Short tsSubmit, Date tsApprDt, Integer tsApprId, short tsDel, Date tsInsDt, Date tsUpDt) {
        super(id, tsTotal, tsOverTm, tsFlexTm, tsSignDt,
                tsSubmit, tsApprDt, tsApprId, tsDel, tsInsDt, tsUpDt);
    }
    
    /**
     * Returns current state of editing.
     * @return state of editing.
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * Sets current state of editing.
     * @param editing.
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     * Returns Whether or not this page is editable.
     * @return editable
     */
    public boolean isEditable() {
        return isEditable;
    }

    /**
     * Sets whether or not this page will be editable in the future.
     * @param isEditable state
     */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * Flips the state of editing.
     */
    public void editPage() {
        if (isEditable()) {
            setEditing(!isEditing());
        }
    }

    /**
     * returns the appropriate string for the button.
     * @return Save or Edit
     */
    public String amIEditing() {
        if (editing) {
            return "Save";
        }
        return "Edit";
    }

    
}
