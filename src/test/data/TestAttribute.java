package test.data;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;

public class TestAttribute {
    @Test
    public void testInteger() {
        Attribute<Integer> a0 = new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL), 5);
        Assert.assertEquals(5, (int)a0.getValue());
    }

    @Test
    public void testString() {
        Attribute<String> a1 = new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NOMINAL), "c1");
        Assert.assertEquals("c1", a1.getValue());
    }
}
