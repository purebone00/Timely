package EditableTimesheets;

import model.Employee;
import model.Timesheet;
import model.TimesheetId;
import model.Tsrow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



@SuppressWarnings("serial")
public class EditableTimesheet extends Timesheet {

    /** Sees if a time sheet is editable. For now it is always true. */
    private boolean isEditable;

    /** Saves the state on if someone is currently editing this timesheet. */
    private boolean editing;



    public EditableTimesheet() {
        super();
        isEditable = true;
        editing = false;
        this.addRow();
        this.addRow();
        this.addRow();
        this.addRow();
        this.addRow();
    }


    public EditableTimesheet(final Employee newEmployee, String wkEnd, BigDecimal tsTotal, BigDecimal tsOverTm, BigDecimal tsFlexTm, Date tsSignDt,
            Short tsSubmit, Date tsApprDt, Integer tsApprId, short tsDel, Date tsInsDt, Date tsUpDt, 
            final List<Tsrow> list) {
        super(new TimesheetId(newEmployee.getEmpId(), wkEnd), tsTotal,  tsOverTm,  tsFlexTm,  tsSignDt,
                 tsSubmit,  tsApprDt,  tsApprId,  tsDel,  tsInsDt,  tsUpDt);

        isEditable = true;
        editing = false;
    }


    public boolean isEditing() {
        return editing;
    }


    public void setEditing(boolean editing) {
        this.editing = editing;
    }


    public boolean isEditable() {
        return isEditable;
    }


    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


    public void editPage() {
        if (isEditable()) {
            setEditing(!isEditing());
        }
    }


    public String amIEditing() {
        if (editing) {
            return "Save";
        }
        return "Edit";
    }

}
