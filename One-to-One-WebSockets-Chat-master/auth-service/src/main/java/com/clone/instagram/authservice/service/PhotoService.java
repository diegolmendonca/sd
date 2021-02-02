package com.clone.instagram.authservice.service;

import com.clone.instagram.authservice.model.image.Photo;
import com.clone.instagram.authservice.model.image.PhotoDTO;
import com.clone.instagram.authservice.repository.PhotoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    public String addPhoto(String title, MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepo.insert(photo);
        return photo.getId();
    }

    public Photo getPhoto(String id) {
        return photoRepo.findById(id).get();
    }

    public List<PhotoDTO> getPhotos() {
        List<Photo> photos =  photoRepo.findAll();

       return photos.stream()
               .map(x->
                       new PhotoDTO(x.getId(),
                               x.getTitle(),
                               Base64.getEncoder().encodeToString( x.getImage().getData())))
               .collect(Collectors.toList());
    }
}
