package com.sre.image.compare.controllers;

import com.sre.image.compare.domains.CompareProp;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sre.image.compare.logic.ImageCompareLogic;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

@Controller
public class UploadController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

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
                List<CompareProp> image_files = csvToBean.parse();

                // TODO: save users in DB?
                long startTime = System.currentTimeMillis();
                ImageCompareLogic imagelogic = new ImageCompareLogic();
                double dist_pc = imagelogic.ImageCompare(image_files.get(0).getImage1(),image_files.get(0).getImage1());
                long estimatedTime = System.currentTimeMillis() - startTime;
                image_files.get(0).setElapsed(estimatedTime);
                image_files.get(0).setSimilarity(dist_pc);
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
}
