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
        String a0s = a0.toString(),
                a1s = a1.toString();
//        System.out.println(a0s);
//        System.out.println(a1s);
    }
}
