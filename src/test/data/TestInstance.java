package test.data;

import org.junit.Test;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.Instance;

public class TestInstance {
    @Test
    public void testBasic() {
        Attribute<Integer> a0 = new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL), 5);
        Attribute<String> a1 = new Attribute<>(new AttributeInfo(AttributeInfo.AttributeType.NOMINAL), "c1");
        Attribute[] atts = new Attribute[]{a0, a1};
        Instance i = new Instance(atts);
        Instance ii = new Instance(a0, a1);
    }
}
