package com.elixr.springbootfile.service;

import com.elixr.springbootfile.constants.Constants;
import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.repository.FileRepository;
import com.elixr.springbootfile.response.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class FileServiceImplTest {

    @Autowired
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Test
    public void fileUpload() {
        ModelFile modelFile = new ModelFile();
        MockMultipartFile sampleFile = new MockMultipartFile("file",
                "some.txt",
                "text/plain",
                "This is the file content".getBytes());
        when(fileRepository.save(any(ModelFile.class))).thenReturn(modelFile);
        Optional<?> savedData = Optional.ofNullable(fileService.fileUpload(sampleFile, modelFile, "Shiva"));
        assertThat(savedData).isNotNull();
    }

    @Test
    void getFileById() {
        ModelFile modelFile = new ModelFile();
        modelFile.setFileName("some.txt");
        modelFile.setUserName("bikas");
        when(fileRepository.findById(UUID.fromString(String.valueOf(UUID.fromString(String.valueOf(modelFile.getId())))))).thenReturn(Optional.of(modelFile));
        ResponseEntity<SuccessResponse> foundFile = fileService.getFileById("04c3cae6-c213-48c1-a640-14837d0ed659");
        assertNotNull(foundFile);
        assertEquals(HttpStatus.OK, foundFile.getStatusCode());
        assertEquals(Constants.STATUS, Objects.requireNonNull(foundFile.getBody()).getStatus());
        assertEquals(modelFile.getUserName(), foundFile.getBody().getUserName());
        assertEquals(modelFile.getFileName(), foundFile.getBody().getFileName());
    }

    @Test
    void getFileByUserName() {
        ModelFile modelFile = new ModelFile();
        modelFile.setFileName("name.txt");
        modelFile.setUserName("bikas");
        when(fileRepository.findByUserName(modelFile.getUserName())).thenReturn(Optional.of(modelFile));
        ResponseEntity<SuccessResponse> foundFile = fileService.getFileByUserName("bikas");
        assertNotNull(foundFile);
        assertEquals(HttpStatus.OK, foundFile.getStatusCode());
        assertEquals(Constants.STATUS, Objects.requireNonNull(foundFile.getBody()).getStatus());
        assertEquals(modelFile.getUserName(), Objects.requireNonNull(foundFile.getBody()).getUserName());
    }
}
