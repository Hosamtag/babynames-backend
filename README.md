data
====

This project uses data about [baby names from the US Social Security Administration](https://www.ssa.gov/oact/babynames/limits.html) to answer specific questions. 


Name: Hosam Tageldin

### Timeline

Start Date: 8/22/2020

Finish Date: 9/8/2020

Hours Spent: 75

### Resources Used
**Online resources used in the development of this program include:**

"Smartly Load your Properties" by Vladimir Roubstov (https://www.infoworld.com/article/2077352/smartly-load-your-properties.html),
"Different ways of Reading a text file in Java" by Pankaj Kumar
(https://www.geeksforgeeks.org/different-ways-reading-text-file-java/),
"Reading Directly from a URL" from the Java tutorial 
(https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html), and
the Class ZipInputStream resource (https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/zip/ZipInputStream.html).

These online resources helped with reading the inputs from the many sources
of files that were available. 

### Running the Program

**Main class:** 
The Questions class is the main class of this program.

**Key Inputs**
When calling the Questions class to run the program, the two parameters are "fileType" and
"dataSet". Changing these two parameters is the method to change the source and folder for the program to
read from. The fileType parameter supports the Strings "Zip" "Web" and "Local" which tells
the program where to read the data set from. The dataSet parameter is the specific folder
in which the program will read the files from.

**Data files used to test:** 
Local files were used to test the project and those files
can be found in the data folder. Files found on the following URL: 
(https://www2.cs.duke.edu/courses/fall20/compsci307d/assign/01_data/data/ssa_complete/)
were also used to test the project's ability to read online resources.
A ZIP file that is found in the data folder was also used to test the ability
of the program to read from a local ZIP file.

**Required Data/Resource Files:** 
This program can handle URL, local and zip files. The file names need to have 
the year of the data somewhere in the name, but it doesn't matter where.
The file content itself should be in the following format: "Name","Gender",
"Count". The order of gender within the file does not matter. However, the 
names need to be listed in order of decreasing count for the program
to work correctly.

**Errors program can handle:** 
1. Program can handle invalid or empty data sources. Invalid data sources
will throw an InvalidParameterException and empty data sources get interpretted
as data sources with "No Name Found" for both genders. This same "No Name Found"
will also appear if one of the genders does not appear in a file.

2. Empty,Invalid, or Nonsensical Ranger of years are handled by the program
by also throwing out an InvalidParameterException. The program makes sure
to check out the inputted parameters before attempting any calculations.

3. Incorrect case for names or gender don't cause any error for the program
in the completion of the algorithms. The program compares without looking
at case of the names so "SaM" and "sAm" are the same name. Also, "F" and "f"
both signify female, and same for male.

4. The program will also throw an InvalidParameterException for genders
that aren't "M/m" or "F/f".

**Extra Features:**
1. Reading from a local ZIP File using ZipInputStream is provided as on option
for the program.
2. The program can also find the name that occurs as a derivative for other names.
This name is referred to as the highest occurring prefix. The program will return
a list of all the derivative names associated with the highest occurring prefix.
3. The program can find the most popular letter for both males and females, rather
than just females.
4. In addition to providing the name with the highest rank change, the program
also returns the rank change associated with that name.
5. The program will also return the highest average rank when returning
the name with the highest average rank in a range of years.


### Notes/Assumptions
Assumptions:
* Names are in descending order of rank/count. 
* There are no duplicates of name/gender pair within a given year.
* The file name contains the represented year and no other numbers (Albeit, program would still function if first four numbers were the year, and other numbers came after)

### Impressions
Future efforts in this project include: 
* Expanding the Baby class to contain more information, therefore reducing the work that other classes must do
* a more effective algorithm to find the highest occurring name that serves as a prefix for the other
names in a list
* a faster way to find the highest rank change from given year range


