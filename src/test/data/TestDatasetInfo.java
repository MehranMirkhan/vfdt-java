package test.data;

import org.junit.Test;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;

public class TestDatasetInfo {
    @Test
    public void testBuild() {
        AttributeInfo a0 = new AttributeInfo(AttributeInfo.AttributeType.NOMINAL)
                .name("a0")
                .values("v0", "v1", "v2");
        AttributeInfo a1 = new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL)
                .name("a1");
        DatasetInfo dsInfo = new DatasetInfo(a0, a1);
    }
}
