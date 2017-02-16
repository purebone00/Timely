package timsheetUtility;

import javax.inject.Named;

import model.Tsrow;

@Named
public class EditableRows {

    private Tsrow thisRow;

    public Tsrow getThisRow() {
        return thisRow;
    }

    public void setThisRow(Tsrow thisRow) {
        this.thisRow = thisRow;
    }
    
}
