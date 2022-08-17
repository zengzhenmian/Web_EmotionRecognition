package com.zzm.controller;

import com.zzm.entity.PictureUpload;
import com.zzm.mapper.PictureUploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/TestPicture")
public class TestPictureController {
    @Autowired
    PictureUploadMapper pictureUploadMapper;



   /* //删除文件夹
    public void deletePic() throws IOException {
        Process proc;
        proc = Runtime.getRuntime().exec("python C:\\Users\\Administrator\\Desktop\\study\\py_util\\main.py");// 执行py文件
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }*/



    /*public List<String> getRuntime(String imgName){
        Process proc;
        List<String> list = new ArrayList<>();
        try {
            String cmds = String.format("python C:\\Users\\Administrator\\Desktop\\convert\\main.py %s",
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
    }
*/
    }
