package test.stat.attstat;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.stat.attstat.AttStatGaussian;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 10
 */
public class TestAttStatGaussian {
    @Test
    public void testCorrectness() {
        Attribute<String> label   = new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NOMINAL).values("c0"), "c0");
        AttStatGaussian   attStat = new AttStatGaussian(1);
        attStat.update(new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL), -1.), label);
        attStat.update(new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL), 0.), label);
        attStat.update(new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL), 1.), label);
        Assert.assertEquals(attStat.getMinValue(), -1., 1e-6);
        Assert.assertEquals(attStat.getMaxValue(), 1., 1e-6);
        Assert.assertEquals(attStat.getClassDist()[0].getNumData(), 3);
        Assert.assertEquals(attStat.getClassDist()[0].getMean(), 0., 1e-6);
        Assert.assertEquals(attStat.getClassDist()[0].getSum(), 0., 1e-6);
        Assert.assertEquals(attStat.getClassDist()[0].getSumSq(), 2., 1e-6);
    }
}
