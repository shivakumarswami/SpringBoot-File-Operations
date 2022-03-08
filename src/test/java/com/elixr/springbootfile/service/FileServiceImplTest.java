package com.elixr.springbootfile.service;

import com.elixr.springbootfile.constants.Constants;
import com.elixr.springbootfile.dao.ModelFile;
import com.elixr.springbootfile.exceptionhandling.NotFoundException;
import com.elixr.springbootfile.repository.FileRepository;
import com.elixr.springbootfile.response.ResponseGetId;
import com.elixr.springbootfile.response.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(FileServiceImpl.class)
@RunWith(MockitoJUnitRunner.class)
class FileServiceImplTest {

    private final String id = "34d06b3d-e15a-4053-8e82-5acf7cbaecfb";
    private final String userName = "Shiva";
    private final String fileName = "Some.txt";

    @Autowired
    private FileService fileService;

    @MockBean
    private FileRepository fileRepository;

    @Test
    public void fileUpload() {
        ModelFile modelFile = new ModelFile();
        MockMultipartFile sampleFile = new MockMultipartFile("file",
                fileName,
                "text/plain",
                "This is the file content".getBytes());
        when(fileRepository.save(any(ModelFile.class))).thenReturn(modelFile);
        Optional<?> savedData = Optional.ofNullable(fileService.fileUpload(sampleFile, modelFile, userName));
        assertThat(savedData).isNotNull();
    }

    @Test
    public void getFileById() {
        ModelFile modelFile = new ModelFile();
        modelFile.setUserName(userName);
        modelFile.setFileName(id);
        modelFile.setId(UUID.fromString((id)));
        Mockito.when(fileRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(modelFile));
        ResponseEntity<ResponseGetId> found = fileService
                .getFileByUserId(id);
        assertNotNull(found);
        assertEquals(HttpStatus.OK, found.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(found.getBody()).getStatus());
        assertEquals(modelFile.getUserName(), found.getBody().getData().getUserName());
        assertEquals(modelFile.getFileName(), found.getBody().getData().getFileName());
    }

    @Test
    public void errorGet_FileById() {
        when(fileRepository.findById(UUID.fromString(id))).thenThrow(new NotFoundException(Constants.FILE_NOT_FOUND));
        try {
            fileService.getFileByUserId(id);
            fail();
        } catch (NotFoundException exception) {
            assertTrue(true);
        }
    }

    @Test
    void getFileByUserName() {
        ModelFile modelFile = new ModelFile();
        modelFile.setFileName(fileName);
        modelFile.setUserName(userName);
        when(fileRepository.findByUserName(modelFile.getUserName())).thenReturn(Optional.of(modelFile));
        ResponseEntity<SuccessResponse> foundFile = fileService.getFileByUserName(userName);
        assertNotNull(foundFile);
        assertEquals(HttpStatus.OK, foundFile.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(foundFile.getBody()).getStatus());
        assertEquals(modelFile.getUserName(), Objects.requireNonNull(foundFile.getBody()).getUserName());
    }

    @Test
    public void errorGet_FileByUserName() {
        when(fileRepository.findByUserName(userName)).thenThrow(new NotFoundException(Constants.FILE_NOT_FOUND));
        try {
            fileService.getFileByUserName(userName);
            fail();
        } catch (NotFoundException exception) {
            assertTrue(true);
        }
    }
}
