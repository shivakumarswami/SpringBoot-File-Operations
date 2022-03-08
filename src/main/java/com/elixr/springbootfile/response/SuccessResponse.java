package com.elixr.springbootfile.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {
    private String status;
    private String id;
    private String success;
    private String userName;
    private LocalDateTime uploadTime;
    private String fileName;
    private String content;
    private List<FileInfoDTO> files;
}
