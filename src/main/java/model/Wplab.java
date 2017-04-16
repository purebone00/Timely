package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Entities for Wplab table.
 * @author Timely
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "Wplab")
public class Wplab implements Serializable {
	
    /**
     * Whether this wplab is editable or not.
     */
	private boolean editable = false;
	
	/**
	 * Wplab id.
	 */
	private WplabId id;
	
	/**
	 * Hours planned for this work package labour grade.
	 */
	private BigDecimal wlPlanHrs;
	
	/**
	 * Delete flag.
	 */
	private short wlDel;
	
	/**
	 * Insert date.
	 */
    private Date wlInsDt;
    
    /**
     * Update date.
     */
    private Date wlUpDt;
    
    /**
     * Default ctor.
     */
    public Wplab() {
    }
    
    /**
     * Create a Wplab.
     * @param id WplabId
     * @param wlPlanHrs hours
     * @param etDel delete flag
     * @param etInsDt insert date
     * @param etUpDt update date
     */
    public Wplab(WplabId id, BigDecimal wlPlanHrs, short etDel, Date etInsDt, Date etUpDt) {
    	this.id = id;
    	this.wlPlanHrs = wlPlanHrs;
    	this.wlDel = etDel;
    	this.wlInsDt = etInsDt;
        this.wlUpDt = etUpDt;
    }
    
    /**
     * Get id.
     * @return id
     */
    @EmbeddedId
    
    @AttributeOverrides({ @AttributeOverride(name = "wlProjNo", column = @Column(name = "wlProjNo", nullable = false)),
        @AttributeOverride(name = "wlWpNo", column = @Column(name = "wlWpNo", nullable = false, length = 8)),
        @AttributeOverride(name = "wlLgId", column = @Column(name = "wlLgId", nullable = false, length = 2))})
    public WplabId getId() {
    	return this.id;
    }
    
    /**
     * Set id.
     * @param id id
     */
    public void setId(WplabId id) {
    	this.id = id;
    }
    
    /**
     * Get wlPlanHrs.
     * @return wlPlanHrs
     */
    @Column(name = "wlPlanHrs")
    public BigDecimal getWlPlanHrs() {
    	return this.wlPlanHrs;
    }
    
    /**
     * Set wlPlanHrs.
     * @param wlPlanHrs wlPlanHrs
     */
    public void setWlPlanHrs(BigDecimal wlPlanHrs) {
    	this.wlPlanHrs = wlPlanHrs;
    }
    
    /**
     * Get wlDel.
     * @return wlDel
     */
    @Column(name = "wlDel", nullable = false)
    public short getWlDel() {
        return this.wlDel;
    }

    /**
     * Set wlDel.
     * @param wlDel wlDel
     */
    public void setWlDel(short wlDel) {
        this.wlDel = wlDel;
    }

    /**
     * Get wlInsDt.
     * @return wlInsDt
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wlInsDt", insertable=false, nullable = false, length = 19)
    public Date getWlInsDt() {
        return this.wlInsDt;
    }

    /**
     * Set wlInsDt.
     * @param wlInsDt wlInsDt
     */
    public void setWlInsDt(Date wlInsDt) {
        this.wlInsDt = wlInsDt;
    }

    /**
     * Get wlUpDt.
     * @return wlUpDt
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wlUpDt", insertable=false, nullable = false, length = 19)
    public Date getWlUpDt() {
        return this.wlUpDt;
    }

    /**
     * Set wlUpDt.
     * @param wlUpDt wlUpDt
     */
    public void setWlUpDt(Date wlUpDt) {
        this.wlUpDt = wlUpDt;
    }
    
    public String toString() {
    	return this.id.getWlLgId() + ": " + this.wlPlanHrs;
    }
    
    @Override
    public int hashCode() {
    	int result = 17;

        result = 37 * result + this.getId().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object other) {
    	if ((this == other)) {    	    
    	    return true;
    	}
        if ((other == null)) {            
            return false;
        }
        if (!(other instanceof Wplab)) {            
            return false;
        }
        Wplab castOther = (Wplab) other;

        return (this.getId().equals(castOther.getId()));
    }
    
    /* =============================================== */
    
    /**
     * Get editable. 
     * @return editable
     */
    @Transient
    public boolean getEditable() {
        return editable;
    }
    
    /**
     * Set editable.
     * @param editable editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    /**
     * Get the wl plan days.
     * @return wl plan days.
     */
    @Transient
    public BigDecimal getWlPlanDays() {
    	if (getWlPlanHrs() == null) {
    		return BigDecimal.ZERO;
    	} else {    		
    		return getWlPlanHrs().divide(new BigDecimal(8));
    	}
    }
    
    /**
     * Set the wl plan days.
     * @param wlPlanDays wl plan days.
     */
    public void setWlPlanDays(BigDecimal wlPlanDays) {
    	setWlPlanHrs(wlPlanDays.multiply(new BigDecimal(8)));
    }
 
}
