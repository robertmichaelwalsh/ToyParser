package uk.ac.rhul.csle.tooling.parsing;

import java.util.ArrayList;

/**
 * Object to denote a GSS node
 * 
 * @author robert
 * 
 */
public class GSSNode {

  private final Labels label;

  public Labels getLabel() {
    return label;
  }

  private final int index;

  public int getIndex() {
    return index;
  }

  private final ArrayList<GSSEdge> adjacencies;

  public ArrayList<GSSEdge> getAdjacencies() {
    return adjacencies;
  }

  public GSSNode(Labels label, int index) {
    this.label = label;
    this.index = index;
    adjacencies = new ArrayList<GSSEdge>();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + index;
    result = prime * result + label.ordinal();
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
    GSSNode other = (GSSNode) obj;
    if (index != other.index)
      return false;
    if (label != other.label)
      return false;
    return true;
  }

  /**
   * Determines whether an edge from this node to another node exists
   * 
   * @param other
   *          The possible to node
   * @return true if there is an edge, false otherwise
   */
  public boolean edgeExists(GSSNode other) {
    for (GSSEdge edge : adjacencies) {
      if (edge.getTo() == other) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds an edge labelled with an SPPF node to a GSS node
   * 
   * @param other
   *          The GSS node to add as an adjacencies
   * @param node
   *          The label of the edge
   */
  public void addEdge(GSSNode other, SPPFNode node) {
    adjacencies.add(new GSSEdge(other, node));
  }

  public String toVCGString() {
    String str =
            "node:{title:\"" + label + "," + index + "\" horizontal_order:"
                    + index + "}\n";
    for (GSSEdge edge : adjacencies) {
      str += edge.toVCGString(this);
    }
    return str;
  }
}