package com.zzm.controller;

import com.zzm.entity.VideoUpload;
import com.zzm.entity.VideoUpload;
import com.zzm.mapper.VideoUploadMapper;
import com.zzm.mapper.VideoUploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/DeleteVideo")
public class DeleteVideoController {


    @Autowired
    VideoUploadMapper videoUploadMapper;

    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/{videoId}")
    public void delete(@PathVariable("videoId") Integer videoId){
        System.out.println(videoId);
        videoUploadMapper.DeletevideoId(videoId);
    }
}

