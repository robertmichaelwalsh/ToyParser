package uk.ac.rhul.csle.testing;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.rhul.csle.tooling.parsing.InvalidParseException;
import uk.ac.rhul.csle.tooling.parsing.TopDown;

/**
 * Tests for a Top Down Parser
 * 
 * @author robert
 * 
 */
public class TestTopDownParser {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void testcbd() throws InvalidParseException {
    assertEquals(true, new TopDown("cbd").parse());
  }

  @Test
  public void testempty() throws InvalidParseException {
    assertEquals(true, new TopDown("").parse());
  }

  @Test
  public void testdbc() throws InvalidParseException {
    TopDown parser = new TopDown("dbc");

    exception.expect(InvalidParseException.class);
    parser.parse();
  }
}
