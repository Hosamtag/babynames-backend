package names;

import java.util.*;

/**
 * This class will do the heavy lifting calculations to help find the answers to the questions. This
 * class depends on YOBFileReader to correctly return a map that resembles the data provided in each
 * set for each gender. It is used by the Questions class to help with the calculations involved in
 * finding the answers to the questions.
 *
 * @author Hosam Tageldin
 */
public class DataSetInformation {

  private static final String MALE = "M";
  private static final String FEMALE = "F";
  private static final String NO_NAME_FOUND = "No Name Found";
  private static final String INVALID_YEAR_PARAMETER = "Invalid Year Range or Dataset Source!";
  private static final String INVALID_GENDER_INPUT = "Invalid Gender Input, M/m or F/f only";
  private static final String[] ALL_CAPITAL_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
  private Map<Integer, List<Baby>> allFemaleBabiesInDataSet;
  private Map<Integer, List<Baby>> allMaleBabiesInDataSet;

  /**
   * Stores all the baby information from a given dataset into a map corresponding to each baby
   * gender
   *
   * @param fileType the specified filetype, defined in calling the Questions class
   * @param dataSet  the specified dataSet, also defined when calling the Questions class
   */
  public DataSetInformation(String fileType, String dataSet) {
    YOBFileReader yobFileReader = new YOBFileReader(fileType, dataSet);
    this.allFemaleBabiesInDataSet = yobFileReader.allBabiesInDataSet(FEMALE);
    this.allMaleBabiesInDataSet = yobFileReader.allBabiesInDataSet(MALE);
  }

  /**
   * This method returns the Baby object with the highest number of name derivatives that has this
   * baby's name as a prefix.
   *
   * @param allNamesInRange a set containing all the unique names within a year range
   * @return the Baby name prefix with the largest number of name derivatives
   */
  public Baby findMaxPrefixInRange(Set<Baby> allNamesInRange) {
    int maxDerivativeCount = 0;
    Baby maxPrefix = new Baby(NO_NAME_FOUND);
    for (Baby baby : allNamesInRange) {
      addAllDerivatives(baby, allNamesInRange);
      int babyDerivativeCount = baby.getNameDerivatives().size();
      if (babyDerivativeCount > maxDerivativeCount) {
        maxDerivativeCount = babyDerivativeCount;
        maxPrefix = baby;
      }
    }
    return maxPrefix;
  }

  private void addAllDerivatives(Baby baby, Set<Baby> allNamesInRange) {
    for (Baby comparisonBaby : allNamesInRange) {
      if (comparisonBaby.startsWith(baby.getName())) {
        baby.getNameDerivatives().add(comparisonBaby.getName());
      }
    }
  }

  /**
   * This method is used to throw an InvalidParameterException if the year or gender inputs are
   * incorrect. Each method in Questions calls this method to ensure all the inputs are valid.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @throws InvalidParameterException the years or gender inputs are invalid
   */
  public void checkValidParameters(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    if (yearStart > yearEnd) {
      throw new InvalidParameterException(INVALID_YEAR_PARAMETER);
    }
    if (!allMaleBabiesInDataSet.containsKey(yearStart) || !allMaleBabiesInDataSet
        .containsKey(yearEnd)) {
      throw new InvalidParameterException(INVALID_YEAR_PARAMETER);
    }
    if (!gender.equalsIgnoreCase(MALE) && !gender.equalsIgnoreCase(FEMALE)) {
      throw new InvalidParameterException(INVALID_GENDER_INPUT);
    }
  }

