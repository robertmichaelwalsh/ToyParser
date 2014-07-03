package uk.ac.rhul.csle.tooling.parsing;

/**
 * Object describing parser descriptors
 * 
 * @author robert
 * 
 */
public class Descriptor {

  private final Labels label;

  public Labels getLabel() {
    return label;
  }

  public GSSNode getGssNode() {
    return gssNode;
  }

  public int getInputPosition() {
    return inputPosition;
  }

  GSSNode gssNode;

  private final int inputPosition;

  private final SPPFNode node;

  public SPPFNode getNode() {
    return node;
  }

  public Descriptor(Labels label, GSSNode gssNode, int inputPosition,
          SPPFNode node) {
    this.label = label;
    this.gssNode = gssNode;
    this.inputPosition = inputPosition;
    this.node = node;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gssNode == null) ? 0 : gssNode.hashCode());
    result = prime * result + inputPosition;
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((node == null) ? 0 : node.hashCode());
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
    Descriptor other = (Descriptor) obj;
    if (gssNode == null) {
      if (other.gssNode != null)
        return false;
    } else if (!gssNode.equals(other.gssNode))
      return false;
    if (inputPosition != other.inputPosition)
      return false;
    if (label != other.label)
      return false;
    if (node == null) {
      if (other.node != null)
        return false;
    } else if (!node.equals(other.node))
      return false;
    return true;
  }
}
