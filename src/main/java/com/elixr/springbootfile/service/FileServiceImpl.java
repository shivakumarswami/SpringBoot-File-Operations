package com.elixr.springbootfile.service;

import com.elixr.springbootfile.constants.Constants;
import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.exceptionhandling.InternalServerException;
import com.elixr.springbootfile.exceptionhandling.NotFoundException;
import com.elixr.springbootfile.repository.FileRepository;
import com.elixr.springbootfile.response.FileInfoDTO;
import com.elixr.springbootfile.response.ResponseGetId;
import com.elixr.springbootfile.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepo;


    @Value(Constants.LOCAL_STORAGE_FOLDER_PATH)
    private String UPLOAD_DIR;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Path.of(UPLOAD_DIR));
        } catch (Exception ex) {
            throw new NotFoundException(Constants.ERROR_CREATING_FOLDER_PATH);
        }
    }

    @Override
    public ResponseEntity<?> fileUpload(MultipartFile file, ModelFile modelFile, String userName) {
        if (Objects.equals(file.getContentType(), "text/plain")) {
            try {
                modelFile.setFileName(file.getOriginalFilename());
                modelFile.setUserName(userName);
                UUID id = fileRepo.save(modelFile).getId();
                Path resolvedPath = Path.of(UPLOAD_DIR).resolve(String.valueOf(id));
                Files.copy(file.getInputStream(), resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                return new ResponseEntity<>(SuccessResponse.builder().success(Constants.SUCCESS).id(String.valueOf(modelFile.getId())).build(), HttpStatus.OK);
            } catch (Exception e) {
                throw new InternalServerException(Constants.FILE_PATH_NOT_VALID);
            }
        } else {
            throw new NotFoundException(Constants.FILE_NOT_SUPPORTED);
        }
    }

    @Override
    public ResponseEntity<ResponseGetId> getFileByUserId(String id) {
        ModelFile getFile = fileRepo.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException(Constants.ID_NOT_FOUND));
        String fileName= getFile.getFileName();
        File file = new File(UPLOAD_DIR + id);

        if (file.exists()) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + id)));
                return new ResponseEntity<>(ResponseGetId.builder().status(Constants.SUCCESS)
                        .data(SuccessResponse.builder().userName(getFile.getUserName()).uploadTime(getFile.getDateAndTime())
                                .fileName(fileName).content(data).build()).build(), HttpStatus.OK);
            } catch (Exception e) {
                throw new NotFoundException(Constants.FILE_IS_UNREADABLE);
            }
        } else {
            throw new NotFoundException(Constants.FILE_NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<SuccessResponse> getFileByUserName(String userName) {
        fileRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException(Constants.USERNAME_NOT_FOUND));
        return new ResponseEntity<>(SuccessResponse.builder().status(Constants.SUCCESS).userName(userName).files(fileRepo
                .findByUserName(userName)
                .stream()
                .map(this::convertDataIntoDTO)
                .collect(Collectors.toList())).build(), HttpStatus.OK);
    }

    private FileInfoDTO convertDataIntoDTO(ModelFile modelFile) {
        File getFile = new File(UPLOAD_DIR + (modelFile.getId()));
        if (getFile.exists()) {
            return FileInfoDTO.builder().id(modelFile.getId())
                    .fileName(modelFile.getFileName())
                    .dateAndTime(modelFile.getDateAndTime()).isFilePresent(Constants.TRUE)
                    .build();
        } else {
            return FileInfoDTO.builder().id(modelFile.getId())
                    .fileName(modelFile.getFileName())
                    .dateAndTime(modelFile.getDateAndTime()).isFilePresent(Constants.FALSE)
                    .build();
        }
    }
}