  /**
   * This method is used to return all the unique baby objects that are a particular gender and are
   * within a particular year range.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a set containing all the unique baby gender-specific objects within a year range
   */
  public Set<Baby> allNamesInYearRange(String gender, int yearStart, int yearEnd) {
    Set<Baby> allNames = new TreeSet<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      allNames.addAll(desiredGenderMap(gender).get(year));
    }
    return allNames;
  }

  /**
   * This method keeps a count of how many names started with a particular letter. It utilizes a map
   * that maps each letter in the alphabet with the count of how many babies started with that
   * letter.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a map with the keys being the letters in the alphabet and the value being the number of
   * babies born with that first letter
   */
  public Map<String, Integer> countAllNamesFirstLetter(String gender, int yearStart, int yearEnd) {
    Map<String, Integer> letterCounter = new TreeMap<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      for (String letter : ALL_CAPITAL_LETTERS) {
        int count = letterCount(gender, letter, year)[1];
        letterCounter.put(letter, letterCounter.getOrDefault(letter, 0) + count);
      }
    }
    return letterCounter;
  }

  /**
   * This method is used to serve as a counter of how many distinct baby names had a certain rank
   * within a year range
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @param rank      the given rank
   * @return the found baby names mapped to how many times they appear at that rank within the year
   * range
   */
  public Map<String, Integer> countAllNamesAtRank(String gender, int yearStart, int yearEnd,
      int rank) {
    Map<String, Integer> nameAtRankCounter = new TreeMap<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      String nameAtRank = findNameFromRankAndGender(gender, year, rank);
      nameAtRankCounter.put(nameAtRank, nameAtRankCounter.getOrDefault(nameAtRank, 0) + 1);
    }
    return nameAtRankCounter;
  }

  /**
   * This method counts the number of baby names born with that first letter in the specified year
   * and the number of babies as well.
   *
   * @param gender Baby's gender
   * @param letter the requested letter
   * @param year   the given year
   * @return a count of the number of baby names born with a specific first letter and number of
   * babies born with that name
   */
  public int[] letterCount(String gender, String letter, int year) {
    List<Baby> babyNames = desiredGenderMap(gender).get(year);
    int namesCount = 0;
    int babiesCount = 0;
    for (Baby baby : babyNames) {
      if (baby.startsWith(letter)) {
        namesCount++;
        babiesCount += baby.getCount();
      }
    }
    return new int[]{namesCount, babiesCount};
  }

  /**
   * This method returns a string array of all the unique girl names within a year range that
   * contain the specified first letter. It is used to help the program find the most popular
   * letter.
   *
   * @param gender    Baby's gender
   * @param letter    the requested letter
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a string array of unique baby names that start with the specified letter within the
   * year range
   */
  public String[] allNamesWithLetter(String gender, String letter, int yearStart, int yearEnd) {
    Set<String> allNames = new TreeSet<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      List<Baby> babyNamesForThatYear = desiredGenderMap(gender).get(year);
      for (Baby baby : babyNamesForThatYear) {
        if (baby.startsWith(letter)) {
          allNames.add(baby.getName());
        }
      }
    }
    String[] allNamesArray = new String[allNames.size()];
    return allNames.toArray(allNamesArray);
  }

  /**
   * This method serves as a counter to keep track of how many times within a year range a
   * particular name was the top ranked name.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a map mapping baby names with the number of times that they were the most popular name
   */
  public Map<String, Integer> countTopRankedNames(String gender, int yearStart, int yearEnd) {
    Map<String, Integer> nameCounter = new TreeMap<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      String nameForThatYear = findNameFromRankAndGender(gender, year, 1);
      nameCounter.put(nameForThatYear, nameCounter.getOrDefault(nameForThatYear, 0) + 1);
    }
    return nameCounter;

  }

  /**
   * This method uses the fact that each list in the map is listed in the correct order as the file
   * in returning the rank of the specified name/gender pair.
   *
   * @param name   Baby's name
   * @param gender Baby's gender
   * @param year   the specified year
   * @return the rank within the specific year that matches the name/gender request
   */
  public int findRankFromNameAndGender(String name, String gender, int year) {
    List<Baby> babyNames = desiredGenderMap(gender).get(year);
    if (!babyNames.contains(new Baby(name))) {
      return 0;
    }
    return babyNames.indexOf(new Baby(name)) + 1;
  }


  /**
   * This method returns the name found at the specified rank/gender within the specified year
   *
   * @param gender Baby's gender
   * @param year   the specified year
   * @param rank   the specified rank
   * @return the baby's name at a particular rank
   */
  public String findNameFromRankAndGender(String gender, int year, int rank) {
    List<Baby> babyNames = desiredGenderMap(gender).get(year);
    if (rank > babyNames.size() || rank == 0) {
      return NO_NAME_FOUND;
    }
    return babyNames.get(rank - 1).getName();
  }

  /**
   * This method is used to return all the information from the dataset for the specified gender. If
   * the requested gender isn't male or female, it returns an empty hashmap which the program
   * realizes to return an InvalidParameterException
   *
   * @param gender the specified gender
   * @return a map containing the dataset information for that gender
   */
  public Map<Integer, List<Baby>> desiredGenderMap(String gender) {
    if (gender.equalsIgnoreCase(MALE)) {
      return allMaleBabiesInDataSet;
    } else if (gender.equalsIgnoreCase(FEMALE)) {
      return allFemaleBabiesInDataSet;
    } else {
      return new HashMap<>();
    }
  }

}
