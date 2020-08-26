## Image Compare API
This is an image compare api done as an assignment for SRE. The image compare API uses absolute path names of two images in the form of csv to compare the files. The output is in the form of results csv containing the similarity score and time elapsed to do the comparison.

- [Quick start](#quick-start)
- [Solution Design](#solution-design)
- [Solution Implementation](#solution-implementation)
- [API Documentation](#api-documentation)
- [Quick Build and run](#quick-build-and-run)
- [Latest Releases](#latest-releases)
- [API Testing](#api-testing)

### Quick Start
See the project [Read-me](https://github.com/onnes-sam/image-compare) here.

### Solution Design
The solution is designed as a web page to work in an OS (Mac/Windows) with a web browser through API calls. The index page consists of a place holder to upload the csv file. Processing the csv file happens with the same API. Once the processing is done and the scores are updated, the link to download the csv is updated on the results page.

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
@GetMapping("/export-results")
    public void exportCSV(HttpServletResponse response) throws Exception
```
The processing of the csv has the image compare logic which accepts the file names and updates the CompareProp object for similarity and time elapsed
```markdown
public double ImageCompare(String fileName1, String fileName2)
```

View: The view consists of a index.html and results page. Based on the API, the corresponding html is loaded.

[Index Page](docs/index.html.PNG)

[Results Page](docs/results.html.PNG)

### Quick Build and run
```
mvn clean install
java -jar image-compare/target/compare-0.0.1-SNAPSHOT.jar
```
### API documentation
Once the application is run, the api documentation can be viewed here:  [API-documentation](http://localhost:8080/swagger-ui.html#/)

### Latest Releases
See [the Releases section of the GitHub project](https://github.com/onnes-sam/image-compare/releases) for changelogs for each release version of Image-Compare

### API Testing
The API can be verified to work through [Postman scripts](https://github.com/onnes-sam/image-compare/blob/master/postman/Image-Compare-PM-Testing.postman_collection.json)
* Postman can be downloaded here [Windows64](https://dl.pstmn.io/download/latest/win64) and [MacOS](https://dl.pstmn.io/download/latest/osx)
* Import the collection in Postman and run the scripts

