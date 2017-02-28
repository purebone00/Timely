package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Workpack_seq database table.
 * 
 */
@Entity
public class Workpack_seq implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="wp_id")
	private int wpId;

	public Workpack_seq() {
	}

	public int getWpId() {
		return this.wpId;
	}

	public void setWpId(int wpId) {
		this.wpId = wpId;
	}

}