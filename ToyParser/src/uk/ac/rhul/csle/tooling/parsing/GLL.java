package uk.ac.rhul.csle.tooling.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.ac.rhul.csle.tooling.io.IOReadWrite;

/**
 * A GLL Parser for the grammar:
 * 
 * S ::= A S d | B S | #
 * 
 * A ::= a | c
 * 
 * B ::= a | b
 * 
 * (# is epsilon)
 * 
 * @author robert
 * 
 */
public class GLL {

  private class GLLRunnable implements Runnable {

    private GSSNode currentGSSNode;

    private SPPFNode currentSPPFNode;

    private int currentCharacterIndex;

    private final Labels callFunction;

    public GLLRunnable(Labels callFunction, GSSNode currentGSSNode,
            int currentCharacterIndex, SPPFNode currentSPPFNode) {
      this.currentGSSNode = currentGSSNode;
      this.currentSPPFNode = currentSPPFNode;
      this.currentCharacterIndex = currentCharacterIndex;
      this.callFunction = callFunction;
    }

    @Override
    public void run() {
      switch (callFunction) {
        case LA:
          parseLA();
          break;
        case LA1:
          parseLA1();
          break;
        case LA2:
          parseLA2();
          break;
        case LB:
          parseLB();
          break;
        case LB1:
          parseLB1();
          break;
        case LB2:
          parseLB2();
          break;
        case LS:
          parseLS();
          break;
        case LS1:
          parseLS1();
          break;
        case LS2:
          parseLS2();
          break;
        case LS3:
          parseLS3();
          break;
        case RA1:
          parseRA1();
          break;
        case RB1:
          parseRB1();
          break;
        case RS1:
          parseRS1();
          break;
        case RS2:
          parseRS2();
          break;
        default:
          break;
      }
    }

