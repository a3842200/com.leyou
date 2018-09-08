package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enumeration.ExceptionEnumeration;
import com.leyou.common.exception.LyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 45207
 * @create 2018-08-18 23:17
 */
@Service
public class UploadService {

    @Value("${ly.upload.baseUrl}")
    public String baseUrl;

    @Autowired
    private FastFileStorageClient storageClient;

    private static final List<String> ALLOW_FILE_TYPES = Arrays.asList("image/jpeg", "image/png");

    public String uploadImage(MultipartFile file) {
        try {
            //校验文件类型
            if (!ALLOW_FILE_TYPES.contains(file.getContentType())) {
                throw new LyException(ExceptionEnumeration.FILE_TYPE_ERROR);
            }

            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnumeration.FILE_CONTENT_ERROR);
            }

           /* //准备一个文件夹
            File desDir = new File("C:\\develop\\nginx-1.12.2\\html");
            if (!desDir.exists()) {
                desDir.mkdirs();
            }
            //保存
            file.transferTo(new File(desDir, file.getOriginalFilename()));*/

           //上传到FastDFS
            String fileExtName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);


            //构建URl地址
            String url = baseUrl + storePath.getFullPath();
            return url;
        } catch (IOException e) {
            throw new LyException(ExceptionEnumeration.FILE_UPLOAD_ERROR);
        }


    }
}
