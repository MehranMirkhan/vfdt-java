package test.data;

import org.junit.Test;
import vfdt.data.ArffReader;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TestArffReader {
    @Test
    public void testInit() throws Exception {
        String fileName = "src/test/data/sample.arff";
        ArffReader reader = new ArffReader(fileName);
        reader.init();
//        System.out.println(reader.getDatasetInfo());
        reader.close();
    }
}
