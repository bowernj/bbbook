import org.junit.Test;
import org.nb.bbbook.model.Court;

public class TestEnum {

    @Test
    public void testMethod() {
        Court c1 = Court.valueOf("all");
        System.out.println(c1);

        Court c2 = Court.valueOf("none");
        System.out.println(c2);

    }

}
