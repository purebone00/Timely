package model;

import java.io.Serializable;

/**
 * The primary key class for the Emptitle database table.
 * 
 */
public class EmptitlePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    /**
     * Employee ID.
     */
    private int etEmpID;

    /**
     * Title ID.
     */
    private int etTitID;

    /**
     * Default ctor.
     */
    public EmptitlePK() {
    }

    /**
     * Get etEmpID.
     * @return etEmpID
     */
    public int getEtEmpID() {
        return this.etEmpID;
    }

    /**
     * Set etEmpID.
     * @param etEmpID etEmpID
     */
    public void setEtEmpID(int etEmpID) {
        this.etEmpID = etEmpID;
    }

    /**
     * Get etTitID.
     * @return etTitID
     */
    public int getEtTitID() {
        return this.etTitID;
    }

    /**
     * Set etTitID.
     * @param etTitID etTitID
     */
    public void setEtTitID(int etTitID) {
        this.etTitID = etTitID;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EmptitlePK)) {
            return false;
        }
        EmptitlePK castOther = (EmptitlePK) other;
        return (this.etEmpID == castOther.etEmpID) && (this.etTitID == castOther.etTitID);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.etEmpID;
        hash = hash * prime + this.etTitID;

        return hash;
    }
}