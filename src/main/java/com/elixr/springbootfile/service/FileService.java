package com.elixr.springbootfile.service;

import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseEntity<?> fileUpload(MultipartFile file, ModelFile modelfile, String userName);

    ResponseEntity<SuccessResponse> getFileById(String id);

    ResponseEntity<SuccessResponse> getFileByUserName(String userName);
}
