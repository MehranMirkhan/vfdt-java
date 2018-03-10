package test.stat;

import org.junit.Assert;
import org.junit.Test;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.measure.bound.Bound;
import vfdt.measure.bound.BoundHoeffding;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.GainBase;
import vfdt.measure.impurity.GiniIndex;
import vfdt.measure.impurity.Impurity;
import vfdt.stat.SuffStat;
import vfdt.stat.SuffStatBase;
import vfdt.stat.attstat.AttStatFactory;
import vfdt.stat.attstat.AttStatFactoryBase;
import vfdt.stat.splitter.SplitterFactory;
import vfdt.stat.splitter.SplitterFactoryBase;
import vfdt.tree.Decision;

import java.util.List;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class TestSuffStat {

    @Test
    public void testCorrectness() throws Exception {
        Integer  classIndex   = 2;
        Impurity impurity     = new GiniIndex();
        Gain     gain         = new GainBase(impurity);
        Double   delta        = 0.5;
        Double   R            = 2.;
        Bound    bound        = new BoundHoeffding(delta, R);
        int      numCadidates = 10;

        AttributeInfo[] attsInfo = new AttributeInfo[]{
                new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL).name("a0"),
                new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL).name("a1"),
                new AttributeInfo(AttributeInfo.AttributeType.NOMINAL).name("class")
                        .values("c0", "c1")
        };

        DatasetInfo datasetInfo = new DatasetInfo()
                .datasetName("test").attributeInfo(attsInfo).classIndex(classIndex);

        List<AttributeInfo> availableAtts = datasetInfo.getAttributeInfoAsList();
        availableAtts.remove(datasetInfo.getClassAttribute());

        AttStatFactory  attStatFactory  = new AttStatFactoryBase(datasetInfo);
        SplitterFactory splitterFactory = new SplitterFactoryBase(gain, numCadidates);

        SuffStat suffStat = new SuffStatBase(availableAtts, attStatFactory, splitterFactory);

        // Fake dataset
        Instance          ins1 = new Instance(new Attribute<>(attsInfo[0], -3.), new Attribute<>(attsInfo[1], -1.));
        Attribute<String> lbl1 = new Attribute<>(attsInfo[classIndex], "c0");
        Instance          ins2 = new Instance(new Attribute<>(attsInfo[0], -1.), new Attribute<>(attsInfo[1], 1.));
        Attribute<String> lbl2 = new Attribute<>(attsInfo[classIndex], "c0");
        Instance          ins3 = new Instance(new Attribute<>(attsInfo[0], 1.), new Attribute<>(attsInfo[1], -1.));
        Attribute<String> lbl3 = new Attribute<>(attsInfo[classIndex], "c1");
        Instance          ins4 = new Instance(new Attribute<>(attsInfo[0], 3.), new Attribute<>(attsInfo[1], 1.));
        Attribute<String> lbl4 = new Attribute<>(attsInfo[classIndex], "c1");

        // Feed dataset
        for (int i = 0; i < 100; i++) {
            suffStat.update(ins1, lbl1);
            suffStat.update(ins2, lbl2);
            suffStat.update(ins3, lbl3);
            suffStat.update(ins4, lbl4);
        }

        AttributeInfo attToSplit = suffStat.checkSplit(bound);
        Assert.assertEquals(attToSplit.getName(), "a0");
        Decision decision = suffStat.getDecision();
        Assert.assertEquals(decision.getNumBranches(), 2);
        Assert.assertEquals(decision.decide(new Attribute<>(attsInfo[0], -1.)), 0);
        Assert.assertEquals(decision.decide(new Attribute<>(attsInfo[0], 1.)), 1);
    }

    @Test
    public void testCorrectnessOneAttribute() throws Exception {
        Integer  classIndex   = 1;
        Impurity impurity     = new GiniIndex();
        Gain     gain         = new GainBase(impurity);
        Double   delta        = 0.5;
        Double   R            = 2.;
        Bound    bound        = new BoundHoeffding(delta, R);
        int      numCadidates = 10;

        AttributeInfo[] attsInfo = new AttributeInfo[]{
                new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL).name("a0"),
                new AttributeInfo(AttributeInfo.AttributeType.NOMINAL).name("class")
                        .values("c0", "c1")
        };

        DatasetInfo datasetInfo = new DatasetInfo()
                .datasetName("test").attributeInfo(attsInfo).classIndex(classIndex);

        List<AttributeInfo> availableAtts = datasetInfo.getAttributeInfoAsList();
        availableAtts.remove(datasetInfo.getClassAttribute());

        AttStatFactory  attStatFactory  = new AttStatFactoryBase(datasetInfo);
        SplitterFactory splitterFactory = new SplitterFactoryBase(gain, numCadidates);

        SuffStat suffStat = new SuffStatBase(availableAtts, attStatFactory, splitterFactory);

        // Fake dataset
        Instance          ins1 = new Instance(new Attribute<>(attsInfo[0], -3.));
        Attribute<String> lbl1 = new Attribute<>(attsInfo[classIndex], "c0");
        Instance          ins2 = new Instance(new Attribute<>(attsInfo[0], -1.));
        Attribute<String> lbl2 = new Attribute<>(attsInfo[classIndex], "c0");
        Instance          ins3 = new Instance(new Attribute<>(attsInfo[0], 1.));
        Attribute<String> lbl3 = new Attribute<>(attsInfo[classIndex], "c1");
        Instance          ins4 = new Instance(new Attribute<>(attsInfo[0], 3.));
        Attribute<String> lbl4 = new Attribute<>(attsInfo[classIndex], "c1");

        // Feed dataset
        for (int i = 0; i < 100; i++) {
            suffStat.update(ins1, lbl1);
            suffStat.update(ins2, lbl2);
            suffStat.update(ins3, lbl3);
            suffStat.update(ins4, lbl4);
        }

        AttributeInfo attToSplit = suffStat.checkSplit(bound);
        Assert.assertEquals(attToSplit.getName(), "a0");
        Decision decision = suffStat.getDecision();
        Assert.assertEquals(decision.getNumBranches(), 2);
        Assert.assertEquals(decision.decide(new Attribute<>(attsInfo[0], -1.)), 0);
        Assert.assertEquals(decision.decide(new Attribute<>(attsInfo[0], 1.)), 1);
    }
}
