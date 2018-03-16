package test.measure.impurity;

import org.junit.Test;
import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.GainBase;
import vfdt.measure.gain.Split;
import vfdt.measure.impurity.GiniIndex;
import vfdt.measure.impurity.Impurity;
import vfdt.measure.impurity.InformationEntropy;
import vfdt.measure.impurity.MisclassificationError;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class TestCompareImpurities {
    @Test
    public void testCompare() throws Exception {
        Impurity impurity1 = new InformationEntropy();
        Impurity impurity2 = new GiniIndex();
        Impurity impurity3 = new MisclassificationError();

        Gain gain1 = new GainBase(impurity1, 1e-6);
        Gain gain2 = new GainBase(impurity2, 1e-6);
        Gain gain3 = new GainBase(impurity3, 1e-6);

        int[] numData = {10, 100, 1000, 10000};
        for (int n : numData) {
            Counts original = new Counts(n * 0.4, n * 0.3, n * 0.3);
            Counts b1 = new Counts(n * 0.5 * 3/4, n * 0.5 * 1/4, n * 0.5 * 0/4);
            Counts b2 = new Counts(n * 0.5 * 1/6, n * 0.5 * 2/6, n * 0.5 * 3/6);
            Split split = new Split(original, new Counts[]{b1, b2});
//            System.out.println(
//                    "N=" + n +
//                    " / GH=" + gain1.measure(split) +
//                    " / GG=" + gain2.measure(split) +
//                    " / GM=" + gain3.measure(split));
        }
    }
}
