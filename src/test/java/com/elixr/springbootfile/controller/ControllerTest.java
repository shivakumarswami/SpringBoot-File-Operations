package com.elixr.springbootfile.controller;

import com.elixr.springbootfile.service.FileService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @MockBean
    private FileService fileService;

    @Autowired
    private Controller controller;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createFile() throws Exception {
        MockMultipartFile sampleFile = new MockMultipartFile("file",
                "some.txt",
                "text/plain",
                "This is the file content".getBytes());
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/upload");
        mockMvc.perform(multipartRequest.file(sampleFile).param("userName", "bikas"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadFileNoFileProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getFileById() throws Exception {
        String id = "da2fdbec-3354-475d-8a9e-8d0a30e0ac87";
        when(fileService.getFileByUserId(id))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.get("/file/fileId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void errorGetFileById() throws Exception {
        when(fileService.getFileByUserId(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/file/fileId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getFileByName() throws Exception {
        String userName = "shiva";
        when(fileService.getFileByUserName(userName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.get("/file/user/username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    }

    @Test
    public void errorGetFileByName() throws Exception {
        when(fileService.getFileByUserName(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/file/user/userName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }
}
