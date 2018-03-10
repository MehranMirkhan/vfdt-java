package vfdt.data;

import vfdt.util.Pair;

import java.io.IOException;
import java.util.Iterator;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 10
 */
public interface DatasetIterator extends Iterator<Pair<Instance, Attribute>> {
    void close() throws IOException;
}
