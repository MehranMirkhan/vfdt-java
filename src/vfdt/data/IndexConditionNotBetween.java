package vfdt.data;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 16
 */
public class IndexConditionNotBetween implements IndexCondition {

    private final int start, end;

    public IndexConditionNotBetween(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean isValid(Integer index) {
        return start < index || index >= end;
    }
}
