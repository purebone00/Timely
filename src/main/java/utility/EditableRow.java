package utility;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import model.Tsrow;

public class EditableRow extends Tsrow implements Serializable{
    
    private int tsrEmpId;
    private String tsrWkEnd;
    private int tsrProjNo;
    private String tsrWpNo;
    private BigDecimal tsrSat;
    private BigDecimal tsrSun;
    private BigDecimal tsrMon;
    private BigDecimal tsrTue;
    private BigDecimal tsrWed;
    private BigDecimal tsrThu;
    private BigDecimal tsrFri;
    private String tsrNote;
    private short tsrDel;
    private Date tsrInsDt;
    private Date tsrUpDt;

    
    public EditableRow() {
        super();
    }
    
    public EditableRow(int tsrEmpId, String tsrWkEnd, int tsrProjNo, String tsrWpNo, short tsrDel, Date tsrInsDt,
            Date tsrUpDt) {
        super(tsrEmpId, tsrWkEnd, tsrProjNo, tsrWpNo, tsrDel, tsrInsDt,
                tsrUpDt);
        this.tsrEmpId = tsrEmpId;
        this.tsrWkEnd = tsrWkEnd;
        this.tsrProjNo = tsrProjNo;
        this.tsrWpNo = tsrWpNo;
        this.tsrDel = tsrDel;
        this.tsrInsDt = tsrInsDt;
        this.tsrUpDt = tsrUpDt;
    }

    public EditableRow(int tsrEmpId, String tsrWkEnd, int tsrProjNo, String tsrWpNo, BigDecimal tsrSat, BigDecimal tsrSun,
            BigDecimal tsrMon, BigDecimal tsrTue, BigDecimal tsrWed, BigDecimal tsrThu, BigDecimal tsrFri,
            String tsrNote, short tsrDel, Date tsrInsDt, Date tsrUpDt) {
        super(tsrEmpId, tsrWkEnd, tsrProjNo, tsrWpNo, tsrSat, tsrSun,
                tsrMon, tsrTue, tsrWed, tsrThu, tsrFri,
                tsrNote, tsrDel, tsrInsDt, tsrUpDt );      
        this.tsrEmpId = tsrEmpId;
        this.tsrWkEnd = tsrWkEnd;
        this.tsrProjNo = tsrProjNo;
        this.tsrWpNo = tsrWpNo;
        this.tsrSat = tsrSat;
        this.tsrSun = tsrSun;
        this.tsrMon = tsrMon;
        this.tsrTue = tsrTue;
        this.tsrWed = tsrWed;
        this.tsrThu = tsrThu;
        this.tsrFri = tsrFri;
        this.tsrNote = tsrNote;
        this.tsrDel = tsrDel;
        this.tsrInsDt = tsrInsDt;
        this.tsrUpDt = tsrUpDt;
    }
    

    
    public int getTsrEmpId() {
        return this.tsrEmpId;
    }

    public void setTsrEmpId(int tsrEmpId) {
        this.tsrEmpId = tsrEmpId;
    }

  
    public String getTsrWkEnd() {
        return this.tsrWkEnd;
    }

    public void setTsrWkEnd(String tsrWkEnd) {
        this.tsrWkEnd = tsrWkEnd;
    }

  
    public int getTsrProjNo() {
        return this.tsrProjNo;
    }

    public void setTsrProjNo(int tsrProjNo) {
        this.tsrProjNo = tsrProjNo;
    }

    
    public String getTsrWpNo() {
        return this.tsrWpNo;
    }

    public void setTsrWpNo(String tsrWpNo) {
        this.tsrWpNo = tsrWpNo;
    }

    public BigDecimal getTsrSat() {
        return this.tsrSat;
    }

    public void setTsrSat(BigDecimal tsrSat) {
        this.tsrSat = tsrSat;
    }

    public BigDecimal getTsrSun() {
        return this.tsrSun;
    }

    public void setTsrSun(BigDecimal tsrSun) {
        this.tsrSun = tsrSun;
    }

    public BigDecimal getTsrMon() {
        return this.tsrMon;
    }

    public void setTsrMon(BigDecimal tsrMon) {
        this.tsrMon = tsrMon;
    }

    public BigDecimal getTsrTue() {
        return this.tsrTue;
    }

    public void setTsrTue(BigDecimal tsrTue) {
        this.tsrTue = tsrTue;
    }

    public BigDecimal getTsrWed() {
        return this.tsrWed;
    }

    public void setTsrWed(BigDecimal tsrWed) {
        this.tsrWed = tsrWed;
    }

    public BigDecimal getTsrThu() {
        return this.tsrThu;
    }

    public void setTsrThu(BigDecimal tsrThu) {
        this.tsrThu = tsrThu;
    }

    public BigDecimal getTsrFri() {
        return this.tsrFri;
    }

    public void setTsrFri(BigDecimal tsrFri) {
        this.tsrFri = tsrFri;
    }

    public String getTsrNote() {
        return this.tsrNote;
    }

    public void setTsrNote(String tsrNote) {
        this.tsrNote = tsrNote;
    }

    public short getTsrDel() {
        return this.tsrDel;
    }

    public void setTsrDel(short tsrDel) {
        this.tsrDel = tsrDel;
    }

    public Date getTsrInsDt() {
        return this.tsrInsDt;
    }

    public void setTsrInsDt(Date tsrInsDt) {
        this.tsrInsDt = tsrInsDt;
    }

    public Date getTsrUpDt() {
        return this.tsrUpDt;
    }

    public void setTsrUpDt(Date tsrUpDt) {
        this.tsrUpDt = tsrUpDt;
    }
    
}
