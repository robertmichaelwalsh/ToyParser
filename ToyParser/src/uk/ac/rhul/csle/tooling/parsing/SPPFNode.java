package uk.ac.rhul.csle.tooling.parsing;

import java.util.ArrayList;

/**
 * Object to describe an SPPF node
 * 
 * @author robert
 * 
 */
public class SPPFNode {

  private final String label;

  public String getLabel() {
    return label;
  }

  private final int leftExtent;

  public int getLeftExtent() {
    return leftExtent;
  }

  private final int rightExtent;

  public int getRightExtent() {
    return rightExtent;
  }

  private final ArrayList<SPPFPackedNode> children;

  public ArrayList<SPPFPackedNode> getChildren() {
    return children;
  }

  public SPPFNode(String label, int leftExtent, int rightExtent) {
    this.label = label;
    this.leftExtent = leftExtent;
    this.rightExtent = rightExtent;
    children = new ArrayList<SPPFPackedNode>();
  }

  /**
   * Adds a packed node child to this node
   * 
   * @param child
   */
  public void addChild(SPPFPackedNode child) {
    children.add(child);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + leftExtent;
    result = prime * result + rightExtent;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SPPFNode other = (SPPFNode) obj;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (leftExtent != other.leftExtent)
      return false;
    if (rightExtent != other.rightExtent)
      return false;
    return true;
  }

  public String toVCGString() {
    String str =
            "node:{shape:ellipse title:\"" + label + "," + leftExtent + ","
                    + rightExtent + "\" horizontal_order:" + rightExtent
                    + "}\n";
    for (SPPFPackedNode child : children) {
      str += child.toVCGString();
      str +=
              "edge:{sourcename:\"" + label + "," + leftExtent + ","
                      + rightExtent + "\" targetname:\"" + child.getLabel()
                      + "," + child.getPivot() + "\"}\n";
    }

    return str;
  }

  @Override
  public String toString() {
    return label + "," + leftExtent + "," + rightExtent;
  }
}
