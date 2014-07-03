package uk.ac.rhul.csle.testing;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.rhul.csle.tooling.parsing.GLL;
import uk.ac.rhul.csle.tooling.parsing.InvalidParseException;

/**
 * Tests for a GLL Parser
 * 
 * @author robert
 * 
 */
public class TestGLLParser {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void testcbd() throws InvalidParseException {
    assertEquals(true, new GLL("cbd").parse());
  }

  @Test
  public void testempty() throws InvalidParseException {
    assertEquals(true, new GLL("").parse());
  }

  @Test
  public void testdbc() throws InvalidParseException {
    GLL parser = new GLL("dbc");

    exception.expect(InvalidParseException.class);
    parser.parse();
  }

  @Test
  public void testaabcabd() throws InvalidParseException {
    assertEquals(true, new GLL("aabcabd").parse());
  }

  @Test
  public void testd() throws InvalidParseException {
    GLL parser = new GLL("d");

    exception.expect(InvalidParseException.class);
    parser.parse();
  }
}
