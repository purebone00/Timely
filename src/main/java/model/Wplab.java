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

@SuppressWarnings("serial")
@Entity
@Table(name = "Wplab")
public class Wplab implements Serializable {
	
	private boolean editable = false;
	private WplabId id;
	private BigDecimal wlPlanHrs;
	private short wlDel;
    private Date wlInsDt;
    private Date wlUpDt;
    
    public Wplab() {
    }
    
    public Wplab(WplabId id, BigDecimal wlPlanHrs, short etDel, Date etInsDt, Date etUpDt) {
    	this.id = id;
    	this.wlPlanHrs = wlPlanHrs;
    	this.wlDel = etDel;
    	this.wlInsDt = etInsDt;
        this.wlUpDt = etUpDt;
    }
    
    @EmbeddedId
    
    @AttributeOverrides({ @AttributeOverride(name = "wlProjNo", column = @Column(name = "wlProjNo", nullable = false)),
        @AttributeOverride(name = "wlWpNo", column = @Column(name = "wlWpNo", nullable = false, length = 8)),
        @AttributeOverride(name = "wlLgId", column = @Column(name = "wlLgId", nullable = false, length = 2))})
    public WplabId getId() {
    	return this.id;
    }
    
    public void setId(WplabId id) {
    	this.id = id;
    }
    
    @Column(name = "wlPlanHrs")
    public BigDecimal getWlPlanHrs() {
    	return this.wlPlanHrs;
    }
    
    public void setWlPlanHrs(BigDecimal wlPlanHrs) {
    	this.wlPlanHrs = wlPlanHrs;
    }
    
    @Column(name = "wlDel", nullable = false)
    public short getWlDel() {
        return this.wlDel;
    }

    public void setWlDel(short wlDel) {
        this.wlDel = wlDel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wlInsDt", insertable=false, nullable = false, length = 19)
    public Date getWlInsDt() {
        return this.wlInsDt;
    }

    public void setWlInsDt(Date wlInsDt) {
        this.wlInsDt = wlInsDt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wlUpDt", insertable=false, nullable = false, length = 19)
    public Date getWlUpDt() {
        return this.wlUpDt;
    }

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
    	if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof Wplab))
            return false;
        Wplab castOther = (Wplab) other;

        return (this.getId().equals(castOther.getId()));
    }
    
    /* =============================================== */
    
    @Transient
    public boolean getEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    @Transient
    public BigDecimal getWlPlanDays() {
    	if (getWlPlanHrs() == null) {
    		return BigDecimal.ZERO;
    	} else {    		
    		return getWlPlanHrs().divide(new BigDecimal(8));
    	}
    }
    
    public void setWlPlanDays(BigDecimal wlPlanDays) {
    	setWlPlanHrs(wlPlanDays.multiply(new BigDecimal(8)));
    }
 
}
