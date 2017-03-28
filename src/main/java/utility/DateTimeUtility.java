package utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateTimeUtility {

    /**
     * Gets the date of the end of the current week (Friday) in String with
     * format 'YYYYMMDD'.
     * 
     * @return end of the week.
     */
    public String getEndOfWeek() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK) != 7 ? c.get(Calendar.DAY_OF_WEEK) : 0;
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endWeek);
        int year = cal.get(Calendar.YEAR);
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

        return year + month + day;
    }

    /**
     * Gets the date of the end of a given week (Friday) in String with format
     * 'YYYYMMDD'.
     * 
     * @param date
     *            The date to get the end of the week for. Format: 'YYYYMMDD'.
     * @return end of the week.
     */
    public String getEndOfWeek(String date) {
        Integer dateYear = Integer.parseInt(date.substring(0, 4));
        Integer dateMonth = Integer.parseInt(date.substring(4, 6)) - 1;
        Integer dateDay = Integer.parseInt(date.substring(6));
        Calendar c = new GregorianCalendar();
        c.set(dateYear, dateMonth, dateDay);
        int currentDay = c.get(Calendar.DAY_OF_WEEK) != 7 ? c.get(Calendar.DAY_OF_WEEK) : 0;
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endWeek);
        int year = cal.get(Calendar.YEAR);
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

        return year + month + day;
    }
    
    /**
     * Gets the date of the end of a given month in String with format 'YYYYMMDD'.
     * @param date The date to get the end of the month for. Format: 'YYYYMMDD'.
     * @return end of the month.
     */
    public String getEndOfMonth(String date) {
        Integer dateYear = Integer.parseInt(date.substring(0, 4));
        Integer dateMonth = Integer.parseInt(date.substring(4, 6)) - 1;
        Integer dateDay = Integer.parseInt(date.substring(6));
        Calendar c = new GregorianCalendar();
        c.set(dateYear, dateMonth, dateDay);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        int year = c.get(Calendar.YEAR);
        String month = String.format("%02d", c.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", c.get(Calendar.DAY_OF_MONTH));

        return year + month + day;
    }

    /**
     * Gets a list of Week End Strings.
     * 
     * @param startDate
     *            Start date.
     * @param endDate
     *            End date.
     * @return List of Week End strings.
     */
    public List<String> getListOfWeekEnds(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<String>();
        String first = getEndOfWeek(startDate);
        String last = getEndOfWeek(endDate);
        String current = last;

        list.add(current);

        while (!current.equals(first)) {
            Integer dateYear = Integer.parseInt(current.substring(0, 4));
            Integer dateMonth = Integer.parseInt(current.substring(4, 6)) - 1;
            Integer dateDay = Integer.parseInt(current.substring(6));

            Calendar c = new GregorianCalendar();
            c.set(dateYear, dateMonth, dateDay);
            c.add(Calendar.DAY_OF_MONTH, -7);
            Date lastWeek = c.getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastWeek);
            int year = cal.get(Calendar.YEAR);
            String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

            current = year + month + day;

            list.add(current);
        }

        return list;
    }
    
    /**
     * Gets a list of months in String format ('YYYYMM') between a given start date and end date.
     * @param startDate the start date.
     * @param endDate the end date.
     * @return a list of months in String format ('YYYYMM').
     */
    public Set<String> getListOfMonths(String startDate, String endDate) {
        HashSet<String> set = new HashSet<String>();
        String first = getEndOfMonth(startDate);
        String last = getEndOfMonth(endDate);
        String current = last;
        
        set.add(current.substring(0, 6));
        
        while (!current.equals(first)) {
            Integer dateYear = Integer.parseInt(current.substring(0, 4));
            Integer dateMonth = Integer.parseInt(current.substring(4, 6)) - 1;
            Integer dateDay = Integer.parseInt(current.substring(6));

            Calendar c = new GregorianCalendar();
            c.set(dateYear, dateMonth, dateDay);
            c.add(Calendar.MONTH, -1);
            Date lastWeek = c.getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastWeek);
            int year = cal.get(Calendar.YEAR);
            String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

            current = getEndOfMonth(year + month + day);
            
            set.add(current.substring(0, 6));
        }
        
        return set;
    }
}
