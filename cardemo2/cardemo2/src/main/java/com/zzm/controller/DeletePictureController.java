package com.zzm.controller;

import com.zzm.entity.PictureUpload;
import com.zzm.mapper.PictureUploadMapper;
import com.zzm.utils.NonStaticResourceHttpRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/DeletePicture")
public class DeletePictureController {
    @Autowired
    PictureUploadMapper pictureUploadMapper;

    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/{pictureId}")
    public List<PictureUpload> delete(@PathVariable("pictureId") Integer pictureId){
        System.out.println(pictureId);
        pictureUploadMapper.DeletepictureId(pictureId);
        return pictureUploadMapper.SelectpictureAll();
    }
}
