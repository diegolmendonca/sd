package com.clone.instagram.authservice.repository;

import com.clone.instagram.authservice.model.image.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo,String> {
}
