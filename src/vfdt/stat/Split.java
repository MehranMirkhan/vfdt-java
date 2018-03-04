package vfdt.stat;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class Split {
    protected Counts original;
    protected Counts[] splits;

    public Split(Counts original, Counts[] splits) {
        this.original = original;
        this.splits = splits;
    }
}
