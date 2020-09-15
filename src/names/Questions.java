package names;

import java.util.*;

/**
 * The Questions class will contain the methods for the user to call whenever an answer to a
 * question is requested. It will utilize the other classes in order to return the correct answer to
 * the question. Assumptions include that girls names and boys names are in descending order, and
 * that there are no duplicates of name/gender pairing within a file for the year. This class
 * depends on the DataSetInformation class to do the heavy lifting in answering the questions. To
 * use this class, call an instance of the Questions class and call any of the public methods within
 * it to answer any question about the baby names.
 *
 * @author Hosam Tageldin
 */
public class Questions {

  private static final String MALE = "M";
  private static final String FEMALE = "F";
  private static final String NO_NAME_FOUND = "No Name Found";
  private static final String NO_PREFIX_FOUND = "No Prefix Found";
  private static final String INVALID_PARAMETER_MESSAGE = "Invalid year range, gender or file name";
  private final DataSetInformation dataSetInfo;

  /**
   * When calling the Questions class, one should include the fileType(web, local, or zip) and the
   * specific dataset within those data types.
   *
   * @param fileType "Web" "Local" or "Zip" supporting different data sources
   * @param dataSet  the specific set to read within the source
   */
  public Questions(String fileType, String dataSet) {
    this.dataSetInfo = new DataSetInformation(fileType, dataSet);
  }

  /**
   * This method is used to return a list of all the names that include the highest occurring prefix
   * that is also a name. It will return the alphabetically first prefix if there are ties in the
   * highest occurring prefix.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return all the derivative names of the highest occurring prefix name
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] highestOccurringPrefix(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    Set<Baby> allNamesInRange = dataSetInfo.allNamesInYearRange(gender, yearStart, yearEnd);
    Baby maxPrefix = dataSetInfo.findMaxPrefixInRange(allNamesInRange);
    if (maxPrefix.getNameDerivatives().size() == 1) {
      return new String[]{NO_PREFIX_FOUND};
    }
    return maxPrefix.getNameDerivatives().toArray(new String[0]);
  }

  /**
   * This class returns the name(s) that held a specific rank the most often within a year range and
   * the number of years at that rank
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @param rank      the requested rank for the Baby
   * @return the String array with the name(s) that held the rank most often, last index is the
   * number of years holding that rank
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] nameHoldingRankMostOften(String gender, int yearStart, int yearEnd, int rank)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    Map<String, Integer> nameCounter = dataSetInfo
        .countAllNamesAtRank(gender, yearStart, yearEnd, rank);
    return findMostCommonKeysAndMaxValue(nameCounter);
  }

  /**
   * This method will provide the name that matched the given rank/gender for every year within the
   * year range.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @param rank      the requested rank for the Baby
   * @return a String array with all the names associated with each year/gender/rank
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] findAllNamesWithRank(String gender, int yearStart, int yearEnd, int rank)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    List<String> allRanks = new ArrayList<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      allRanks.add(year + ":" + dataSetInfo.findNameFromRankAndGender(gender, year, rank));
    }
    return allRanks.toArray(new String[0]);
  }

  /**
   * This method will return the average rank for a given name/gender for the most recent
   * numberOfYears in a dataset.
   *
   * @param name          Baby's name
   * @param gender        Baby's gender
   * @param numberOfYears most recent number of years within a data set
   * @return a float representing the average rank of the name/gender within the specified years
   * @throws InvalidParameterException if filename, the years(numberOfYears greater than dataset
   *                                   size) or gender is invalid
   */
  public float averageRankMostRecentNumberOfYears(String name, String gender, int numberOfYears)
      throws InvalidParameterException {
    if (numberOfYears > dataSetInfo.desiredGenderMap(gender).keySet().size()) {
      throw new InvalidParameterException(INVALID_PARAMETER_MESSAGE);
    }
    int mostRecentYear = Collections.max(dataSetInfo.desiredGenderMap(gender).keySet());
    int startYear = mostRecentYear - numberOfYears + 1;
    return averageRank(name, gender, startYear, mostRecentYear);
  }

