package model;
// Generated 15-Feb-2017 2:38:53 PM by Hibernate Tools 3.5.0.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Title generated by hbm2java
 */
@Entity
@Table(name = "Title")
public class Title implements java.io.Serializable {

    private Integer titId;
    private String titNm;
    private short titDel;
    private Date titInsDt;
    private Date titUpDt;

    public Title() {
    }

    public Title(String titNm, short titDel, Date titInsDt, Date titUpDt) {
        this.titNm = titNm;
        this.titDel = titDel;
        this.titInsDt = titInsDt;
        this.titUpDt = titUpDt;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "titID", unique = true, nullable = false)
    public Integer getTitId() {
        return this.titId;
    }

    public void setTitId(Integer titId) {
        this.titId = titId;
    }

    @Column(name = "titNm", nullable = false, length = 32)
    public String getTitNm() {
        return this.titNm;
    }

    public void setTitNm(String titNm) {
        this.titNm = titNm;
    }

    @Column(name = "titDel", nullable = false)
    public short getTitDel() {
        return this.titDel;
    }

    public void setTitDel(short titDel) {
        this.titDel = titDel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "titInsDt", nullable = false, length = 19)
    public Date getTitInsDt() {
        return this.titInsDt;
    }

    public void setTitInsDt(Date titInsDt) {
        this.titInsDt = titInsDt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "titUpDt", nullable = false, length = 19)
    public Date getTitUpDt() {
        return this.titUpDt;
    }

    public void setTitUpDt(Date titUpDt) {
        this.titUpDt = titUpDt;
    }

}
