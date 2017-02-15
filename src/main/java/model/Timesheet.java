package model;
// Generated 15-Feb-2017 2:38:53 PM by Hibernate Tools 3.5.0.Final

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

/**
 * Timesheet generated by hbm2java
 */
@Entity
@Table(name = "Timesheet")
public class Timesheet implements java.io.Serializable {

    private TimesheetId id;
    private BigDecimal tsTotal;
    private BigDecimal tsOverTm;
    private BigDecimal tsFlexTm;
    private Date tsSignDt;
    private Short tsSubmit;
    private Date tsApprDt;
    private Integer tsApprId;
    private short tsDel;
    private Date tsInsDt;
    private Date tsUpDt;

    public Timesheet() {
    }

    public Timesheet(TimesheetId id, short tsDel, Date tsInsDt, Date tsUpDt) {
        this.id = id;
        this.tsDel = tsDel;
        this.tsInsDt = tsInsDt;
        this.tsUpDt = tsUpDt;
    }

    public Timesheet(TimesheetId id, BigDecimal tsTotal, BigDecimal tsOverTm, BigDecimal tsFlexTm, Date tsSignDt,
            Short tsSubmit, Date tsApprDt, Integer tsApprId, short tsDel, Date tsInsDt, Date tsUpDt) {
        this.id = id;
        this.tsTotal = tsTotal;
        this.tsOverTm = tsOverTm;
        this.tsFlexTm = tsFlexTm;
        this.tsSignDt = tsSignDt;
        this.tsSubmit = tsSubmit;
        this.tsApprDt = tsApprDt;
        this.tsApprId = tsApprId;
        this.tsDel = tsDel;
        this.tsInsDt = tsInsDt;
        this.tsUpDt = tsUpDt;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "tsEmpId", column = @Column(name = "tsEmpID", nullable = false)),
            @AttributeOverride(name = "tsWkEnd", column = @Column(name = "tsWkEnd", nullable = false, length = 8)) })
    public TimesheetId getId() {
        return this.id;
    }

    public void setId(TimesheetId id) {
        this.id = id;
    }

    @Column(name = "tsTotal", precision = 5)
    public BigDecimal getTsTotal() {
        return this.tsTotal;
    }

    public void setTsTotal(BigDecimal tsTotal) {
        this.tsTotal = tsTotal;
    }

    @Column(name = "tsOverTm", precision = 4)
    public BigDecimal getTsOverTm() {
        return this.tsOverTm;
    }

    public void setTsOverTm(BigDecimal tsOverTm) {
        this.tsOverTm = tsOverTm;
    }

    @Column(name = "tsFlexTm", precision = 4)
    public BigDecimal getTsFlexTm() {
        return this.tsFlexTm;
    }

    public void setTsFlexTm(BigDecimal tsFlexTm) {
        this.tsFlexTm = tsFlexTm;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tsSignDt", length = 19)
    public Date getTsSignDt() {
        return this.tsSignDt;
    }

    public void setTsSignDt(Date tsSignDt) {
        this.tsSignDt = tsSignDt;
    }

    @Column(name = "tsSubmit")
    public Short getTsSubmit() {
        return this.tsSubmit;
    }

    public void setTsSubmit(Short tsSubmit) {
        this.tsSubmit = tsSubmit;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tsApprDt", length = 19)
    public Date getTsApprDt() {
        return this.tsApprDt;
    }

    public void setTsApprDt(Date tsApprDt) {
        this.tsApprDt = tsApprDt;
    }

    @Column(name = "tsApprID")
    public Integer getTsApprId() {
        return this.tsApprId;
    }

    public void setTsApprId(Integer tsApprId) {
        this.tsApprId = tsApprId;
    }

    @Column(name = "tsDel", nullable = false)
    public short getTsDel() {
        return this.tsDel;
    }

    public void setTsDel(short tsDel) {
        this.tsDel = tsDel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tsInsDt", nullable = false, length = 19)
    public Date getTsInsDt() {
        return this.tsInsDt;
    }

    public void setTsInsDt(Date tsInsDt) {
        this.tsInsDt = tsInsDt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tsUpDt", nullable = false, length = 19)
    public Date getTsUpDt() {
        return this.tsUpDt;
    }

    public void setTsUpDt(Date tsUpDt) {
        this.tsUpDt = tsUpDt;
    }

}
