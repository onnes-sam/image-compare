## Image Compare API
This is an image compare api done as an assignment for SRE. The image compare API uses absolute path names of two images in the form of csv to compare the files. The output is in the form of results csv containing the similarity score and time elapsed to do the comparison.

### Quick Start
https://github.com/onnes-sam/image-compare

### Solution Design

The solution is designed to work as a web page working with almost all OS with a browser. The index page consists of a place holder to upload the csv file. Processing the csv file happens with the same API. Once the processing is done and the scores are updated, the link to download the csv is updated on the results page.

### Solution Implementation

The solution follows a Model, View, Controller (MVC) pattern. 

Model: The model is of type CompareProp which holds the image locations, similarity and time-elapsed

```markdown
public class CompareProp {
    @CsvBindByName
    private String image1;
    @CsvBindByName
    private String image2;
    @CsvBindByName
    private double similarity;
    @CsvBindByName
    private double elapsed;
```

Controller: The upload controller has the API's to upload, process a csv and update the csv for the results. 

```markdown
@PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model)
```
The processing of the csv has the image compare logic which accepts the file names and updates the CompareProp object for similarity and time elapsed
```markdown
public double ImageCompare(String fileName1, String fileName2)
```

View: The view consists of a index.html and results page. Based on the loaded API, the corresponding html is updated and loaded.

### API documentation
Once the application is run, the api documentation can be viewed here
http://localhost:8080/swagger-ui.html#/


### Quick Build

```
cd to project folder
./mvnw clean install
```

### Build 'fat jar' and run
```
mvn clean install
java -jar image-compare/target/image-compare.jar
```
