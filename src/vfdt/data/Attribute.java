package vfdt.data;

/**
 * An attribute.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 23
 */
public class Attribute<T> {
    private final AttributeInfo attributeInfo;
    private       T             value;

    public Attribute(AttributeInfo attributeInfo, T value) {
        this.attributeInfo = attributeInfo;
        this.value = value;
    }

    public AttributeInfo getAttributeInfo() {
        return attributeInfo;
    }

    public String getName() {
        return attributeInfo.getName();
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Integer getValueIndex() {
        return getAttributeInfo().findValue((String) value);
    }

    public Attribute clone() {
        return new Attribute<T>(attributeInfo, value);
    }

    @Override
    public String toString() {
        return "" + attributeInfo.getName() + ": " + value;
    }
}
