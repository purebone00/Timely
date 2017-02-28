package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the Timesheet database table.
 * 
 */

public class TimesheetPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int tsEmpID;

	private String tsWkEnd;

	public int getTsEmpID() {
		return this.tsEmpID;
	}
	public void setTsEmpID(int tsEmpID) {
		this.tsEmpID = tsEmpID;
	}
	public String getTsWkEnd() {
		return this.tsWkEnd;
	}
	public void setTsWkEnd(String tsWkEnd) {
		this.tsWkEnd = tsWkEnd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TimesheetPK)) {
			return false;
		}
		TimesheetPK castOther = (TimesheetPK)other;
		return 
			(this.tsEmpID == castOther.tsEmpID)
			&& this.tsWkEnd.equals(castOther.tsWkEnd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tsEmpID;
		hash = hash * prime + this.tsWkEnd.hashCode();
		
		return hash;
	}
}