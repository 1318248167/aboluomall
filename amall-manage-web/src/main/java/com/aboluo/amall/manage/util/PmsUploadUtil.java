package com.aboluo.amall.manage.util;

import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile){
        String imgUrl="http://192.168.213.132";
        try {
            ClientGlobal.initByProperties("fdfs-client.properties");
            // 链接FastDFS服务器，创建tracker和Stroage
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient storageClient = new StorageClient(trackerServer,storageServer);

            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            //上传文件,只需要文件字节数组，第二个参数是文件扩展名，第三个参数是元数据
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String[] jpgs = storageClient.upload_file(bytes, extName, null);

            for (String jpg : jpgs) {
                imgUrl += "/"+jpg;
            }
            System.out.println(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
