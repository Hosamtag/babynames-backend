package names;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * This class is designed to find the directory that includes all the files and reading all the data
 * into a map of year and its list of Baby's and corresponding gender. This class will throw an
 * InvalidParameterException if the provided dataset does not exist. This class depends on the Baby
 * class in order to correctly create the maps corresponding to each gender.
 *
 * @author Hosam Tageldin
 */

public class YOBFileReader {

  private static final String URL_LOCATION = "https://www2.cs.duke.edu/courses/fall20/compsci307d/assign/01_data/data/";
  private static final String WEB = "WEB";
  private static final String ZIP = "ZIP";
  private static final String ZIP_NAME = "names.zip";
  private static final String HREF_REGEX = "href\\s?=\\s?\"([^\"]+)\"";
  private static final String NONEXISTENT_DIRECTORY = "Nonexistent Directory!";
  private String fileType;
  private String dataSet;
  private final Pattern hrefPattern;

  /**
   * Reads all files involved in a dataset/fileType
   *
   * @param fileType "web" "zip" or "local" file type to open
   * @param dataSet  specific dataset to read from
   */
  public YOBFileReader(String fileType, String dataSet) {
    this.fileType = fileType;
    this.dataSet = dataSet;
    this.hrefPattern = Pattern.compile(HREF_REGEX);

  }

  /**
   * Returns a map with the keys corresponding to each year in the data set and the value is an
   * inorder list of all the baby Objects read off from the file
   *
   * @param gender gender of baby to return information for
   * @return all the gendered babies in the data set in a map
   */
  public Map<Integer, List<Baby>> allBabiesInDataSet(String gender) {
    Map<Integer, List<Baby>> allBabiesInDataSet = new HashMap<>();
    openBasedOnType(allBabiesInDataSet, gender);
    return allBabiesInDataSet;
  }

  private void openBasedOnType(Map<Integer, List<Baby>> babyMap, String gender) {
    try {
      if (fileType.equalsIgnoreCase(WEB)) {
        collectYearsFromWeb(babyMap, gender);
      } else if (fileType.equalsIgnoreCase(ZIP)) {
        collectYearsFromZip(babyMap, gender);
      } else {
        collectYearsFromLocalPath(babyMap, gender);
      }
    } catch (Exception e) {
      // babyMap will remain empty and InvalidParameterException will get thrown later
    }
  }


  private void collectYearsFromWeb(Map<Integer, List<Baby>> babyMap, String gender)
      throws Exception {
    URL folderLocation = new URL(URL_LOCATION + dataSet);
    BufferedReader readFolder = new BufferedReader(
        new InputStreamReader(folderLocation.openStream()));
    String fileLine;
    while ((fileLine = readFolder.readLine()) != null) {
      Matcher matcher = hrefPattern.matcher(fileLine);
      if (matcher.find(0)) {
        String hrefValue = matcher.group(1);
        int year = extractYear(hrefValue);
        if (year == 0) {
          continue;
        }
        babyMap.put(year, allBabiesInFile(openWebURL(hrefValue), gender));
      }
    }
  }

  private BufferedReader openWebURL(String fileLink) throws IOException {
    URL webLocation = new URL(URL_LOCATION + dataSet + "/" + fileLink);
    return new BufferedReader(new InputStreamReader(webLocation.openStream()));
  }

  private void collectYearsFromZip(Map<Integer, List<Baby>> babyMap, String gender)
      throws Exception {
    FileInputStream fis = new FileInputStream(getPathToDataSource(ZIP_NAME));
    BufferedInputStream bis = new BufferedInputStream(fis);
    ZipInputStream zis = new ZipInputStream(bis);
    ZipEntry ze;
    while ((ze = zis.getNextEntry()) != null) {
      int year = extractYear(ze.getName());
      babyMap.put(year, allBabiesInFile(openLocalZIPFile(ze), gender));
    }
  }

  private BufferedReader openLocalZIPFile(ZipEntry zipFileToOpen) throws Exception {
    ZipFile zipFile = new ZipFile(getPathToDataSource(ZIP_NAME));
    InputStream stream = zipFile.getInputStream(zipFileToOpen);
    return new BufferedReader(new InputStreamReader(stream));
  }

  private void collectYearsFromLocalPath(Map<Integer, List<Baby>> babyMap, String gender)
      throws Exception {
    File dir = new File(getPathToDataSource(dataSet));
    for (File child : dir.listFiles()) {
      //NullPointerException noted, InvalidParameterException will be thrown if there is invalid dataset
      int year = extractYear(child.getName());
      babyMap.put(year, allBabiesInFile(openLocalFile(child), gender));
    }
  }

  private BufferedReader openLocalFile(File fileToOpen) throws Exception {
    return new BufferedReader(new FileReader(fileToOpen));
  }

  private List<Baby> allBabiesInFile(BufferedReader br, String gender) throws Exception {
    List<Baby> babyList = new ArrayList<>();
    String lineData;
    while ((lineData = br.readLine()) != null) {
      String[] lineDataArray = lineData.split(",");
      if (lineDataArray[1].equals(gender)) {
        babyList.add(new Baby(lineDataArray[0], Integer.parseInt(lineDataArray[2])));
      }
    }
    return babyList;
  }

  private int extractYear(String fileName) {
    StringBuilder year = new StringBuilder();
    for (int i = 0; i < fileName.length(); i++) {
      if (Character.isDigit(fileName.charAt(i))) {
        year.append(fileName.charAt(i));
      }
    }
    if (year.length() != 4) {
      return 0;
    }
    return Integer.parseInt(year.toString());
  }

  private String getPathToDataSource(String dataSource) throws InvalidParameterException {
    try {
      return String
          .valueOf(Paths.get(Questions.class.getClassLoader().getResource(dataSource).toURI()));
      //NullPointerException noted, InvalidParameterException will be thrown if there is invalid dataset
    } catch (URISyntaxException e) {
      throw new InvalidParameterException(NONEXISTENT_DIRECTORY);
    }
  }

}