    // A ::= c .
    private void parseLA2() {
      if (input[currentCharacterIndex] == 'c') {
        mostRecentTerminalNode =
                getNodeL("c", currentCharacterIndex, ++currentCharacterIndex);
        currentSPPFNode =
                getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
        pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // A ::= a .
    private void parseLA1() {
      if (input[currentCharacterIndex] == 'a') {
        mostRecentTerminalNode =
                getNodeL("a", currentCharacterIndex, ++currentCharacterIndex);
        currentSPPFNode =
                getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
        pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // A ::= .a | .c
    private void parseLA() {
      if (testSelect(input[currentCharacterIndex], "A", "a")) {
        add(Labels.LA1, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
      if (testSelect(input[currentCharacterIndex], "A", "c")) {
        add(Labels.LA2, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
    }

    // B ::= b .
    private void parseLB2() {
      if (input[currentCharacterIndex] == 'b') {
        mostRecentTerminalNode =
                getNodeL("b", currentCharacterIndex, ++currentCharacterIndex);
        currentSPPFNode =
                getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
        pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // B ::= a .
    private void parseLB1() {
      if (input[currentCharacterIndex] == 'a') {
        mostRecentTerminalNode =
                getNodeL("a", currentCharacterIndex, ++currentCharacterIndex);
        currentSPPFNode =
                getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
        pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // B ::= .a | .b
    private void parseLB() {
      if (testSelect(input[currentCharacterIndex], "B", "a")) {
        add(Labels.LB1, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
      if (testSelect(input[currentCharacterIndex], "B", "b")) {
        add(Labels.LB2, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
    }

    // S ::= .
    private void parseLS3() {
      mostRecentTerminalNode =
              getNodeL("#", currentCharacterIndex, currentCharacterIndex);
      currentSPPFNode =
              getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
      pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
    }

    // S ::= B S .
    private void parseRS2() {
      pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
    }

    // S ::= B S .
    private void parseRB1() {
      if (testSelect(input[currentCharacterIndex], "S", "S")) {
        currentGSSNode =
                create(Labels.RS2, currentGSSNode, currentCharacterIndex,
                        currentSPPFNode);
        add(Labels.LS, currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // S ::= B . S
    private void parseLS2() {
      currentGSSNode =
              create(Labels.RB1, currentGSSNode, currentCharacterIndex,
                      currentSPPFNode);
      add(Labels.LB, currentGSSNode, currentCharacterIndex, currentSPPFNode);
    }

    // S ::= A S d .
    private void parseRS1() {
      if (input[currentCharacterIndex] == 'd') {
        mostRecentTerminalNode =
                getNodeL("d", currentCharacterIndex, ++currentCharacterIndex);
        currentSPPFNode =
                getNode(callFunction, currentSPPFNode, mostRecentTerminalNode);
        pop(currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // S ::= A S . d
    private void parseRA1() {
      if (testSelect(input[currentCharacterIndex], "S", "Sd")) {
        currentGSSNode =
                create(Labels.RS1, currentGSSNode, currentCharacterIndex,
                        currentSPPFNode);
        add(Labels.LS, currentGSSNode, currentCharacterIndex, currentSPPFNode);
      }
    }

    // S ::= A . S d
    private void parseLS1() {
      currentGSSNode =
              create(Labels.RA1, currentGSSNode, currentCharacterIndex,
                      currentSPPFNode);
      add(Labels.LA, currentGSSNode, currentCharacterIndex, currentSPPFNode);

    }

    // S ::= .A S d | .B S | .
    private void parseLS() {
      if (testSelect(input[currentCharacterIndex], "S", "ASd")) {
        add(Labels.LS1, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
      if (testSelect(input[currentCharacterIndex], "S", "BS")) {
        add(Labels.LS2, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
      if (testSelect(input[currentCharacterIndex], "S", "#")) {
        add(Labels.LS3, currentGSSNode, currentCharacterIndex, new SPPFNode(
                "$", -1, -1));
      }
    }
  }

  private final char[] input;

  private final int inputLength;

  private SPPFNode mostRecentTerminalNode;

  private final List<GSSNode> GSS;

  private final List<SPPFNode> SPPF;

  private final Set<Descriptor> descriptorMemoizationState;

  private final Stack<Descriptor> currentDescriptorSet;

  private final Set<PPair> remainingPopSet;

  private final GSSNode initialNode;

  /**
   * 
   * Adds descriptors to the current descriptor set
   * 
   * @param label
   *          Label to store in the descriptor
   * @param gssNode
   *          GSS node to store in the descriptor
   * @param characterPosition
   *          index to store in the descriptor
   * @param node
   *          SPPF node to store in the descriptor
   */
  public void add(Labels label, GSSNode gssNode, int characterPosition,
          SPPFNode node) {
    Descriptor candidate =
            new Descriptor(label, gssNode, characterPosition, node);
    if (!descriptorMemoizationState.contains(candidate)) {
      descriptorMemoizationState.add(candidate);
      currentDescriptorSet.add(candidate);
    }
  }

  /**
   * Performs a pop on the given GSS node
   * 
   * @param gssNode
   *          GSS node to pop
   * @param characterPosition
   *          next character position
   * @param node
   *          SPPF node created.
   */
  public void pop(GSSNode gssNode, int characterPosition, SPPFNode node) {
    if (!gssNode.getAdjacencies().isEmpty()) {
      remainingPopSet.add(new PPair(gssNode, node));
      synchronized (gssNode.getAdjacencies()) {
        for (GSSEdge edge : gssNode.getAdjacencies()) {
          SPPFNode y = getNode(gssNode.getLabel(), edge.getLabel(), node);
          add(gssNode.getLabel(), edge.getTo(), characterPosition, y);
        }
      }
    }
  }

  /**
   * Translates enumerated label into a string label
   * 
   * @param label
   *          Enumerate label
   * @return String label
   */
  private String getStringLabel(Labels label) {
    switch (label) {
      case DOLLAR:
        return "$";
      case L0:
        return "S'";
      case LA:
        return "A ::= a | c";
      case LA1:
        return "A ::= a.";
      case LA2:
        return "A ::= c.";
      case LB:
        return "B ::= a | b";
      case LB1:
        return "B ::= a.";
      case LB2:
        return "B ::= b.";
      case LS:
        return "S ::= A S d | B S | #";
      case LS1:
        return "S ::= A .S d";
      case LS2:
        return "S ::= B .S";
      case LS3:
        return "S ::= .";
      case RA1:
        return "S ::= A S .d";
      case RB1:
        return "S ::= B .S";
      case RS1:
        return "S ::= A S d.";
      case RS2:
        return "S ::= B S .";
      default:
        return "ERROR";

    }
  }

  /**
   * Creates interior and packed nodes
   * 
   * @param label
   *          The label of the new node
   * @param node1
   *          The left child
   * @param node2
   *          The right child
   * @return The node created
   */
  private SPPFNode getNode(Labels label, SPPFNode node1, SPPFNode node2) {
    String sppfLabel = "";
    switch (label) {
      case DOLLAR:
        sppfLabel = "$";
        break;
      case L0:
        sppfLabel = "S'";
        break;
      case LA:
        sppfLabel = "A";
        break;
      case LA1:
        sppfLabel = "A";
        break;
      case LA2:
        sppfLabel = "A";
        break;
      case LB:
        sppfLabel = "B";
        break;
      case LB1:
        sppfLabel = "B";
        break;
      case LB2:
        sppfLabel = "B";
        break;
      case LS:
        sppfLabel = "S";
        break;
      case LS1:
        sppfLabel = "S ::= A .S d";
        break;
      case LS2:
        sppfLabel = "S ::= B .S";
        break;
      case LS3:
        sppfLabel = "S";
        break;
      case RA1:
        sppfLabel = "S ::= A S .d";
        break;
      case RB1:
        sppfLabel = "S ::= B .S";
        break;
      case RS1:
        sppfLabel = "S";
        break;
      case RS2:
        sppfLabel = "S";
        break;
      default:
        break;

    }
    int j;
    if (node1.getLabel().equals("$")) {
      j = node2.getLeftExtent();
    } else {
      j = node1.getLeftExtent();
    }

    SPPFNode candidate = new SPPFNode(sppfLabel, j, node2.getRightExtent());
    if (!SPPF.contains(candidate)) {
      SPPF.add(candidate);
    }

    SPPFPackedNode candidatePackedNode =
            new SPPFPackedNode(getStringLabel(label), node2.getLeftExtent());
    if (!candidate.getChildren().contains(candidatePackedNode)) {
      candidate.addChild(candidatePackedNode);
      candidatePackedNode.setRightChild(node2);
      if (!node1.getLabel().equals("$")) {
        candidatePackedNode.setLeftChild(node1);
      }
    }
    return candidate;
  }

  /**
   * Creates leaf nodes
   * 
   * @param label
   *          The label of the node
   * @param leftExtent
   *          The left extent
   * @param rightExtent
   *          The right extent
   * @return The created node
   */
  private SPPFNode getNodeL(String label, int leftExtent, int rightExtent) {
    SPPFNode candidate = new SPPFNode(label, leftExtent, rightExtent);
    if (!SPPF.contains(candidate)) {
      SPPF.add(candidate);
    }
    return candidate;
  }

  /**
   * Generates a new GSS node
   * 
   * @param label
   *          The label of the new node
   * @param callingNode
   *          The calling node
   * @param characterPosition
   *          The input character position
   * @param node
   *          The SPPF node for the edge labels
   * @return The generated node
   */
  public GSSNode create(Labels label, GSSNode callingNode,
          int characterPosition, SPPFNode node) {
    GSSNode candidate = new GSSNode(label, characterPosition);
    boolean exists = false;

    synchronized (GSS) {
      for (GSSNode g : GSS) {
        if (g.equals(candidate)) {
          candidate = g;
          exists = true;
          break;
        }
      }
    }
    if (!exists) {
      GSS.add(candidate);
    }
    if (!candidate.edgeExists(callingNode)) {
      candidate.addEdge(callingNode, node);
      synchronized (remainingPopSet) {
        for (PPair p : remainingPopSet) {
          if (p.getNode().equals(candidate)) {
            SPPFNode y = getNode(candidate.getLabel(), node, p.getSPPFNode());
            add(label, callingNode, p.getSPPFNode().getRightExtent(), y);
          }
        }
      }
    }
    return candidate;
  }

  private final String originalString;

  /**
   * Performs initial setup
   * 
   * @param input
   *          The string to parse
   */
  public GLL(String input) {
    originalString = input;
    String terminatedInput = input + '\0';
    this.input = terminatedInput.toCharArray();
    inputLength = input.length();
    initialNode = new GSSNode(Labels.L0, 0);
    remainingPopSet = Collections.synchronizedSet(new HashSet<PPair>());
    currentDescriptorSet = new Stack<Descriptor>();
    GSS = Collections.synchronizedList(new ArrayList<GSSNode>());
    GSS.add(initialNode);
    SPPF = Collections.synchronizedList(new ArrayList<SPPFNode>());
    descriptorMemoizationState =
            Collections.synchronizedSet(new HashSet<Descriptor>());
    add(Labels.LS, initialNode, 0, new SPPFNode("$", -1, -1));
  }

  /**
   * Parses the string. Main program loop
   * 
   * @return true if the string is in the language
   * @throws InvalidParseException
   *           If the string is not in the language
   */
  public boolean parse() throws InvalidParseException {
    while (!currentDescriptorSet.isEmpty()) {
      ExecutorService es =
              Executors.newFixedThreadPool(currentDescriptorSet.size());

      while (!currentDescriptorSet.isEmpty()) {
        Descriptor next = currentDescriptorSet.pop();
        es.execute(new GLLRunnable(next.getLabel(), next.getGssNode(), next
                .getInputPosition(), next.getNode()));
      }

      es.shutdown();
      while (!es.isTerminated()) {

      }
    }
    if (SPPF.contains(new SPPFNode("S", 0, inputLength))) {
      sppfToVCG();
      gssToVCG();
      return true;
    } else {
      for (SPPFNode node : SPPF) {
        System.out.println(node);
      }
      throw new InvalidParseException();
    }
  }

  /**
   * Converts the GSS into a VCG (aiSee3) file
   */
  public void gssToVCG() {
    String str =
            "graph:{\n" + "layoutalgorithm:tree\n" + "splines:yes\n"
                    + "orientation:left_to_right\n" + "edge.arrowsize:7\n"
                    + "edge.thickness:1\n" + "display_edge_labels:yes\n"
                    + "arrowmode:free\n" + "node.borderwidth:1\n";
    for (GSSNode node : GSS) {
      str += node.toVCGString();
    }
    IOReadWrite.writeFile(originalString + "gss.vcg", str + "}");
  }

  /**
   * Converts the SPPF into a VCG (aiSee3) file
   */
  public void sppfToVCG() {
    String str =
            "graph:{\n" + "layoutalgorithm:tree\n" + "splines:yes\n"
                    + "orientation:top_to_bottom\n" + "edge.arrowsize:7\n"
                    + "edge.thickness:1\n" + "display_edge_labels:yes\n"
                    + "arrowmode:free\n" + "node.borderwidth:1\n";
    SPPFNode root = SPPF.get(SPPF.indexOf(new SPPFNode("S", 0, inputLength)));
    IOReadWrite.writeFile(originalString + "output.vcg",
            str + root.toVCGString() + "}");
  }

  /**
   * Guard function to test whether a character is in the first set of the next
   * symbol (or in the follow set if nullable)
   * 
   * @param character
   *          The current character
   * @param nonterminal
   *          The current nonterminal
   * @param alternate
   *          The current rule
   * @return true if it is, false otherwise
   */
  private boolean testSelect(char character, String nonterminal,
          String alternate) {
    if (inFirst(character, alternate)
            || (inFirst(alternate) && inFollow(character, nonterminal))) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Determines whether a character is in the FIRST set of the alternate
   * 
   * @param character
   *          The current character
   * @param alternate
   *          The current rule
   * @return true if it is, false otherwise
   */
  private boolean inFirst(char character, String alternate) {
    switch (alternate) {
      case "ASd":
        return "ac".indexOf(character) >= 0;
      case "Sd":
        return "abcd".indexOf(character) >= 0;
      case "d":
        return 'd' == character;
      case "BS":
        return "ab".indexOf(character) >= 0;
      case "S":
        return "abcd\0".indexOf(character) >= 0;
      case "a":
        return 'a' == character;
      case "c":
        return 'c' == character;
      case "b":
        return 'b' == character;
      default:
        return false;
    }
  }

  /**
   * Tests whether epsilon is in the FIRST set of the alternate
   * 
   * @param alternate
   *          The current alternate
   * @return true if so, false otherwise
   */
  private boolean inFirst(String alternate) {
    return alternate.equals("#") || alternate.equals("S");
  }

  /**
   * Tests whether the current character is in the FOLLOW set of the nonterminal
   * 
   * @param character
   *          The current character
   * @param nonterminal
   *          The current nonterminal
   * @return true if so, false otherwise.
   */
  private boolean inFollow(char character, String nonterminal) {
    switch (nonterminal) {
      case "S":
        return "d\0".indexOf(character) >= 0;
      case "A":
        return "abcd".indexOf(character) >= 0;
      case "B":
        return "abcd\0".indexOf(character) >= 0;
      default:
        return false;
    }
  }
}
