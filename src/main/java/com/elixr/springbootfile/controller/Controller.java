package com.elixr.springbootfile.controller;


import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;


@RestController
@AllArgsConstructor
@Validated
public class Controller {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> createFile(@RequestParam("file") MultipartFile file, ModelFile modelFile, @RequestParam(value = "userName") @NotEmpty String userName) {
        return fileService.fileUpload(file, modelFile, userName);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> getFileById(@PathVariable("fileId") String id) {
        return fileService.getFileById(id);
    }

    @GetMapping("/file/user/{userName}")
    public ResponseEntity<?> getFileByName(@PathVariable("userName") String userName) {
        return fileService.getFileByUserName(userName);
    }
}


