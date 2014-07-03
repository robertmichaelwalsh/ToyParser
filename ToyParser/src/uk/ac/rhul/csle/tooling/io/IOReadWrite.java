package uk.ac.rhul.csle.tooling.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Some macros for reading and writing files.
 * 
 * @author robert
 * 
 */
public abstract class IOReadWrite {

  // I don't want to construct instances
  private IOReadWrite() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Reads a file containing text
   * 
   * @param path
   *          The path of the file
   * @return The textual contents of the file
   * @throws IOException
   */
  public static String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
  }

  /**
   * Write the input to a file
   * 
   * @param path
   *          The path of the file to be created
   * @param input
   *          The contents of the file
   */
  public static void writeFile(String path, String input) {
    try {
      new PrintStream(path).printf("%s", input);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
