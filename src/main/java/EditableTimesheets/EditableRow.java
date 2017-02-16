package EditableTimesheets;


import model.Tsrow;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Expands on the existing TimesheetRows class to make it interact with
 * the jsfPage Timesheet easier.
 * 
 * @author Albert
 *
 */
@SuppressWarnings("serial")
public class EditableRow extends Tsrow implements Serializable {

    private int rowId;

    public EditableRow() {
        super();
    }

    public EditableRow(int tsrEmpId, String tsrWkEnd, int tsrProjNo, String tsrWpNo, BigDecimal tsrSat, BigDecimal tsrSun,
            BigDecimal tsrMon, BigDecimal tsrTue, BigDecimal tsrWed, BigDecimal tsrThu, BigDecimal tsrFri,
            String tsrNote, short tsrDel, Date tsrInsDt, Date tsrUpDt) {
        super( tsrEmpId,  tsrWkEnd,  tsrProjNo,  tsrWpNo,  tsrSat,  tsrSun,
                 tsrMon,  tsrTue,  tsrWed,  tsrThu,  tsrFri,
                 tsrNote,  tsrDel,  tsrInsDt,  tsrUpDt);
    }
    
    /**
     * returns the number of hours for Saturday.
     * @return number of hours for Saturday.
     */
    public BigDecimal getSaturday() {   
        return super.getHour(0);
    }

    /**
     * sets the number of hours for Saturday.
     * @param hours number of hours for Saturday.
     */
    public void setTsr(BigDecimal hours) {
        super.setHour(0, hours);
    }


    /**
     * returns the number of hours for Sunday.
     * @return number of hours for Sunday.
     */
    public BigDecimal getSunday() {
        return super.getHour(1);
    }


    /**
     * sets the number of hours for Sunday.
     * @param hours number of hours for Sunday.
     */
    public void setSunday(BigDecimal hours) {
        super.setHour(1, hours);
    }


    /**
     * returns the number of hours for Monday.
     * @return number of hours for Monday.
     */
    public BigDecimal getMonday() {
        return super.getHour(2);
    }


    /**
     * sets the number of hours for Monday.
     * @param hours number of hours for Monday.
     */
    public void setMonday(BigDecimal hours) {
        super.setHour(2, hours);
    }

    /**
     * returns the number of hours for Tuesday.
     * @return number of hours for Tuesday.
     */
    public BigDecimal getTuesday() {
        return super.getHour(3);
    }


    /**
     * sets the number of hours for Tuesday.
     * @param hours number of hours for Tuesday.
     */
    public void setTuesday(BigDecimal hours) {
        super.setHour(3, hours);
    }


    /**
     * returns the number of hours for Wednesday.
     * @return number of hours for Wednesday.
     */
    public BigDecimal getWednesday() {
        return super.getHour(4);
    }


    /**
     * sets the number of hours for Wednesday.
     * @param hours number of hours for Wednesday.
     */
    public void setWednesday(BigDecimal hours) {
        super.setHour(4, hours);
    }


    /**
     * returns the number of hours for Thursday.
     * @return number of hours for Thursday.
     */
    public BigDecimal getThursday() {
        return super.getHour(5);
    }


    /**
     * sets the number of hours for Thursday.
     * @param hours number of hours for Thursday.
     */
    public void setThursday(BigDecimal hours) {
        super.setHour(5, hours);
    }


    /**
     * returns the number of hours for Friday.
     * @return number of hours for Friday.
     */
    public BigDecimal getFriday() {
        return super.getHour(6);
    }


    /**
     * sets the number of hours for Friday.
     * @param hours number of hours for Friday.
     */
    public void setFriday(BigDecimal hours) {
        super.setHour(6, hours);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }


}

