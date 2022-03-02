package com.elixr.springbootfile.service;

import com.elixr.springbootfile.constants.Constants;
import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.exceptionhandling.NotFoundException;
import com.elixr.springbootfile.repository.FileRepository;
import com.elixr.springbootfile.response.FileInfoDTO;
import com.elixr.springbootfile.response.SuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepo;

    @Override
    public ResponseEntity<?> fileUpload(MultipartFile file, ModelFile modelFile, String userName) {
        if (Objects.equals(file.getContentType(), "text/plain")) {
            try {
                modelFile.setFileName(file.getOriginalFilename());
                modelFile.setUserName(userName);
                Files.copy(file.getInputStream(), Paths.get(Constants.UPLOAD_DIR + File.separator, file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                fileRepo.save(modelFile);
                return new ResponseEntity<>(SuccessResponse.builder().success(Constants.STATUS).id(String.valueOf(modelFile.getId())).build(), HttpStatus.OK);
            } catch (Exception e) {
                throw new NotFoundException(Constants.FILE_PATH_NOT_VALID);
            }
        } else {
            throw new NotFoundException(Constants.FILE_NOT_SUPPORTED);
        }
    }

    @Override
    public ResponseEntity<?> getFileById(String id) {
        ModelFile getFile = fileRepo.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException(Constants.ID_NOT_FOUND));
        String fileName = getFile.getFileName();
        File file = new File(Constants.UPLOAD_DIR + fileName);

        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(Constants.UPLOAD_DIR + fileName)));
                return new ResponseEntity<>(SuccessResponse.builder().status(Constants.STATUS).userName(getFile.getUserName()).uploadTime(LocalDateTime.now()).fileName(fileName).content(content).build(), HttpStatus.OK);
            } catch (Exception e) {
                throw new NotFoundException(Constants.FILE_IS_UNREADABLE);
            }
        } else {
            throw new NotFoundException(Constants.FILE_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getFileByUserName(String userName) {
        Optional<ModelFile> getFile = Optional.ofNullable(fileRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException(Constants.USERNAME_NOT_FOUND)));
        String fileName = getFile.get().getFileName();
        File file = new File(Constants.UPLOAD_DIR + fileName);
        if (file.exists()) {
            return new ResponseEntity<>(SuccessResponse.builder().status(Constants.STATUS).userName(userName).files(fileRepo
                    .findByUserName(userName)
                    .stream()
                    .map(this::convertDataIntoDTO)
                    .collect(Collectors.toList())).build(), HttpStatus.OK);
        } else {
            throw new NotFoundException(Constants.FILE_NOT_FOUND);
        }
    }

    private FileInfoDTO convertDataIntoDTO(ModelFile file) {
        FileInfoDTO requiredFileDetailsDto = FileInfoDTO.builder().build();
        requiredFileDetailsDto.setId(UUID.fromString(String.valueOf(file.getId())));
        requiredFileDetailsDto.setFileName(file.getFileName());
        requiredFileDetailsDto.setDateAndTime(file.getDateAndTime());
        return requiredFileDetailsDto;
    }
}
