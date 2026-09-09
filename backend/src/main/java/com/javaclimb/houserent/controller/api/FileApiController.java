package com.javaclimb.houserent.controller.api;

import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.util.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/** 上传接口：文件存入由 Docker 挂载的 uploads 目录，并返回可直接使用的 URL。 */
@RestController
@RequestMapping("/api/v1/files")
public class FileApiController extends ApiControllerSupport {
    @PostMapping
    public JsonResult upload(@RequestParam("file") MultipartFile file) {
        if (!loginUserIsAdmin()) return adminRequired();
        if (file == null || file.isEmpty()) return JsonResult.error("请选择要上传的文件");
        try {
            Map<String, String> uploaded = FileUtil.upload(file);
            return JsonResult.success("上传成功", uploaded);
        } catch (Exception e) {
            return JsonResult.error("上传失败");
        }
    }
}
