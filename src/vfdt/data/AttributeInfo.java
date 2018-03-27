package vfdt.data;

import java.util.Arrays;

/**
 * Information about an attribute.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 23
 */
public class AttributeInfo {
    public enum AttributeType {
        NOMINAL, NUMERICAL
    }

    private String   name   = "Unknown";
    private String[] values = null;
    private AttributeType type;

    public AttributeInfo(AttributeType type) {
        this.type = type;
    }

    public AttributeInfo name(String name) {
        this.name = name;
        return this;
    }

    public AttributeInfo values(String... values) {
        this.values = values;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttributeInfo that = (AttributeInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(values, that.values)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (values != null ? Arrays.hashCode(values) : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return this.values;
    }

    public void setValues(String... values) {
        this.values = values;
    }

    public AttributeType getType() {
        return this.type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Integer findValue(String value) {
        if (values != null)
            for (int i = 0; i < values.length; i++)
                if (values[i].equals(value))
                    return i;
        return null;
    }

    public boolean isNominal() {
        return getType() == AttributeType.NOMINAL;
    }

    public boolean isNumerical() {
        return getType() == AttributeType.NUMERICAL;
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
