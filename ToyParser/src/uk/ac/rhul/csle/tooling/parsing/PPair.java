package uk.ac.rhul.csle.tooling.parsing;

/**
 * A 2-tuple (x,y) where x is a gss node and y is an SPPF node
 * 
 * @author robert
 * 
 */
public class PPair {

  private final GSSNode node;

  public GSSNode getNode() {
    return node;
  }

  public SPPFNode getSPPFNode() {
    return sppfNode;
  }

  private final SPPFNode sppfNode;

  public PPair(GSSNode node, SPPFNode sppfNode) {
    this.node = node;
    this.sppfNode = sppfNode;
  }
}
