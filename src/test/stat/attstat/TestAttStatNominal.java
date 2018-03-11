package test.stat.attstat;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.stat.attstat.AttStatNominal;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 11
 */
public class TestAttStatNominal {
    @Test
    public void test() {
        AttributeInfo[] attsInfo = new AttributeInfo[]{
                new AttributeInfo(AttributeInfo.AttributeType.NOMINAL).values("v0", "v1"),
                new AttributeInfo(AttributeInfo.AttributeType.NOMINAL).values("c0")
        };
        Attribute<String> label   = new Attribute<>(attsInfo[1], "c0");
        AttStatNominal asn = new AttStatNominal(1, attsInfo[0].getValues());
        asn.update(new Attribute<>(attsInfo[0], "v0"), label);
        asn.update(new Attribute<>(attsInfo[0], "v1"), label);
        asn.update(new Attribute<>(attsInfo[0], "v0"), label);
        Assert.assertEquals(asn.getClassDist()[0].getCounts().getCount(0), 2., 1e-6);
        Assert.assertEquals(asn.getClassDist()[0].getCounts().getCount(1), 1., 1e-6);
    }
}
