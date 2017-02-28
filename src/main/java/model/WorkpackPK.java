package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the Workpack database table.
 * 
 */
@Embeddable
public class WorkpackPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private int wpProjNo;

	private String wpNo;

	public WorkpackPK() {
	}
	public int getWpProjNo() {
		return this.wpProjNo;
	}
	public void setWpProjNo(int wpProjNo) {
		this.wpProjNo = wpProjNo;
	}
	public String getWpNo() {
		return this.wpNo;
	}
	public void setWpNo(String wpNo) {
		this.wpNo = wpNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WorkpackPK)) {
			return false;
		}
		WorkpackPK castOther = (WorkpackPK)other;
		return 
			(this.wpProjNo == castOther.wpProjNo)
			&& this.wpNo.equals(castOther.wpNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.wpProjNo;
		hash = hash * prime + this.wpNo.hashCode();
		
		return hash;
	}
}