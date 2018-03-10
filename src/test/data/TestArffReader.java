package test.data;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.*;
import vfdt.util.Pair;

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
        String        fileName    = "src/test/data/sample.arff";
        DatasetReader reader      = new ArffReader(fileName);
        DatasetInfo   datasetInfo = reader.analyze();
//        System.out.println(datasetInfo);
    }

    @Test
    public void testRead() throws Exception {
        String        fileName    = "src/test/data/sample.arff";
        DatasetReader reader      = new ArffReader(fileName);
        DatasetInfo   datasetInfo = reader.analyze();
        datasetInfo.setClassIndex(2);
        IndexCondition            cond = new IndexConditionBetween(4, 7);
        DatasetIterator           it   = reader.onePass(cond);
        Pair<Instance, Attribute> entry;
        entry = it.next();
        Assert.assertEquals((Double) entry.getFirst().getAttribute(0).getValue(), 4., 1e-6);
        Assert.assertEquals((Double) entry.getFirst().getAttribute(1).getValue(), -4., 1e-6);
        Assert.assertEquals(entry.getSecond().getValue(), "c0");
        entry = it.next();
        Assert.assertEquals((Double) entry.getFirst().getAttribute(0).getValue(), 5., 1e-6);
        Assert.assertEquals((Double) entry.getFirst().getAttribute(1).getValue(), -5., 1e-6);
        Assert.assertEquals(entry.getSecond().getValue(), "c1");
        entry = it.next();
        Assert.assertEquals((Double) entry.getFirst().getAttribute(0).getValue(), 6., 1e-6);
        Assert.assertEquals((Double) entry.getFirst().getAttribute(1).getValue(), -6., 1e-6);
        Assert.assertEquals(entry.getSecond().getValue(), "c0");
        it.close();
    }
}
