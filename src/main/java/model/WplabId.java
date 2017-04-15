package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class WplabId implements Serializable {
	
    /**
     * Project number.
     */
	private int wlProjNo;
	
	/**
	 * Work package number.
	 */
	private String wlWpNo;
	
	/**
	 * Labour grade.
	 */
	private String wlLgId;
	
	/**
	 * Default ctor.
	 */
	public WplabId() {
	}
	
	/**
	 * Create a WplabId.
	 * @param wlProjNo project number
	 * @param wlWpNo wp number
	 * @param wlLgId labour grade id
	 */
	public WplabId(int wlProjNo, String wlWpNo, String wlLgId) {
		this.wlProjNo = wlProjNo;
		this.wlWpNo = wlWpNo;
		this.wlLgId = wlLgId;
	}
	
	/**
	 * Get wlProjNo.
	 * @return wlProjNo
	 */
	@Column(name = "wlProjNo", nullable = false)
	public int getWlProjNo() {
		return this.wlProjNo;
	}
	
	/**
	 * Set wlProjNo.
	 * @param wlProjNo wlProjNo
	 */
	public void setWlProjNo(int wlProjNo) {
		this.wlProjNo = wlProjNo;
	}
	
	/**
	 * Get wlWpNo.
	 * @return wlWpNo
	 */
	@Column(name = "wlWpNo", nullable = false)
	public String getWlWpNo() {
		return this.wlWpNo;
	}
	
	/**
	 * Set wlWpNo. 
	 * @param wlWpNo wlWpNo
 	 */
	public void setWlWpNo(String wlWpNo) {
		this.wlWpNo = wlWpNo;
	}
	
	/**
	 * Get wlLgId.
	 * @return wlLgId
	 */
	@Column(name = "wlLgId", nullable = false)
	public String getWlLgId() {
		return this.wlLgId;
	}
	
	/**
	 * Set wlLgId.
	 * @param wlLgId wlLgId
	 */
	public void setWlLgId(String wlLgId) {
		this.wlLgId = wlLgId;
	}
	
	public boolean equals(Object other) {
		if ((this == other)) {		    
		    return true;
		}
        if ((other == null)) {            
            return false;
        }
        if (!(other instanceof WplabId)) {            
            return false;
        }
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
