# Data Assignment- Design Document
### Hosam Tageldin


#### Project's Design Goals
The project's design had the goals of implementing an IPO (input, processing, 
output) system in a clean and readable way. The classes worked together to 
read the requested data source and data set and then use data structures 
to answer questions regarding the information from the files. A lot of the 
features that I wanted to make easy to add included year-to-year comparisons 
of baby names regarding the name's first letter, rank, count and other interesting
specifics, like name prefix. The design goals with this project was to 
separate the work into many classes to handle a chunk of the work. This
goal would then allow for the incorporation of new features in the future.

#### High-Level Design Of Project
This project included 5 classes that worked together in answering a variety of 
questions regarding baby names. The "Baby" class created a Baby object that held
useful information for each baby, including baby name, gender, count for that year,
and a list of all names that had this baby name as a prefix. The Baby class worked with 
the YOBFileReader class (which was used to read the information about baby names
from the web, local files, or a ZIP file) to create the DataSetInformation
class's map. This HashMap had keys corresponding to years provided in the 
data set. The value for each key was a list of baby names in the same order
provided in the files. There were two hashmaps, one for the females and one
for the males. This design decision made it really easy to work with the genders
individually.

The DataSetInformation also made simple, valuable calculations regarding 
the information in the hashmaps, which became very useful when the Main 
class- the Questions class- needed to find an answer to a particular question.
Finally, the InvalidParameterException was thrown whenever the user
inputs an incorrect gender, year range (compared to the provided data set), 
or provided an invalid datasource. 

These classes all interacted together and split up the heavy work needed in finding
the answers to the questions. To sum it up, the Questions class had the methods
corresponding to different questions about the dataset and return the correct
answer by utilizing the DataSetInformation class. The DataSetInformation class
had a HashMap of <Integer, List<Baby>> to allow easy access for different
years and different genders. The Baby class was necessary in creating the 
HashMap in DataSetInformation. Finally, YOBFileReader read through all the files
in the beginning and populated the HashMaps in DataSetInformation before running
any calculations to answer the questions.

#### Assumptions/Decisions Made
One key assumption that was made to simplify the project's design was that
the file names given would include the represented year. This is necessary for
the program to know what year corresponds to each data file information. There
was also the key assumption that the data files provided to the program
would follow the format provided in the NationalReadMe. This format
includes the assumption that the file will follow the Name,Gender,Count
format and that the file would be sorted first based on sex and then on 
number of occurrences. My program runs with either gender listed first.

#### How To Add New Features To Project
New features can be added to this project. Whatever new question needs
to be answered should be a new method in the "Questions" class. If there is any 
new information that needs to be kept track of regarding the Baby name, that
can be updated in the Baby class as well. After creating the new method in the
Questions class, this method could utilize a lot of the methods in the 
DataSetInformation class to answer the new question. However, if there 
needs to be another calculation that isn't included in the DataSetInformation,
that could be added as a new method within this class and then called in the 
Questions class. 

Let's take for example the last bonus question that I was 
not able to complete by the deadline. This question provided a file of
information in the format of baby_name gender name_meaning. The objective
of this question was to add the name_meaning associated with any name in
any of the previous questions/answers. To add this feature to my project,
the first step would be to create a new class called "NameDefinitionReader". 
This file would populate a HashMap data structure with the key being
the name, and the value being that names definition. There should be 
separate names for males and females for ease of access/ same names for both
genders. Once this map gets made, the definitions of any name can be returned
by calling the appropriate gender map and using the .get("Name") in order
to receive that name's definition. The name's definition can also be included
as a value in the Baby class, and the toString() method can return 
"name: meaning". In this case, any time a name gets returned it will be
associated with its definition.
