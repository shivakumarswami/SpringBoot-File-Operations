package com.elixr.springbootfile.dao;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Component
@Document(collection = "files")
public class ModelFile {

    @MongoId(FieldType.STRING)
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String userName;

    private LocalDateTime dateAndTime = LocalDateTime.now();

}


