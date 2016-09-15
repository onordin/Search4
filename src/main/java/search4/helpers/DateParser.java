package search4.helpers;

import java.sql.Date;

public class DateParser {

    public Date getDateFromString(String dateString) {
        return Date.valueOf(dateString);
    }
}
