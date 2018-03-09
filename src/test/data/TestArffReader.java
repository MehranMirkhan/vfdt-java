package test.data;

import org.junit.Test;
import vfdt.data.ArffReader_old;
import vfdt.data.Instance;

import java.util.Iterator;

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
        ArffReader_old reader = new ArffReader_old(fileName);
        reader.init();
        String datasetInfo = reader.getDatasetInfo().toString();
//        System.out.println(datasetInfo);
        reader.close();
    }

    @Test
    public void testRead() throws Exception {
        String fileName = "src/test/data/sample.arff";
        ArffReader_old reader = new ArffReader_old(fileName);
        reader.init();
        Iterator<Instance> it = reader.iterator();
        for (int i=0; i<3; i++) {
            Instance data = it.next();
            int index = reader.getInstanceIndex();
//            System.out.println(index + ": " + data);
        }
        reader.close();
    }
}
