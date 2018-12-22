import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.Test;

public class TestDateTime {

    @Test
    public void testDates() {

        LocalDate now = LocalDate.now(ZoneId.of("America/New_York"));
        System.out.println(now);

        LocalDate minusDays = now.minusDays(21);
        System.out.println(minusDays);

    }


}
