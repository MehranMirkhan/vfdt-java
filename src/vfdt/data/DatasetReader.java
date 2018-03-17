package vfdt.data;

import java.io.IOException;

/**
 * Reads a .arff file.
 * instances are indexed from 0.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public interface DatasetReader {
    DatasetInfo getDatasetInfo() throws IOException;
    void setDatasetInfo(DatasetInfo datasetInfo);

    DatasetInfo analyze() throws IOException;

    DatasetIterator onePass() throws IOException;

    DatasetIterator onePass(IndexCondition indexCondition) throws IOException;

    DatasetIterator epochs(int numEpochs) throws IOException;

    DatasetIterator epochs(int numEpochs, IndexCondition indexCondition) throws IOException;
}
