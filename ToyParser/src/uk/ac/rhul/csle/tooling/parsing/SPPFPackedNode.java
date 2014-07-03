package uk.ac.rhul.csle.tooling.parsing;

/**
 * Object to denote an SPPF packed node
 * 
 * @author robert
 * 
 */
public class SPPFPackedNode {

  private final String label;

  private final int pivot;

  private SPPFNode leftChild;

  public String getLabel() {
    return label;
  }

  public int getPivot() {
    return pivot;
  }

  public void setLeftChild(SPPFNode leftChild) {
    this.leftChild = leftChild;
  }

  public void setRightChild(SPPFNode rightChild) {
    this.rightChild = rightChild;
  }

  private SPPFNode rightChild;

  public SPPFPackedNode(String label, int pivot) {
    this.label = label;
    this.pivot = pivot;
  }

  public String toVCGString() {
    String str =
            "node:{title:\"" + label + "," + pivot + "\" horizontal_order:"
                    + pivot + " }\n";
    if (leftChild != null) {
      str += leftChild.toVCGString();
      str +=
              "edge:{sourcename:\"" + label + "," + pivot + "\" targetname:\""
                      + leftChild.getLabel() + "," + leftChild.getLeftExtent()
                      + "," + leftChild.getRightExtent() + "\"}\n";
    }
    if (rightChild != null) {
      str += rightChild.toVCGString();
      str +=
              "edge:{sourcename:\"" + label + "," + pivot + "\" targetname:\""
                      + rightChild.getLabel() + ","
                      + rightChild.getLeftExtent() + ","
                      + rightChild.getRightExtent() + "\"}\n";
    }

    return str;
  }
}
