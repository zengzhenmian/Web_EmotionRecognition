package com.zzm.controller;

import com.zzm.mapper.VideoUploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

//接口：前端视频上传
@RestController
//一级地址
@RequestMapping("/api")
public class    uploadVideoController {
    @Autowired
    VideoUploadMapper videoUploadMapper;
    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    //二级地址
    @PostMapping(value = "/uploadVideo3")
    @ResponseBody
    //post请求传入的参数：MultipartFile file(理解为springmvc框架给我们提供的工具类，代表视频流数据)，SavePath(前台传来的地址路径，也是用来后端保存在服务器哪个文件夹的地址)
    public Map<String,String> savaVideoTest(@RequestParam("file") MultipartFile file,@RequestParam String SavePath)
    //throws IllegalStateException写在方法的前面是可以抛出异常状态的，如果有错误会把错误信息发出来对应下面的try和catch
            throws IllegalStateException {
        //new一个map集合出来
        Map<String,String> resultMap = new HashMap<>();
        try{
            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            // 重构文件名称
            System.out.println("前端传递的保存路径："+SavePath);
            //UUID(全局唯一标识符)randomUUID(随机生成标识符)toString(转成字符串)replaceAll(替换字符方法，因为随机生成的里面包括了 - ，这里意思是把 - 全部换成空)
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            //视频名字拼接：唯一标识符加上点，再加上上面的视频后缀也就是MP4之类的。就组成了现在的视频名字，比如这样：c7bbc1f9664947a287d35dd7cdc48a95.mp4
            String newVideoName = pikId + "." + fileExt;
            System.out.println("重构文件名防止上传同名文件："+newVideoName);
            //保存视频的原始名字
            String videoNameText = file.getOriginalFilename();
            System.out.println("视频原名:"+videoNameText);
            //保存视频url路径地址
            String videoUrl = SavePath + "/" + newVideoName;
            //调用数据库接口插入数据库方法save，把视频原名，视频路径，视频的唯一标识码传进去存到数据库内
            System.out.println(videoNameText);
            System.out.println(videoUrl);
            System.out.println(newVideoName);
            videoUploadMapper.save(videoNameText,videoUrl,newVideoName);
            //判断SavePath这个路径也就是需要保存视频的文件夹是否存在
            File filepath = new File(SavePath, file.getOriginalFilename());
            if (!filepath.getParentFile().exists()) {
                //如果不存在，就创建一个这个路径的文件夹。
                filepath.getParentFile().mkdirs();
            }
            //保存视频：把视频按照前端传来的地址保存进去，还有视频的名字用唯一标识符显示，需要其他的名字可改这
            File fileSave = new File(SavePath, newVideoName);
            //下载视频到文件夹中
            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称：这里put代表添加进map集合内，和前端的push一样。括号内是前面字符串是键，后面是值
            resultMap.put("newVideoName",newVideoName);
            //正确保存视频成功，则设置返回码为200
            resultMap.put("resCode","200");
            //返回视频保存路径
            resultMap.put("VideoUrl",SavePath + "/" + newVideoName);
            //到这里全部保存好了，把整个map集合返给前端
            getRuntime(videoUrl);
            copyVideo(newVideoName);
            return  resultMap;
        }catch (Exception e){
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
            //返回有关异常的详细描述性消息。
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("resCode","400");
            //这时候错误了，map里面就添加了错误的状态码400并返回给前端看
            return  resultMap ;
        }
    }

    public void getRuntime(String imgUrl){
        Process proc;
        try {
            String cmds = "python detect.py --weights runs/train/exp/weights/last.pt --source"+" "+
                    imgUrl;
            System.out.println("cmds:"+cmds);

            proc = Runtime.getRuntime().exec(cmds,null,new File("C:\\Users\\Administrator\\Desktop\\convert\\yolov5"));// 执行py文件

            /* //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            in.close();

            //输入错误信息
            BufferedReader inErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String lineErr = null;
            while ((lineErr = inErr.readLine()) != null) {
                System.out.println(lineErr);
            }
            inErr.close();
            proc.waitFor();*/

            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            // kick off stderr
            errorGobbler.start();

            StreamGobbler outGobbler = new StreamGobbler(proc.getInputStream(), "STDOUT");
            // kick off stdout
            outGobbler.start();

            proc.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //复制并删除文件
    public void copyVideo(String newName) throws IOException {
        Process proc;


        proc = Runtime.getRuntime().exec("python C:\\Users\\Administrator\\Desktop\\study\\py_util\\copyvideo.py"+" "+newName);// 执行py文件
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class StreamGobbler extends Thread {
        InputStream is;
        String type;
        OutputStream os;

        StreamGobbler(InputStream is, String type) {
            this(is, type, null);
        }

        StreamGobbler(InputStream is, String type, OutputStream redirect) {
            this.is = is;
            this.type = type;
            this.os = redirect;
        }

        public void run() {
            InputStreamReader isr = null;
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                if (os != null)
                    pw = new PrintWriter(os);

                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (pw != null)
                        pw.println(line);
                    System.out.println(type + ">" + line);
                }

                if (pw != null)
                    pw.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }



}


