package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the Empwp database table.
 * 
 */
@Embeddable
public class EmpwpPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    /**
     * Employee Id.
     */
    @Column(insertable = false, updatable = false)
    private int ewEmpID;

    /**
     * Project number.
     */
    @Column(insertable = false, updatable = false)
    private int ewProjNo;

    /**
     * Work package number.
     */
    @Column(insertable = false, updatable = false)
    private String ewWpNo;

    /**
     * Default ctor.
     */
    public EmpwpPK() {
    }

    /**
     * Get ewEmpID.
     * @return ewEmpID
     */
    public int getEwEmpID() {
        return this.ewEmpID;
    }

    /**
     * Set ewEmpID.
     * @param ewEmpID ewEmpID
     */
    public void setEwEmpID(int ewEmpID) {
        this.ewEmpID = ewEmpID;
    }

    /**
     * Get ewProjNo.
     * @return ewProjNo
     */
    public int getEwProjNo() {
        return this.ewProjNo;
    }

    /**
     * Set ewProjNo.
     * @param ewProjNo ewProjNo
     */
    public void setEwProjNo(int ewProjNo) {
        this.ewProjNo = ewProjNo;
    }

    /**
     * Get ewWpNo.
     * @return ewWpNo
     */
    public String getEwWpNo() {
        return this.ewWpNo;
    }

    /**
     * Set ewWpNo.
     * @param ewWpNo ewWpNo
     */
    public void setEwWpNo(String ewWpNo) {
        this.ewWpNo = ewWpNo;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EmpwpPK)) {
            return false;
        }
        EmpwpPK castOther = (EmpwpPK) other;
        return (this.ewEmpID == castOther.ewEmpID) && (this.ewProjNo == castOther.ewProjNo)
                && this.ewWpNo.equals(castOther.ewWpNo);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.ewEmpID;
        hash = hash * prime + this.ewProjNo;
        hash = hash * prime + this.ewWpNo.hashCode();

        return hash;
    }
}