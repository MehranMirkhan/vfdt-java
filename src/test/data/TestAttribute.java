package test.data;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.Attribute;

public class TestAttribute {
    @Test
    public void testInteger() {
        Attribute<Integer> a0 = new Attribute<>(5);
        Assert.assertEquals(5, (int)a0.getValue());
    }

    @Test
    public void testString() {
        Attribute<String> a1 = new Attribute<>("c1");
        Assert.assertEquals("c1", a1.getValue());
    }
}
