package com.clone.instagram.authservice.model.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PhotoDTO {
    private String id;
    private String title;
    private String image;
}
