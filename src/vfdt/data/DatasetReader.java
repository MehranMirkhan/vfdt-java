package vfdt.data;

import vfdt.util.Pair;

import java.io.IOException;
import java.util.Iterator;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public interface DatasetReader {
    DatasetInfo analyze() throws IOException;

    Iterator<Pair<Instance, Attribute>> onePass() throws IOException;
    Iterator<Pair<Instance, Attribute>> onePass(IndexCondition indexCondition) throws IOException;
    Iterator<Pair<Instance, Attribute>> epochs(int numEpochs) throws IOException;
    Iterator<Pair<Instance, Attribute>> epochs(int numEpochs, IndexCondition indexCondition) throws IOException;
}
