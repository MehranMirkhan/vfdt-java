package vfdt.data;

/**
 * Instance is a vector of attributes (features).
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public class Instance {
    private Attribute[] atts;

    public Instance(Attribute... atts) {
        this.atts = atts;
    }

    public Attribute getAttribute(int index) {
        return this.atts[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Attribute att : this.atts) {
            sb.append(att.toString()).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
