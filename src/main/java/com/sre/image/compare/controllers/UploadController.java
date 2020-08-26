package com.sre.image.compare.controllers;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.sre.image.compare.domains.CompareProp;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sre.image.compare.logic.ImageCompareLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@Api(value="images comparison", description="Images comparison through csv")
public class UploadController {

    List<CompareProp> image_files;

    @ApiOperation(value = "Upload images in csv for comparison", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded the csv"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {


        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `images` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream(), ByteOrderMark.UTF_8)))) {

                CsvToBean<CompareProp> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CompareProp.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                image_files = csvToBean.parse();
                ImageCompareLogic imagelogic = new ImageCompareLogic();
                int iCompareCount = 0;
                for (CompareProp image_file: image_files)
                {
                    long startTime = System.currentTimeMillis();
                    System.out.println(image_file.getImage1());
                    if (image_file.getImage1().equalsIgnoreCase("") == true || image_file.getImage2().equalsIgnoreCase("") == true || image_file.getImage1() == null || image_file.getImage2() == null)
                    {
                        int error_row = iCompareCount + 1;
                        model.addAttribute("message", "An error occurred while processing the CSV file. One of the image path in row:" + error_row +" is empty");
                        model.addAttribute("status", false);
                        image_files.get(iCompareCount).setIsRowError(true);
                        return "file-upload-status";
                    }
                    File image_file_check1 = new File(image_file.getImage1());
                    File image_file_check2 = new File(image_file.getImage2());
                    if (image_file_check1.exists() == false || image_file_check1.canRead() == false)
                    {
                        int error_row = iCompareCount + 1;
                        model.addAttribute("message", "An error occurred while processing the CSV file. Image1 path in row:" + error_row +" is not accessible");
                        model.addAttribute("status", false);
                        image_files.get(iCompareCount).setIsRowError(true);
                        return "file-upload-status";
                    }
                    if (image_file_check2.exists() == false || image_file_check2.canRead() == false)
                    {
                        int error_row = iCompareCount + 1;
                        model.addAttribute("message", "An error occurred while processing the CSV file. Image2 path in row:" + error_row +" is not accessible");
                        model.addAttribute("status", false);
                        image_files.get(iCompareCount).setIsRowError(true);
                        return "file-upload-status";
                    }
                    double dist_pc = imagelogic.ImageCompare(image_file.getImage1(),image_file.getImage2());
                    long estimatedTime = System.currentTimeMillis() - startTime;
                    double dbl_est_time = (double)estimatedTime/10000;
                    image_files.get(iCompareCount).setElapsed(dbl_est_time);
                    image_files.get(iCompareCount).setSimilarity(dist_pc);
                    ++iCompareCount;
                }
                // save images list on model
                model.addAttribute("image_files", image_files);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }
    @ApiOperation(value = "Download the images comparison results", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded the csv"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @GetMapping("/export-results")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "image_compare_results.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<CompareProp> writer = new StatefulBeanToCsvBuilder<CompareProp>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(image_files);

    }

    @ApiOperation(value = "Index Page", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Api call for index page"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
