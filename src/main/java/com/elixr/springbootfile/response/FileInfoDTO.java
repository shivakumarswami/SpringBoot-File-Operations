package com.elixr.springbootfile.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FileInfoDTO {
    private String fileName;
    private LocalDateTime dateAndTime;
    private UUID id;
}
