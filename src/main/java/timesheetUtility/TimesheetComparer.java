package timesheetUtility;

import java.util.Comparator;

public class TimesheetComparer implements Comparator<EditableTimesheet> {

    @Override
    public int compare(EditableTimesheet ts1, EditableTimesheet ts2) {
        int tsDate = Integer.parseInt(ts1.getTimesheet().getTsWkEnd());
        int tsDate2 = Integer.parseInt(ts2.getTimesheet().getTsWkEnd());
        return tsDate < tsDate2 ? -1 : tsDate > tsDate2 ? 1 : 0;
    }

}
