package com.filedemo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.filedemo.dto.BusDTO;
import com.filedemo.dto.ChanpinDTO;
import com.filedemo.service.FileService;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/upload")
    public String upload(){
        return "upload";
    }


    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(String search,int page,int limit){
        Map<String,Object> result = new HashMap<>();
        List<ChanpinDTO> list = fileService.list(search,(page-1)*limit,limit);
        result.put("msg","ok");
        result.put("code",200);
        result.put("data",list);
        result.put("count",20);
        return result;
    }


    @RequestMapping("/uploadImg")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file,Integer id )  {
        Map<String,Object> result = new HashMap<>();

        if (file.isEmpty()) {
            result.put("msg","上传失败！");
            result.put("code",2);
            return result;
        }
        String fileName = file.getOriginalFilename();
        Integer fileCount = fileService.getFileCount(id);
        ChanpinDTO chanpin = new ChanpinDTO();
        String fileRealName = fileName.substring(0,fileName.lastIndexOf("."));
        if(fileCount.intValue() > 0){
            fileRealName = fileRealName+fileCount;
        }
        chanpin.setChanpinName(fileRealName);
        chanpin.setBusId(id);
        chanpin.setFileName(fileName);
        File dest = null;
        String path = getUploadPath()+"/";
        dest= new File(path + fileName);
        try {
            chanpin.setFilePath(path + fileName);
            file.transferTo(dest);
            fileService.create(chanpin);
            result.put("msg","上传成功！");
            result.put("code",0);
            return result;
        } catch (IOException e) {
            result.put("msg","上传失败！");
            result.put("code",2);
            return result;
        }
    }

    @RequestMapping("add")
    @ResponseBody
    public Map<String,Object> addBus(String name){
        Map<String,Object> result = new HashMap<>();
        BusDTO bus= new BusDTO();
        bus.setBusName(name);
        fileService.addBus(bus);
        return result;
    }

    @javax.annotation.Resource
    private ResourceLoader resourceLoader;
    @RequestMapping("/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> show(@PathVariable String fileName){
        try
        {
            //resourceLoader.getResource("file:" + uploadPicturePath + fileName) 返回指定路径的资源句柄，这里返回的就是URL [file:D:/springboot/upload/test.png]
            //ResponseEntity.ok(T) 返回指定内容主体
            return ResponseEntity.ok(resourceLoader.getResource("file:" + getUploadPath()+"/" + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/deleteFile")
    @ResponseBody
    public Map<String,Object> dele(String id){
        Map<String,Object> map = new HashMap<>();
        fileService.delt(id);
        return map;
    }

    private String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) path = new File("");
        File upload = new File(path.getAbsolutePath(), "static/upload/");
        if (!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }

}
