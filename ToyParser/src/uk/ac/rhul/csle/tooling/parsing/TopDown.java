package uk.ac.rhul.csle.tooling.parsing;

public class TopDown {

  /*
   * Grammar: S ::= A S d | B S | #
   * 
   * A ::= a | c
   * 
   * B ::= a | b
   */

  private final char[] input;

  private final int inputLength;

  private int currentIndex;

  public TopDown(String input) {
    String terminatedInput = input + '\0';
    this.input = terminatedInput.toCharArray();
    inputLength = input.length();
    currentIndex = 0;
  }

  public boolean parse() throws InvalidParseException {
    if (currentIndex == inputLength || "abcd".indexOf(input[currentIndex]) >= 0) {
      parseS();
    } else {
      throw new InvalidParseException();
    }
    if (currentIndex == inputLength) {
      System.out.println("Successful parse");
      return true;
    } else {
      throw new InvalidParseException();
    }
  }

  private void parseS() throws InvalidParseException {
    if ("ac".indexOf(input[currentIndex]) >= 0) {
      parseA();
      parseS();
      if ("d".indexOf(input[currentIndex]) >= 0) {
        currentIndex++;
      } else {
        throw new InvalidParseException();
      }
    } else {
      if ("ab".indexOf(input[currentIndex]) >= 0) {
        parseB();
        parseS();
      }
    }
  }

  private void parseB() throws InvalidParseException {
    if ("a".indexOf(input[currentIndex]) >= 0) {
      currentIndex++;
    } else if ("b".indexOf(input[currentIndex]) >= 0) {
      currentIndex++;
    } else {
      throw new InvalidParseException();
    }
  }

  private void parseA() throws InvalidParseException {
    if ("a".indexOf(input[currentIndex]) >= 0) {
      currentIndex++;
    } else if ("c".indexOf(input[currentIndex]) >= 0) {
      currentIndex++;
    } else {
      throw new InvalidParseException();
    }
  }
}
