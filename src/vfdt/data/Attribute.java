package vfdt.data;

/**
 * An attribute.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 23
 */
public class Attribute<T> {
    private T value;

    public Attribute(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
