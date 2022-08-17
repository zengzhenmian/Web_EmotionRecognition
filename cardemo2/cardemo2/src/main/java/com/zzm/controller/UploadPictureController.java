package com.zzm.controller;
import com.zzm.entity.PictureUpload;
import com.zzm.mapper.PictureUploadMapper;
import com.zzm.utils.NonStaticResourceHttpRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class UploadPictureController {

    //引入返回视频流的组件
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;



    @Autowired
    PictureUploadMapper pictureUploadMapper;

    Map<Integer,List<String>> map = new HashMap();

    public UploadPictureController(NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    @PostMapping(value = "/api/uploadPicture")
    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ResponseBody
    public Map<String,String> uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam String SavePath)
            //throws IllegalStateException写在方法的前面是可以抛出异常状态的，如果有错误会把错误信息发出来对应下面的try和catch
            throws IllegalStateException {

                System.out.println(file);
                System.out.println(file.toString());
        //new一个map集合出来
        Map<String, String> resultMap = new HashMap<>();
        try {
            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            // 重构文件名称
            System.out.println("前端传递的保存路径：" + SavePath);
            //UUID(全局唯一标识符)randomUUID(随机生成标识符)toString(转成字符串)replaceAll(替换字符方法，因为随机生成的里面包括了 - ，这里意思是把 - 全部换成空)
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            //视频名字拼接：唯一标识符加上点，再加上上面的视频后缀也就是MP4之类的。就组成了现在的视频名字，比如这样：c7bbc1f9664947a287d35dd7cdc48a95.mp4
            String newPictureName = pikId + "." + fileExt;
            System.out.println("重构文件名防止上传同名文件：" + newPictureName);
            //保存视频的原始名字
            String pictureNameText = file.getOriginalFilename();
            System.out.println("视频原名:" + pictureNameText);
            //保存视频url路径地址
            String videoUrl = SavePath + "/" + newPictureName;
            //调用数据库接口插入数据库方法save，把视频原名，视频路径，视频的唯一标识码传进去存到数据库内
            System.out.println(pictureNameText);
            System.out.println(videoUrl);
            System.out.println(newPictureName);
            pictureUploadMapper.save(pictureNameText, videoUrl, newPictureName);
            //判断SavePath这个路径也就是需要保存视频的文件夹是否存在
            File filepath = new File(SavePath, file.getOriginalFilename());
            if (!filepath.getParentFile().exists()) {
                //如果不存在，就创建一个这个路径的文件夹。
                filepath.getParentFile().mkdirs();
            }
            //保存视频：把视频按照前端传来的地址保存进去，还有视频的名字用唯一标识符显示，需要其他的名字可改这
            File fileSave = new File(SavePath, newPictureName);
            //下载视频到文件夹中
            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称：这里put代表添加进map集合内，和前端的push一样。括号内是前面字符串是键，后面是值
            resultMap.put("newPictureName", newPictureName);
            //正确保存视频成功，则设置返回码为200
            resultMap.put("resCode", "200");
            //返回视频保存路径
            resultMap.put("pictureUrl", SavePath + "/" + newPictureName);
            //到这里全部保存好了，把整个map集合返给前端
            //提前识别，获取识别结果。
            List<String> list = getRuntime(newPictureName);
            int id = pictureUploadMapper.SelectpictureUUID(newPictureName).getId();
            map.put(id,list);
            System.out.println("test"+"id="+id+"--"+map.get(id));

            String path = "C:/Users/Administrator/Desktop/convert/runs/detect/exp2";
            //File f = new File(path);
            //获取识别出来的图片名
//            File[] files = f.listFiles();
//            nameU = files[0].getName();
//            //识别出来的图片
//            Path filePath = Paths.get(path + "/" + nameU);

            copyVideo(newPictureName);
            return resultMap;

        } catch (Exception e) {
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
            //返回有关异常的详细描述性消息。
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("resCode", "400");
            //这时候错误了，map里面就添加了错误的状态码400并返回给前端看
            return resultMap;
        }
    }

    //复制并删除文件
    public void copyVideo(String newName) throws IOException {
        Process proc;
        String cmds = "python C:\\Users\\Administrator\\Desktop\\study\\py_util\\copypicture.py"+" "+newName;
        System.out.println(cmds);

        proc = Runtime.getRuntime().exec(cmds);// 执行py文件
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    //解决跨域的注解
    @CrossOrigin(origins = "*", maxAge = 3600)
    //查询视频流的接口
    @GetMapping("/SelectPicture/policemen/{pictureId}")
    //前两个参数不管，第三个参数pictureId代表前端传过来的视频的id号
    public void picturePreview(HttpServletRequest request, HttpServletResponse response,@PathVariable("pictureId") Integer pictureId) throws Exception {
        //调用查询方法，把前端传来的id传过去，查询对应的视频信息。
        PictureUpload picturePathList = pictureUploadMapper.SelectpictureId(pictureId);

        System.out.println(picturePathList.toString());
        //从视频信息中单独把视频路径信息拿出来保存
        String picturePathUrl = picturePathList.getpictureUrl();
        //保存视频磁盘路径
        //Path filePath = Paths.get(picturePathUrl );
        //获取结果。
        String newName = picturePathList.getpictureUUID();
        //图片文件夹的目录
//        String path = "C:/Users/Administrator/Desktop/convert/runs/detect/exp2";
//        File f = new File(path);
//        //获取识别出来的图片名
//        File[] files = f.listFiles();
//        String name = files[0].getName();
//        //识别出来的图片
//        Path filePath = Paths.get(path + "/" + name);
        Path filePath = Paths.get("C:\\Users\\Administrator\\Desktop\\study\\pictureresult\\" + newName);
        System.out.println(filePath);
        //Files.exists：用来测试路径文件是否存在
        if (Files.exists(filePath)) {
            //获取视频的类型，比如是MP4这样
            String mimeType = Files.probeContentType(filePath);
            System.out.println(mimeType);
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
    @GetMapping("/SelectPicture/TestPicture/{pictureId}")
    //前两个参数不管，第三个参数Id代表前端传过来的视频的id号
    public List<String> testPicture(HttpServletRequest request, HttpServletResponse response, @PathVariable("pictureId") Integer pictureId) throws Exception {
        System.out.println("111");
        System.out.println("test"+pictureId+"--"+map.get(pictureId));
        return map.get(pictureId);

    }

    //运行python脚本
    public List<String> getRuntime(String imgName){
        List<String> list = new ArrayList<>();
        Process proc;
        try {
            String cmds = String.format("python C:\\Users\\Administrator\\Desktop\\convert\\picturemain2.py %s",
                    imgName);
            System.out.println("cmds:"+cmds);

            proc = Runtime.getRuntime().exec(cmds,null,new File("C:\\Users\\Administrator\\Desktop\\convert"));// 执行py文件


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

    //查询图片表格列表的接口
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/SelectPicture/table")
    public List<PictureUpload> pictureTable() {
        //调用搜索方法查询所有视频信息，成表格展示前端
        System.out.println("table");
        pictureUploadMapper.SelectpictureAll();
        return pictureUploadMapper.SelectpictureAll();
    }




}
