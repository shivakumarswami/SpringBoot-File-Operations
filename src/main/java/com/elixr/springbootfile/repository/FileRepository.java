package com.elixr.springbootfile.repository;

import com.elixr.springbootfile.dao.ModelFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends MongoRepository<ModelFile, UUID> {
    Optional<ModelFile> findByUserName(String userName);
}
