package com.tinckay.domain.controller;

import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ErrorHandlerController;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.vo.FileInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by root on 2/13/17.
 */
@Controller
@RequestMapping("/capi/Resource")
public class ResourceController extends ErrorHandlerController {
    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.POST,value = "/fileUpload")
    @ResponseBody
    public ResponseObj fileUpload(@RequestParam("file")MultipartFile multipartFile){
        if(!multipartFile.isEmpty()){
            String name = multipartFile.getOriginalFilename();
            //String name = multipartFile.getName();
            try{
                String strHomePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
                //String strHomePath = System.getProperty("user.home");
                String strFilePath = environment.getRequiredProperty("file.upload.path");


                if(null != strHomePath && !"".equals(strHomePath)){
                    if("/".equals(strHomePath.substring(strHomePath.length() - 1))){
                        strFilePath = strHomePath + strFilePath.substring(1);
                    }else {
                        strFilePath = strHomePath + strFilePath;
                    }
                }else{
                    return new ResponseObj(-1,"无法获取项目所在目录，操作失败。",null);
                }

                if(!BeanRefUtil.dirExistsOrCreate(strFilePath)){

                    return new ResponseObj(-1,"【" + name + "】上传失败，目录操作失败。",null);
                }
                byte[] bytes = multipartFile.getBytes();
                int size = bytes.length;
                String fileName = name;
                fileName = BeanRefUtil.getUUID() + fileName;
                String strFile = strFilePath + fileName;
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(strFile)));
                stream.write(bytes);
                stream.close();


                int pos = fileName.lastIndexOf(".");
                String type = (pos <= 0) ? "未知类型" : fileName.substring(pos + 1);
                //String type = multipartFile.getContentType();

                FileInfoVo fileInfoVo = new FileInfoVo();
                fileInfoVo.setName(fileName);
                fileInfoVo.setSize(size);
                fileInfoVo.setPath(strFilePath);
                fileInfoVo.setType((type.length() >= 10) ? "未知类型" : type);
                return new ResponseObj(0,"文件【" + name + "】上传成功。",fileInfoVo);

            }catch (Exception e){
                return new ResponseObj(-1,"文件【" + name + "】上传失败：" + e.getMessage(),null);
            }

        }else{
            return new ResponseObj(-1,"上传的文件内容为空，操作失败",null);
        }
        //byte[] bytes = new byte[512 << 10];
        //InputStream
        //String strHomePath = System.getProperty("user.home");
        //String strFilePath = environment.getProperty("file.uplaod.path");

    }


}