  /**
   * Returns the name with the highest average rank during the specified time and the average rank
   * of this name
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a String array, first index is the name with the highest average rank, second index is
   * the average rank of that name
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] highestAverageRank(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    float highestAverageRank = Integer.MAX_VALUE;
    String nameWithHighestRank = NO_NAME_FOUND;
    for (Baby babyName : dataSetInfo.allNamesInYearRange(gender, yearStart, yearEnd)) {
      float averageRank = averageRank(babyName.getName(), gender, yearStart, yearEnd);
      if (averageRank < highestAverageRank) {
        highestAverageRank = averageRank;
        nameWithHighestRank = babyName.getName();
      }
    }
    return new String[]{nameWithHighestRank, Float.toString(highestAverageRank)};
  }

  /**
   * This method will return the average rank from the given parameters. If a baby name does not
   * exist within a certain year, then the method will ignore that year in calculating it's rank.
   *
   * @param name      Baby's name
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a float representing the average rank from the given parameters
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public float averageRank(String name, String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    float rankSum = 0, validYears = 0;
    for (int year = yearStart; year <= yearEnd; year++) {
      int rank = dataSetInfo.findRankFromNameAndGender(name, gender, year);
      if (rank != 0) {
        rankSum += rank;
        validYears++;
      }
    }
    if (validYears == 0) {
      return 0;
    }
    return rankSum / validYears;
  }

  /**
   * Method to return the biggest name change for the specified gender. If there is a tie for the
   * highest rank change, method will return the alphabetically first name.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return String array corresponding to the baby name with the biggest rank change and the rank
   * change of that name
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] nameWithHighestRankChange(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    int highestRankChange = 0;
    String greatestRankChangeName = NO_NAME_FOUND;
    for (Baby baby : dataSetInfo.allNamesInYearRange(gender, yearStart, yearEnd)) {
      int babyRankChange = Math.abs(differenceInRank(baby.getName(), gender, yearStart, yearEnd));
      if (babyRankChange > highestRankChange) {
        highestRankChange = babyRankChange;
        greatestRankChangeName = baby.getName();
      }
    }
    return new String[]{greatestRankChangeName, Integer.toString(highestRankChange)};
  }

  /**
   * This method will return the difference in rank from the first given year to the end year. If
   * the baby name does not exist in either of those years, then the method will return 0 instead.
   * This zero does overlap with a name that had no change in rank, but it greatly simplifies other
   * parts of the program.
   *
   * @param name      Baby's name
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return the difference between most recent year and first year in baby name rank
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public int differenceInRank(String name, String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    int firstYearRank = dataSetInfo.findRankFromNameAndGender(name, gender, yearStart);
    int lastYearRank = dataSetInfo.findRankFromNameAndGender(name, gender, yearEnd);
    if (firstYearRank == 0 || lastYearRank == 0) {
      return 0;
    }
    return firstYearRank - lastYearRank;
  }


  /**
   * This method finds all ranks of a name/gender pairing in the given range of years
   *
   * @param name      Baby's name
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return a String array of every year in the range and the rank of that name/gender within that
   * year
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] findAllRanks(String name, String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    List<String> allRanks = new ArrayList<>();
    for (int year = yearStart; year <= yearEnd; year++) {
      allRanks.add(year + ":" + dataSetInfo.findRankFromNameAndGender(name, gender, year));
    }
    return allRanks.toArray(new String[0]);
  }

  /**
   * This method finds the most popular letter for the specified gender within the year range. It
   * then returns a alphabetized list of all the names that start with this most popular letter. If
   * there is a tie, the program will return only the alphabetically first letter.
   *
   * @param gender    Baby's gender
   * @param yearStart start of the year range
   * @param yearEnd   end of the year range
   * @return the String array with alphabetized order of names that start with the most popular
   * letter
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] mostPopularLetter(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    Map<String, Integer> letterCounter = dataSetInfo
        .countAllNamesFirstLetter(gender, yearStart, yearEnd);
    String alphabeticallyFirstPopularLetter = findMostCommonKeysAndMaxValue(letterCounter)[0];
    return dataSetInfo
        .allNamesWithLetter(gender, alphabeticallyFirstPopularLetter, yearStart, yearEnd);
  }


  /**
   * This method finds the most popular name within a certain year by looking at which name was top
   * ranked the most number of years. If there is a tie, then the program will return all the names,
   * with the last index being the number of times these names were the number one rank.
   *
   * @param yearStart the start year of the given range
   * @param yearEnd   the ending year for the given range
   * @param gender    the requested gender
   * @return String array with most popular names and last index is frequency
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] mostPopularNames(String gender, int yearStart, int yearEnd)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, yearStart, yearEnd);
    Map<String, Integer> popularNameCounter = dataSetInfo
        .countTopRankedNames(gender, yearStart, yearEnd);
    return findMostCommonKeysAndMaxValue(popularNameCounter);
  }

  /**
   * This method will return the name from the most recent year that corresponds to the given
   * gender/rank in the given year. If there is no name found, or there is no rank that matches in
   * the most recent year, the string returned will be "No Name Found"
   *
   * @param year   the given
   * @param name   the given baby name
   * @param gender the given baby gender
   * @return String of name with same rank in the most recent year in the data set
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String mostRecentYearSameRank(String name, String gender, int year)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, year, year);
    int rank = dataSetInfo.findRankFromNameAndGender(name, gender, year);
    int mostRecentYear = Collections.max(dataSetInfo.desiredGenderMap(gender).keySet());
    return dataSetInfo.findNameFromRankAndGender(gender, mostRecentYear, rank);
  }

  /**
   * This method utilizes findAllRanks to find all the ranks in the data set given a name and a
   * gender. It first checks that the filename and gender exists before attempting to call
   * findAllRanks in case the provided gender is invalid.
   *
   * @param name   the given baby name
   * @param gender the gender of that baby
   * @return a String array with all the ranks from a name/gender pair in the dataset
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] findAllRanksInDataSet(String name, String gender)
      throws InvalidParameterException {
    int minYearInDataSet;
    int maxYearInDataSet;
    try {
      minYearInDataSet = Collections.min(dataSetInfo.desiredGenderMap(gender).keySet());
      maxYearInDataSet = Collections.max(dataSetInfo.desiredGenderMap(gender).keySet());
    } catch (Exception e) {
      throw new InvalidParameterException(INVALID_PARAMETER_MESSAGE);
    }
    return findAllRanks(name, gender, minYearInDataSet, maxYearInDataSet);
  }

  /**
   * This method finds the number of names with a given gender/first letter and how many babies had
   * that name in that year, if no babies have that letter, then the program will return a {0,0}
   * array
   *
   * @param gender the given gender
   * @param letter the given first letter
   * @param year   the given year
   * @return an integer array, with first index being the number of names and the second index being
   * how many babies had names with that first letter
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public int[] nameAndTotalBabiesCount(String gender, String letter, int year)
      throws InvalidParameterException {
    dataSetInfo.checkValidParameters(gender, year, year);
    return dataSetInfo.letterCount(gender, letter, year);
  }

  /**
   * This method finds the top ranked female and male baby name. If no baby name is found for a
   * specific gender, then "No Name Found" is returned. Assumes that names are listed within each
   * gender group and in descending order
   *
   * @param year the given year
   * @return a String array with first index being the top ranked female name and the second index
   * being the top ranked male name
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public String[] topRankedMaleAndFemaleName(int year) throws InvalidParameterException {
    dataSetInfo.checkValidParameters(MALE, year, year);
    String topRankedFemale = dataSetInfo.findNameFromRankAndGender(FEMALE, year, 1);
    String topRankedMale = dataSetInfo.findNameFromRankAndGender(MALE, year, 1);
    return new String[]{topRankedFemale, topRankedMale};
  }

  private String[] findMostCommonKeysAndMaxValue(Map<String, Integer> counter) {
    int maxValue = (Collections.max(counter.values()));
    List<String> keysWithMaxValueAndMaxValue = new ArrayList<>();
    for (String key : counter.keySet()) {
      if (counter.get(key) == maxValue) {
        keysWithMaxValueAndMaxValue.add(key);
      }
    }
    keysWithMaxValueAndMaxValue.add(Integer.toString(maxValue));
    return keysWithMaxValueAndMaxValue.toArray(new String[0]);
  }

  /**
   * Returns answers to all the questions from Test, Basic and Complete in the ssa_complete dataset
   *
   * @throws InvalidParameterException if filename, the years or gender is invalid
   */
  public static void main(String[] args) throws InvalidParameterException {
    //can change filetype to "Zip", "Local" or "Web' and dataSet to whatever set you would like to use
    Questions babyQuestions = new Questions("web", "ssa_complete");

    System.out.println(Arrays.toString(babyQuestions.topRankedMaleAndFemaleName(2000)));
    System.out.println(Arrays.toString(babyQuestions.nameAndTotalBabiesCount("M", "J", 2000)));
    System.out.println(Arrays.toString(babyQuestions.findAllRanksInDataSet("Jim", "M")));
    System.out.println(babyQuestions.mostRecentYearSameRank("Jim", "M", 2000));
    System.out.println(Arrays.toString(babyQuestions.mostPopularNames("M", 2000, 2018)));
    System.out.println(Arrays.toString(babyQuestions.mostPopularLetter("M", 2000, 2018)));
    System.out.println(Arrays.toString(babyQuestions.findAllRanks("Jim", "M", 2000, 2018)));
    System.out.println(babyQuestions.differenceInRank("Jim", "M", 2010, 2018));
    System.out.println(Arrays.toString(babyQuestions.nameWithHighestRankChange("M", 2000, 2018)));
    System.out.println(babyQuestions.averageRank("Jim", "M", 2000, 2018));
    System.out.println(Arrays.toString(babyQuestions.highestAverageRank("M", 2000, 2018)));
    System.out.println(babyQuestions.averageRankMostRecentNumberOfYears("Jim", "M", 18));
    System.out.println(Arrays.toString(babyQuestions.findAllNamesWithRank("M", 2000, 2018, 28)));
    System.out
        .println(Arrays.toString(babyQuestions.nameHoldingRankMostOften("M", 2000, 2018, 28)));
    System.out.println(Arrays.toString(babyQuestions.highestOccurringPrefix("M", 2000, 2018)));
  }

}
