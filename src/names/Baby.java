package names;

import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;

/**
 * This class creates the Baby object which keeps track of the name of the baby and the number of
 * babies born with that name. It also keeps track of all other names in the program that have its
 * name as a prefix.
 * <p>
 * This class assumes the file format files the name,gender,count format in order for the baby
 * values to be populated correctly
 *
 * @author Hosam Tageldin
 */
public class Baby implements Comparable<Baby> {

  private final String babyName;
  private final int nameCount;
  private final Set<String> nameDerivatives;


  /**
   * Create a Baby with no count or rank used for comparison purposes
   *
   * @param name The name for the Baby object
   */
  public Baby(String name) {
    this(name, 0);
  }

  /**
   * Initializes the baby name, the count and the set that is used to keep track of name derivatives
   * that have this baby name as a prefix
   *
   * @param name  the name of the Baby
   * @param count the number of Babies born with that name
   */
  public Baby(String name, int count) {
    babyName = name;
    nameCount = count;
    nameDerivatives = new TreeSet<>();
  }

  /**
   * @return the name of the baby
   */
  public String getName() {
    return babyName;
  }

  /**
   * @return the number of babies born with that name in that year
   */
  public int getCount() {
    return nameCount;
  }

  /**
   * @return set of all derivatives of this Baby
   */
  public Set<String> getNameDerivatives() {
    return nameDerivatives;
  }

  /**
   * compares baby Name to the string ignoring case
   *
   * @param otherBabyName comparison string to see if Baby Name starts with
   * @return boolean regarding if babyName started with string
   */
  public boolean startsWith(String otherBabyName) {
    return babyName.toLowerCase().startsWith(otherBabyName.toLowerCase());
  }

  /**
   * Converts this Baby name to String
   */
  @Override
  public String toString() {
    return babyName;
  }

  /**
   * Check if this Baby's information is the same as the given Baby's. Note, currently only equality
   * is based only on their name, ignoring the case.
   *
   * @param other Baby to compare to this Baby
   * @return true iff the baby name values match ignoring case
   */
  @Override
  public boolean equals(Object other) {
    return other instanceof Baby
        && babyName.equalsIgnoreCase(((Baby) other).babyName);
  }

  /**
   * Hashes Baby for use in collections. Note, matches equals() so based only on name.
   *
   * @return hash value to represent Baby
   */
  @Override
  public int hashCode() {
    return Objects.hash(babyName);
  }

  /**
   * Compare this baby's name to the given baby's for ordering.
   *
   * @param other baby compare to this baby
   * @return positive if this baby name is alphabetically greater than the given baby, zero if they
   * are equal, and negative if this baby name is alphabetically less than the given baby
   */
  @Override
  public int compareTo(Baby other) {
    int nameComparison = babyName.compareToIgnoreCase(other.babyName);
    if (nameComparison == 0) {
      return nameComparison;
    }
    return nameComparison / Math.abs(nameComparison);
  }

}