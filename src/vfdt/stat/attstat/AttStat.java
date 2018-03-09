package vfdt.stat.attstat;

import vfdt.data.Attribute;
import vfdt.measure.gain.Gain;
import vfdt.stat.splitter.Splitter;
import vfdt.tree.Decision;

/**
 * Stores the information about an attribute from the arrived data for each class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public interface AttStat {
    void update(Attribute att, Attribute label);
}
