package names;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsTest {
  //fileType can be "web" "local" or "Zip" and dataSet is the folder to read from
  private static final Questions analyzeWebComplete = new Questions("web", "ssa_complete");
  private static final Questions analyzeWeb2000s = new Questions("web", "ssa_2000s");
  private static final Questions analyzePersonalTests1 = new Questions("local", "ssa_personal");
  private static final Questions analyzeBoysFirst = new Questions("local", "ssa_boys_first");
  private static final Questions analyzeLocalComplete = new Questions("local", "ssa_complete");
  private static final Questions analyzeLocal2000s = new Questions("Local", "ssa_2000s");
  private static final Questions analyzeZIPComplete = new Questions("zip", "ssa_complete");
  private static final Questions analyzeInvalidFile = new Questions("local", "invalid_file");


  /**
   * This test verifies that top Ranked name works for files with both genders
   */
  @org.junit.jupiter.api.Test
  void verifyTopRankedName() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Mary", "John"},
        analyzeWebComplete.topRankedMaleAndFemaleName(1900));
    assertArrayEquals(new String[]{"Maria", "Jonathan"},
        analyzePersonalTests1.topRankedMaleAndFemaleName(2000));
  }

  /**
   * This test verifies that top Ranked name works when the file only has male names
   */
  @org.junit.jupiter.api.Test
  void topRankedNameNoFemaleBabies() throws InvalidParameterException {
    assertEquals("No Name Found", analyzePersonalTests1.topRankedMaleAndFemaleName(2001)[0]);
  }

  /**
   * This test verifies that top Ranked name works when the file only has female names
   */
  @org.junit.jupiter.api.Test
  void topRankedNameNoMaleBabies() throws InvalidParameterException {
    assertEquals("No Name Found", analyzePersonalTests1.topRankedMaleAndFemaleName(2002)[1]);
  }


  /**
   * This test verifies letterCount works for normal files.It returns an int array with the first
   * index being the number of names that start with that letter and the second is how many total
   * babies were given that name
   */
  @org.junit.jupiter.api.Test
  void verifyNameAndTotalBabiesCount() throws InvalidParameterException {
    assertArrayEquals(new int[]{4, 50000},
        analyzePersonalTests1.nameAndTotalBabiesCount("F", "A", 1999));
    assertArrayEquals(new int[]{2, 20000},
        analyzePersonalTests1.nameAndTotalBabiesCount("M", "M", 1999));
    assertArrayEquals(new int[]{3, 111},
        analyzeWebComplete.nameAndTotalBabiesCount("F", "Q", 1900));
  }

  /**
   * This test verifies letterCount returns [0,0] for letters that aren't in the file
   */
  @org.junit.jupiter.api.Test
  void verifyNameAndTotalBabiesCountForNonExistentFemale() throws InvalidParameterException {
    assertArrayEquals(new int[]{0, 0},
        analyzePersonalTests1.nameAndTotalBabiesCount("F", "Z", 1999));
  }

  /**
   * This test verifies letterCount doesn't crash for an Empty File
   */
  @org.junit.jupiter.api.Test
  void verifyNameAndTotalBabiesCountForNonExistentMale() throws InvalidParameterException {
    assertArrayEquals(new int[]{0, 0},
        analyzePersonalTests1.nameAndTotalBabiesCount("M", "P", 1999));
  }

  /**
   * This test is to make sure Find All Ranks works in general
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanksInDataSet() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:1", "2000:2", "2001:0", "2002:3", "2003:0", "2004:2"},
        analyzePersonalTests1.findAllRanksInDataSet("Amina", "F"));
  }

  /**
   * This test is to make sure Find all ranks works when the name doesn't exist
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanksInDataSetNonExistentName() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:0", "2000:0", "2001:0", "2002:0", "2003:0", "2004:0"},
        analyzePersonalTests1.findAllRanksInDataSet("Amtrak", "M"));
  }

  /**
   * This test is to make sure males and females having the same name doesn't affect anything
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanksMaleAndFemaleHaveSameName() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:5", "2000:4", "2001:5", "2002:0", "2003:0", "2004:6"},
        analyzePersonalTests1.findAllRanksInDataSet("Logan", "M"));
    assertArrayEquals(new String[]{"1999:3", "2000:6", "2001:0", "2002:8", "2003:0", "2004:4"},
        analyzePersonalTests1.findAllRanksInDataSet("Logan", "F"));
  }

  /**
   * This test is to verify most recent year same rank works in general
   */
  @org.junit.jupiter.api.Test
  void verifyMostRecentYearSameRank() throws InvalidParameterException {
    assertEquals("Megan", analyzePersonalTests1.mostRecentYearSameRank("Maria", "F", 2000));
    assertEquals("Nathan", analyzePersonalTests1.mostRecentYearSameRank("Arman", "M", 1999));
  }

  /**
   * Most recent year works with non existent names
   */
  @org.junit.jupiter.api.Test
  void verifyMostRecentYearSameRankNonExistentName() throws InvalidParameterException {
    assertEquals("No Name Found",
        analyzePersonalTests1.mostRecentYearSameRank("Mathematics", "M", 2000));
    assertEquals("No Name Found", analyzePersonalTests1.mostRecentYearSameRank("Roses", "F", 2001));
    assertEquals("No Name Found",
        analyzePersonalTests1.mostRecentYearSameRank("LeBron", "M", 2003));
  }

  /**
   * Most Recent year works with non existent ranks for the most recent year
   */
  @org.junit.jupiter.api.Test
  void verifyMostRecentYearSameRankNonExistentRank() throws InvalidParameterException {
    assertEquals("No Name Found", analyzePersonalTests1.mostRecentYearSameRank("Suzie", "F", 2000));
  }

  /**
   * Most Popular Names works in general
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularNames() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Michael", "1"},
        analyzePersonalTests1.mostPopularNames("M", 1999, 1999));
    assertArrayEquals(new String[]{"Amina", "1"},
        analyzePersonalTests1.mostPopularNames("F", 1999, 1999));
  }

  /**
   * Most popular Names displays multiple names when there are ties for the most popular name
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularNamesMultiple() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Jonathan", "Michael", "1"},
        analyzePersonalTests1.mostPopularNames("M", 1999, 2000));
    assertArrayEquals(new String[]{"Amina", "Maria", "1"},
        analyzePersonalTests1.mostPopularNames("F", 1999, 2000));
  }

  /**
   * Returns NoFemaleName and NoMaleName if there are no names with that gender in the year range
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularNamesNoneExisting() throws InvalidParameterException {
    assertArrayEquals(new String[]{"No Name Found", "1"},
        analyzePersonalTests1.mostPopularNames("F", 2001, 2001));
    assertArrayEquals(new String[]{"No Name Found", "1"},
        analyzePersonalTests1.mostPopularNames("M", 2002, 2002));
  }

  /**
   * Most popular letter for girls returns correct array in alphabetical order
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularLetter() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Sam", "Samantha", "Sarah", "Suzie"},
        analyzePersonalTests1.mostPopularLetter("F", 1999, 2002));
  }

  /**
   * Most popular letter for girls chooses alphabetically first letter when there is a tie
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularLetterTie() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Amina", "Amrita", "Amy", "Anna"},
        analyzePersonalTests1.mostPopularLetter("F", 1999, 2004));
  }

  /**
   * No errors occur when there are no girl names in the file
   */
  @org.junit.jupiter.api.Test
  void verifyMostPopularLetterNone() throws InvalidParameterException {
    assertArrayEquals(new String[0], analyzePersonalTests1.mostPopularLetter("F", 2001, 2001));
  }

  /**
   * Verifies findAllRanks works in general
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanks() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:5", "2000:4", "2001:5"},
        analyzePersonalTests1.findAllRanks("Logan", "M", 1999, 2001));
  }

  /**
   * Verifies findAllRanks works when nonexistent name is provided
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanksNonExistentName() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:0", "2000:0", "2001:0"},
        analyzePersonalTests1.findAllRanks("Amtrak", "M", 1999, 2001));
  }

  /**
   * This test is to make sure males and females having the same name doesn't affect anything
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllRanksMaleFemaleDuplicateName() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:5", "2000:4", "2001:5", "2002:0"},
        analyzePersonalTests1.findAllRanks("Logan", "M", 1999, 2002));
    assertArrayEquals(new String[]{"2002:8", "2003:0", "2004:4"},
        analyzePersonalTests1.findAllRanks("Logan", "F", 2002, 2004));
  }

  /**
   * Verifies differenceBetweenRank returns 0 for name at same rank both years
   */
  @org.junit.jupiter.api.Test
  void verifyDifferenceBetweenRank() throws InvalidParameterException {
    assertEquals(0, analyzePersonalTests1.differenceInRank("Logan", "M", 1999, 2001));
  }

  /**
   * Verifies differenceBetweenRank returns 0 for name that doesn't exist in a year
   */
  @org.junit.jupiter.api.Test
  void verifyDifferenceBetweenRankVoidName() throws InvalidParameterException {
    assertEquals(0, analyzePersonalTests1.differenceInRank("Laric", "M", 1999, 2001));
  }

  /**
   * Verifies differenceBetweenRank returns correct name change
   */
  @org.junit.jupiter.api.Test
  void verifyDifferenceBetweenRankLowChange() throws InvalidParameterException {
    assertEquals(1, analyzePersonalTests1.differenceInRank("Samuel", "M", 2000, 2001));
  }

  /**
   * Returns
   */
  @org.junit.jupiter.api.Test
  void verifyNameWithHighestRankChange() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Hosam", "3"},
        analyzeBoysFirst.nameWithHighestRankChange("M", 1900, 1901));
  }

  /**
   * Returns alphabetically first name with highest rank change if there is a tie
   */
  @org.junit.jupiter.api.Test
  void verifyNameWithHighestRankChangeTie() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Arman", "1"},
        analyzePersonalTests1.nameWithHighestRankChange("M", 1999, 2004));
  }

  /**
   * Returns "No Name Found" if no name repeats in both years
   */
  @org.junit.jupiter.api.Test
  void verifyNameWithHighestRankChangeNonExistent() throws InvalidParameterException {
    assertArrayEquals(new String[]{"No Name Found", "0"},
        analyzeBoysFirst.nameWithHighestRankChange("M", 1900, 1902));
  }

  /**
   * AverageRank is 0 if name not found in any of the years
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRankNonExistentName() throws InvalidParameterException {
    assertEquals(0, analyzePersonalTests1.averageRank("Arman", "M", 2000, 2003));
  }

  /**
   * AverageRank doesn't include years that the name didn't exist
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRankVoidSomeYears() throws InvalidParameterException {
    assertEquals(4.5, analyzePersonalTests1.averageRank("Logan", "M", 2000, 2003));
  }

  /**
   * AverageRank calculates correctly if name is included in every year
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRank() throws InvalidParameterException {
    assertEquals(4.5, analyzePersonalTests1.averageRank("Logan", "M", 1999, 2000));
  }

  /**
   * Return alphabetically first name if there is a tie
   */
  @org.junit.jupiter.api.Test
  void verifyHighestAverageRankTie() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Jonathan", "1.0"},
        analyzePersonalTests1.highestAverageRank("M", 1999, 2001));
  }

  /**
   * Returns correct name and average rank if name only exists in one year
   */
  @org.junit.jupiter.api.Test
  void verifyHighestAverageRankExistsInOneYear() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Maria", "1.0"},
        analyzePersonalTests1.highestAverageRank("F", 1999, 2000));
  }

  /**
   * Returns correct name and average rank if name exists in every year
   */
  @org.junit.jupiter.api.Test
  void verifyHighestAverageRank() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Amina", Float.toString((float) 4 / 3)},
        analyzeBoysFirst.highestAverageRank("F", 1900, 1902));
  }

  /**
   * Correctly skips over years not involved and determines most recent number of years
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRankMostRecentNumberOfYears() throws InvalidParameterException {
    assertEquals(5.5, analyzePersonalTests1.averageRankMostRecentNumberOfYears("Logan", "M", 4));
  }

  /**
   * Correctly determines if most recent number of years is full data set and name is not in every
   * year
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRankMostRecentNumberOfYearsFullDataSet() throws InvalidParameterException {
    assertEquals(2.5, analyzeBoysFirst.averageRankMostRecentNumberOfYears("Hosam", "M", 3));
  }

  /**
   * Correctly determines if most recent number of years is full data set and name exists in every
   * year
   */
  @org.junit.jupiter.api.Test
  void verifyAverageRankMostRecentNumberOfYearsEveryYear() throws InvalidParameterException {
    assertEquals(2.0, analyzeBoysFirst.averageRankMostRecentNumberOfYears("Sam", "F", 3));
  }

  /**
   * Returns correctly for existing rank
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllNamesWithRank() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1999:Nathan", "2000:Mark", "2001:Laric"},
        analyzePersonalTests1.findAllNamesWithRank("M", 1999, 2001, 3));
  }

  /**
   * Returns correctly for non-existing rank
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllNamesWithRankNonExistingRank() throws InvalidParameterException {
    assertArrayEquals(
        new String[]{"1999:No Name Found", "2000:No Name Found", "2001:No Name Found"},
        analyzePersonalTests1.findAllNamesWithRank("M", 1999, 2001, 103));
  }

  /**
   * Returns correctly for duplicate names at rank
   */
  @org.junit.jupiter.api.Test
  void verifyFindAllNamesWithRankDuplicateNames() throws InvalidParameterException {
    assertArrayEquals(new String[]{"1900:Amina", "1901:Amina", "1902:Sam"},
        analyzeBoysFirst.findAllNamesWithRank("F", 1900, 1902, 1));
  }

  /**
   * Correctly returns name holding the rank most often and the number of years at that rank
   */
  @org.junit.jupiter.api.Test
  void verifyNameHoldingRanksMostOften() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Amina", "2"},
        analyzeBoysFirst.nameHoldingRankMostOften("F", 1900, 1902, 1));
  }

  /**
   * Returns No Name Found if that gender doesn't exist more time than it does
   */
  @org.junit.jupiter.api.Test
  void verifyNameHoldingRanksMostOftenNoMatchingGender() throws InvalidParameterException {
    assertArrayEquals(new String[]{"No Name Found", "2"},
        analyzePersonalTests1.nameHoldingRankMostOften("F", 2001, 2003, 2));
  }

  /**
   * Correctly returns alphabetically ordered list of names in case of tie
   */
  @org.junit.jupiter.api.Test
  void verifyNameHoldingRanksMostOftenTie() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Hosam", "Mark", "1"},
        analyzeBoysFirst.nameHoldingRankMostOften("M", 1900, 1901, 1));
  }


  /**
   * Returns no prefix found if there are no derivatives of any names in the dataset
   */
  @org.junit.jupiter.api.Test
  void verifyHighestOccurringPrefixNoPrefixFound() throws InvalidParameterException {
    assertArrayEquals(new String[]{"No Prefix Found"},
        analyzePersonalTests1.highestOccurringPrefix("M", 1999, 2004));
  }

  /**
   * Correctly identifies the highest occurring prefix
   */
  @org.junit.jupiter.api.Test
  void verifyHighestOccurringPrefix() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Sam", "Samantha"},
        analyzePersonalTests1.highestOccurringPrefix("F", 1999, 2004));
  }

  /**
   * Returns alphabetically first prefix if there is a tie
   */
  @org.junit.jupiter.api.Test
  void verifyHighestOccurringPrefixTie() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Abe", "Abel"},
        analyzeBoysFirst.highestOccurringPrefix("M", 1900, 1902));
  }

  /**
   * Verifies invalid parameter exception is thrown if invalid file is inputted
   */
  @org.junit.jupiter.api.Test
  void verifyInvalidParameterExceptionThrownWithInvalidFile() {
    assertThrows(InvalidParameterException.class, () ->
        analyzeInvalidFile.nameAndTotalBabiesCount("M", "L", 2010));
  }

  /**
   * Verifies program interprets empty files as "no name found" files
   */
  @org.junit.jupiter.api.Test
  void topRankedNameEmptyFile() throws InvalidParameterException {
    assertArrayEquals(new String[]{"No Name Found", "No Name Found"},
        analyzePersonalTests1.topRankedMaleAndFemaleName(2003));
  }

  /**
   * Verifies InvalidParameterException is thrown if invalid range of years inputted
   */
  @org.junit.jupiter.api.Test
  void verifyPersonalExceptionThrownForInvalidYearRange() {
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.nameAndTotalBabiesCount("M", "L", 2054));
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.mostPopularNames("M", 2000, 2025));
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.mostPopularNames("M", 1850, 2010));
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.mostPopularNames("M", 2010, 2000));
  }

  /**
   * Verifies that the case of gender and name don't matter for correct results
   */
  @org.junit.jupiter.api.Test
  void verifyThatCaseDoesNotAffectOutput() throws InvalidParameterException {
    assertEquals(4.5, analyzePersonalTests1.averageRank("lOGaN", "m", 2000, 2003));
    assertEquals(2.0, analyzeBoysFirst.averageRankMostRecentNumberOfYears("saM", "f", 3));
  }

  /**
   * Verifies that InvalidParameterException is thrown for genders that aren't "M" or "F"
   */
  @org.junit.jupiter.api.Test
  void verifyExceptionThrownForInvalidGenders() {
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.nameAndTotalBabiesCount("Y", "L", 2000));
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.nameAndTotalBabiesCount("C", "L", 2000));
    assertThrows(InvalidParameterException.class, () ->
        analyzeWebComplete.nameAndTotalBabiesCount("L", "L", 2000));
  }

  /**
   * Verifies the program can read different sources locally
   */
  @org.junit.jupiter.api.Test
  void verifyProgramReadsDifferentSourcesLocally() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Mary", "John"},
        analyzeLocalComplete.topRankedMaleAndFemaleName(1900));
    assertArrayEquals(new String[]{"Emily", "Jacob"},
        analyzeLocal2000s.topRankedMaleAndFemaleName(2000));
  }

  /**
   * Verifies the program can read from different sources on the web
   */
  @org.junit.jupiter.api.Test
  void verifyProgramReadsDifferentSourcesFromWeb() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Mary", "John"},
        analyzeWebComplete.topRankedMaleAndFemaleName(1900));
    assertArrayEquals(new String[]{"Emily", "Jacob"},
        analyzeWeb2000s.topRankedMaleAndFemaleName(2000));
  }

  /**
   * Verifies the program can read from the local names.zip folder
   */
  @org.junit.jupiter.api.Test
  void verifyProgramReadsFromLocalZIP() throws InvalidParameterException {
    assertArrayEquals(new String[]{"Mary", "John"},
        analyzeZIPComplete.topRankedMaleAndFemaleName(1900));
    assertArrayEquals(new String[]{"Emily", "Jacob"},
        analyzeZIPComplete.topRankedMaleAndFemaleName(2000));
  }
}