package com.zzm.mapper;


import com.zzm.entity.PictureUpload;
import com.zzm.entity.PictureUpload;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface PictureUploadMapper {
    //插入数据到数据库内，目前需要把id加上，不会自动生成id，不然报错
    @Update("INSERT INTO `picture_upload`( `pictureName`, `pictureUrl`, `pictureUUID`) VALUES (#{pictureName},#{pictureUrl},#{pictureUUID});")
    //事务注解：可加可不加
    @Transactional
    //接收传过来的参数数据
    void save(@Param("pictureName") String pictureName, @Param("pictureUrl") String pictureUrl, @Param("pictureUUID") String pictureUUID);

    //查询数据库内表名为picture_upload的全部数据返回
    @Select("select * from picture_upload")
    //方法：以数组的形式返回数据库信息
    List<PictureUpload> SelectpictureAll();


    //查询数据库内表名为picture_upload的id=pictureId的那一条数据
    @Select("select * from picture_upload where id = #{pictureId}")
        //方法：以表格格式（就是数据库字段一样的格式直接返回一个对象里面包含各个字段和信息）返回
    PictureUpload SelectpictureId(Integer pictureId);

    //查询数据库内表名为picture_upload的pictureUUID=pictureUUID的那一条数据
    @Select("select * from picture_upload where pictureUUID = #{pictureUUID}")
    //方法：以表格格式（就是数据库字段一样的格式直接返回一个对象里面包含各个字段和信息）返回
    PictureUpload SelectpictureUUID(String pictureUUID);

    @Delete("delete from picture_upload where id = #{pictureId}")
    void DeletepictureId(Integer pictureId);
}


