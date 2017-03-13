package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WplabId implements Serializable {
	
	private int wlProjNo;
	private String wlWpNo;
	private String wlLgId;
	
	public WplabId() {
	}
	
	public WplabId(int wlProjNo, String wlWpNo, String wlLgId) {
		this.wlProjNo = wlProjNo;
		this.wlWpNo = wlWpNo;
		this.wlLgId = wlLgId;
	}
	
	@Column(name = "wlProjNo", nullable = false)
	public int getWlProjNo() {
		return this.wlProjNo;
	}
	
	public void setWlProjNo(int wlProjNo) {
		this.wlProjNo = wlProjNo;
	}
	
	@Column(name = "wlWpNo", nullable = false)
	public String getWlWpNo() {
		return this.wlWpNo;
	}
	
	public void setWlWpNo(String wlWpNo) {
		this.wlWpNo = wlWpNo;
	}
	
	@Column(name = "wlLgId", nullable = false)
	public String getWlLgId() {
		return this.wlLgId;
	}
	
	public void setWlLgId(String wlLgId) {
		this.wlLgId = wlLgId;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof WplabId))
            return false;
        WplabId castOther = (WplabId) other;
        
        return (this.getWlProjNo() == castOther.getWlProjNo())
                && ((this.getWlWpNo() == castOther.getWlWpNo()) || (this.getWlWpNo() != null
                        && castOther.getWlWpNo() != null && this.getWlWpNo().equals(castOther.getWlWpNo())))
                && ((this.getWlLgId() == castOther.getWlLgId()) || (this.getWlLgId() != null
                	&& castOther.getWlLgId() != null && this.getWlLgId().equals(castOther.getWlLgId())));
	}
	
	public int hashCode() {
        int result = 17;

        result = 37 * result + this.getWlProjNo();
        result = 37 * result + (getWlWpNo() == null ? 0 : this.getWlWpNo().hashCode());
        result = 37 * result + (getWlLgId() == null ? 0 : this.getWlLgId().hashCode());
        return result;
    }
}
