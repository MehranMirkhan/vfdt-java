package test.data;

import org.junit.Test;
import vfdt.data.AttributeInfo;

public class TestAttributeInfo {
    @Test
    public void testBuild() {
        AttributeInfo a0 = new AttributeInfo(AttributeInfo.AttributeType.NOMINAL)
                .name("a0")
                .values("v0", "v1", "v2");
        AttributeInfo a1 = new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL)
                .name("a1");
//        System.out.println(a0);
//        System.out.println(a1);
    }
}
