package com.clone.instagram.authservice.endpoint;

import com.clone.instagram.authservice.model.image.Photo;
import com.clone.instagram.authservice.model.image.PhotoDTO;
import com.clone.instagram.authservice.service.PhotoService;
import com.clone.instagram.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


@RestController
@Slf4j
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/photos/add")
    public String addPhoto(@RequestParam("title") String title,
                           @RequestParam("image") MultipartFile image, Model model)
            throws IOException {
        String id = photoService.addPhoto(title, image);
        return "redirect:/photos/" + id;
    }

    @GetMapping("/photos/{id}")
    public String getPhoto(@PathVariable String id, Model model) {
        Photo photo = photoService.getPhoto(id);
        model.addAttribute("title", photo.getTitle());
        model.addAttribute("image",
                Base64.getEncoder().encodeToString(photo.getImage().getData()));
        return "photos";
    }


    @GetMapping("/photos")
    public List<PhotoDTO> getPhotos() {
        return photoService.getPhotos();
    }
}
