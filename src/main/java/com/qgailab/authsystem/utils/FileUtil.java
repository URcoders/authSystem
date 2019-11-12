package com.qgailab.authsystem.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author linxu
 * @version 1.0
 * @date 2018/11/16
 * a series operation methods of file
 */
public class FileUtil {
    /**
     * the real image which is suffixed with jpg、jpeg、png、bmp etc...
     * when the file is real image, return T ,else return false;
     *
     * @param file file
     * @return boolean
     * @since jdk 1.8
     */
    public static boolean isImage(File file) {
        if (file == null) {
            throw new NullPointerException("the file is null");
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image != null;
    }

    /**
     * for multipartFile
     * @param headPrefix mimeType
     * @return b
     */
    public static boolean isImage(String headPrefix) {
        if (headPrefix == null) {
            throw new NullPointerException("the header is null");
        }
        return headPrefix.startsWith("image/");
    }

    /**
     *
     * @return 随机号码
     */
    public static String getUuid(){
        return UUID.randomUUID().toString();
    }

    /**
     * 保存文件到 classpath:path下的目录
     * @param file 文件
     * @param path 路径
     */
    public static void saveFile(MultipartFile file, String path){
        if (file==null){
            throw new NullPointerException("the file is null");

        }
        try {
            File f=new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX+path).toString().substring(6)+getUuid()+".jpg");
            if (f.createNewFile()){
                file.transferTo(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
