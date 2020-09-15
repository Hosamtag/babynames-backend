package names;

/**
 * This exception will be thrown whenever any of the parameters are invalid. This includes invalid
 * year ranges, invalid gender inputs, or invalid data sources/files.
 *
 * @author Hosam Tageldin
 */
public class InvalidParameterException extends Exception {

  public InvalidParameterException(String message) {
    super(message);
  }
}
