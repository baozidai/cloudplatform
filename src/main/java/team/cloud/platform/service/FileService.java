package team.cloud.platform.service;

import net.lingala.zip4j.exception.ZipException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public interface FileService {

    /**
     * 解压项目文件
     *
     * @param multipartFile 项目文件
     * @param path1 解压路径1
     * @param path2 解压路径2
     * @return 文件名
     * @throws IOException 文件io异常
     * @throws ZipException 解压异常
     */
    String unzipProjectFile(MultipartFile multipartFile, String path1, String path2) throws IOException,ZipException;

    /**
     * 删除文件
     *
     * @param file 文件
     * @return 是否上传成功
     */
    boolean deleteFile(File file);
}
