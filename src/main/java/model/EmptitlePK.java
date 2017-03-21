package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the Emptitle database table.
 * 
 */
public class EmptitlePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    private int etEmpID;

    private int etTitID;

    public EmptitlePK() {
    }

    public int getEtEmpID() {
        return this.etEmpID;
    }

    public void setEtEmpID(int etEmpID) {
        this.etEmpID = etEmpID;
    }

    public int getEtTitID() {
        return this.etTitID;
    }

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