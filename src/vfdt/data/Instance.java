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

    public Attribute getAttribute(String name) {
        for (Attribute att : atts)
            if (att.getName().equals(name))
                return att;
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<atts.length; i++) {
            sb.append(atts[i].toString());
            if (i < atts.length-1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
