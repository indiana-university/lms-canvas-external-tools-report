package edu.iu.uits.lms.externaltoolsreport.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExternalToolsReportUtils {
    
    /**
     * This returns a map of the line number in the csv for all the fields. This assumes the first line (0)
     * contains the header line. So the smallest line number requested is 1
     * @param csvContents
     * @param lineNumber
     * @return Map of requested line number indexed by key of column name defined by the header (row 0)
     * Example:
     * Line 0 - username, fruit
     * Line 1 - me, apple
     * Line 2 - you, orange
     *
     * if called with lineNumber=1:
     * lineMappedContent.get("username") -> me
     * lineMappedContent.get("fruit") -> apple
     */
    public static Map<String, String> createCsvLineDataMap(List<String[]> csvContents, int lineNumber) {
        Map<String, String> lineMappedContent = new HashMap<>();

        if (csvContents.size() > 1 &&
                (lineNumber > 0 && lineNumber <= csvContents.size()) ) {
            String[] csvHeader = csvContents.get(0);

            String[] line = csvContents.get(lineNumber);

            for (int index = 0; index < line.length; index++) {
                String headerColumnName = csvHeader[index].toLowerCase().trim();
                String lineColumnValue = line[index].trim();

                if (! headerColumnName.isEmpty() && ! lineColumnValue.isEmpty()) {
                    lineMappedContent.put(headerColumnName, lineColumnValue);
                }
            }

        }

        return lineMappedContent;
    }


}
