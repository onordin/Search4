package search4.helpers;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class SingleFileReaderAndWriter {

    public Long readDate() {
        Long date = null;
        try {
            File file = new File(getClass().getResource("../../guidebox_date.date").getFile());
            System.out.println("Reading from file "+file.toString());
            Scanner scanner = new Scanner(file);
            String dateString = scanner.nextLine();
            date = Long.parseLong(dateString);
        } catch (Exception e) {
            System.err.println("Error reading guidebox_date.date file: "+e);
        }
        return date;
    }

    public void writeDate(Long date) {
        try {
            String dateString = date.toString();
            File file = new File(getClass().getResource("../../guidebox_date.date").getFile());
            System.out.println("Writing to "+file.toString());
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(dateString);
            writer.close();
        } catch (Exception e) {
            System.err.println("Error writing to guidebox_date.date file: "+e);
        }
    }
}
