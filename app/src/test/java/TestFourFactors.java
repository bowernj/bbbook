import java.util.Optional;
import org.junit.Test;
import org.nb.bbbook.algo.fourfactor.TeamModel;
import org.nb.bbbook.model.BoxScore;

public class TestFourFactors {
    
    @Test
    public void testTeamModel() {
        final BoxScore bs = new BoxScore(
            "0",
            "FOO",
            "BAR",
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0.520f,
            0.125f,
            0.232f,
            .203f,
            0.520f,
            0.125f,
            0.232f,
            .203f
        );

        final Optional<TeamModel> opt = TeamModel.fromBoxScore("FOO", bs);
        opt.ifPresent(m ->System.out.println(m));
    }

    @Test
    public void testAverageScale() {
        float min = -5.5f;
        float max = 7.2f;

        float vals[] = { -5f, -1f, 0.0f, 1f, 3f, 7f };

        for (float val : vals) {
            final float s = scale(min, max, val);
            System.out.println(val + " = " + s);
        }

        // TODO Create a new persistable model that uses season stats to calculate
        // the four factor min max,
        // the net rating min max,
        // and maybe some other things, pace, etc?
        // and then create a [a,b] scale that can then be used to transform the FF score
        // into a more meaningful point spread.


    }

    public float scale(float min, float max, float x) {
        // x' = (b - a) * (x - min)/(max-min) + a

        //[-1, 1]
        return 2 * (x - min)/(max - min) -1;
        //[0, 10]
//        return 10 * ((x - min)/(max - min)) + 0;
    }
}
