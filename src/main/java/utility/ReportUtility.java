package utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ReportUtility {

    /**
     * The list of "labour grade : person days" estimates is stored as a single
     * string in the database. This method parses that string into a map.
     * 
     * @param wsrEstDes
     * @return map
     */
    public static HashMap<String, BigDecimal> parseWsrEstDes(String wsrEstDes) {
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

        String[] rows = wsrEstDes.split(",");

        for (String s : rows) {
            String[] columns = s.split(":");
            map.put(columns[0], new BigDecimal(columns[1]));
        }

        return map;
    }

    /**
     * The list of "labour grade : person days" estimates is stored as a single
     * string in the database. This method generates that string from a map.
     * 
     * @param map
     * @return wsrEstDes string
     */
    public static String unparseWsrEstDes(HashMap<String, BigDecimal> map) {
        String string = "";

        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            string = string + entry.getKey() + ":" + entry.getValue().toString() + ",";
        }

        return string.substring(0, string.length() - 1);
    }
}
