package uk.ac.rhul.csle.tooling.parsing;

/**
 * Object to denote an edge in a GSS
 * 
 * @author robert
 * 
 */
public class GSSEdge {

  private final GSSNode to;

  private final SPPFNode label;

  public SPPFNode getLabel() {
    return label;
  }

  public GSSNode getTo() {
    return to;
  }

  public GSSEdge(GSSNode to, SPPFNode label) {
    this.to = to;
    this.label = label;
  }

  public String toVCGString(GSSNode from) {
    return "edge:{sourcename:\"" + from.getLabel() + "," + from.getIndex()
            + "\" targetname:\"" + to.getLabel() + "," + to.getIndex()
            + "\" label:\"" + label.getLabel() + "," + label.getLeftExtent()
            + "," + label.getRightExtent() + "\"}\n";
  }
}
