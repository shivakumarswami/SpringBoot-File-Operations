package com.elixr.springbootfile.service;

import com.elixr.springbootfile.dao.ModelFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseEntity<?> fileUpload(MultipartFile file, ModelFile modelfile, String userName);

    ResponseEntity<?> getFileById(String id);

    ResponseEntity<?> getFileByUserName(String userName);
}
