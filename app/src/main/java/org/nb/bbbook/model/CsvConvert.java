package org.nb.bbbook.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvConvert {

    public static List<Game> read(String path) {
        List<Game> list = new ArrayList<>();
        final Iterable<CSVRecord> records;
        // Wed Nov 21 2018
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d yyyy h:mm a", Locale.ENGLISH);
        final AtomicInteger id = new AtomicInteger(0);
        try (Reader in = new FileReader(path)) {
            records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                //DateUtil,Start (ET),Visitor/Neutral,PTS,Home/Neutral,PTS,,,Attend.,Notes
                if (!record.get(0).equals("Date")) {
                    String datePlusTime = record.get(0) + " " + record.get(1)
                        .replace("p", " PM").replace("a", " AM");
                    list.add(
                        new Game(
                            id.getAndIncrement(),
                            dateFormat.parse(datePlusTime).toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate(),
                            record.get(1),
                            record.get(2),
                            record.get(3).isEmpty() ? 0 : Integer.parseInt(record.get(3)),
                            record.get(4),
                            record.get(5).isEmpty() ? 0 : Integer.parseInt(record.get(5)),
                            record.get(9)
                        )
                    );
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

}
