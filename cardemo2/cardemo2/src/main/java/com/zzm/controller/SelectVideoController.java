package com.zzm.controller;

import com.zzm.entity.VideoUpload;
import com.zzm.mapper.VideoUploadMapper;
import com.zzm.utils.NonStaticResourceHttpRequestHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//前端获取后端视频流
@RestController
@AllArgsConstructor
public class SelectVideoController {
    //引入返回视频流的组件
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    List<String> list = new ArrayList<>();
    //把后端链接数据库接口引入进来
    @Autowired
    VideoUploadMapper videoUploadMapper;
    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    //查询视频流的接口
    @GetMapping("/SelectVideo/policemen/{videoId}")
    //前两个参数不管，第三个参数videoId代表前端传过来的视频的id号
    public void selectvideo(HttpServletRequest request, HttpServletResponse response,@PathVariable("videoId") Integer videoId) throws Exception {

        //调用查询方法，把前端传来的id传过去，查询对应的视频信息。
        VideoUpload videoPathList = videoUploadMapper.SelectVideoId(videoId);
        System.out.println(videoPathList.toString());
        //从视频信息中单独把视频路径信息拿出来保存
//        String videoPathUrl=videoPathList.getVideoUrl();
//        //保存视频磁盘路径
//        Path filePath = Paths.get(videoPathUrl );
//        System.out.println(filePath);

        //获取newName。
        String newName = videoPathList.getVideoUUID();
        String videoName = videoPathList.getVideoName();
        //list = getRuntime(newName);

        //识别结果的url
        Path filePath = Paths.get("C:\\Users\\Administrator\\Desktop\\study\\videoresult\\"+newName);

        System.out.println(filePath.toString());

        //Files.exists：用来测试路径文件是否存在
        if (Files.exists(filePath)) {
            //获取视频的类型，比如是MP4这样
            String mimeType = Files.probeContentType(filePath);
            if (StringUtils.hasText(mimeType)) {
                //判断类型，根据不同的类型文件来处理对应的数据
                response.setContentType(mimeType);
            }
            //转换视频流部分
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }


    }

    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/SelectVideo/TestVideo/{videoId}")
    //前两个参数不管，第三个参数Id代表前端传过来的视频的id号
    public List<String> testvideo(HttpServletRequest request, HttpServletResponse response, @PathVariable("videoId") Integer videoId) throws Exception {
        return list;

    }


    //查询视频表格列表的接口
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/SelectVideo/table")
    public List<VideoUpload> videoTable() {
        //调用搜索方法查询所有视频信息，成表格展示前端
        System.out.println("table");
        return videoUploadMapper.SelectVideoAll();
    }

   /* public List<String> getRuntime(String imgName){
        Process proc;
        List<String> list = new ArrayList<>();
        try {
            String cmds = String.format("python C:\\Users\\Administrator\\Desktop\\convert\\videomain2.py %s",
                    imgName);
            System.out.println("cmds:"+cmds);

            proc = Runtime.getRuntime().exec(cmds,null,new File("C:\\Users\\Administrator\\Desktop\\convert"));// 执行py文件
            proc.waitFor();

            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }*/

}

