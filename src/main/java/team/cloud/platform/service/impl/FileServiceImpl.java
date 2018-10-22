package team.cloud.platform.service.impl;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.cloud.platform.service.FileService;

import java.io.File;
import java.io.IOException;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * 解压项目文件
     *
     * @param multipartFile 项目文件
     * @param path1 解压路径1
     * @param path2 解压路径2
     * @return path
     * @throws IOException 文件io异常
     * @throws ZipException 解压异常
     */
    @Override
    public String unzipProjectFile(MultipartFile multipartFile, String path1, String path2) throws IOException, ZipException {
        //项目压缩包完整路径
        String projectZipPath = path2 + "/" + multipartFile.getOriginalFilename();
        Runtime.getRuntime().exec("chmod 777 -R " + path1);
        File appZip = new File(projectZipPath);
        if (!appZip.getParentFile().exists()) {
            appZip.getParentFile().mkdirs();
        }
        if(!path1.equals(path2)){
            multipartFile.transferTo(appZip);
        }
        //开始解压
        ZipFile zipFile = new ZipFile(appZip);
        //用自带的方法检测一下zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等
        if (!zipFile.isValidZipFile()) {
            throw new ZipException("无效的压缩包!");
        }
        zipFile.extractAll(path1);
        File file = new File(path1);
        String fileName = null;
        if(!path1.equals(path2)){
            File[] files = file.listFiles();
            assert files != null;
            Integer length = files.length;
            if(length==1){
                fileName = files[0].getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String war = "war";
                if(suffix.equals(war)){
                    deleteFile(file);
                    unzipProjectFile(multipartFile,path2,path2);
                    return path2+ "/" +fileName;
                }else{
                    //解压完毕删除压缩包
                    if (!appZip.delete()){
                        throw new IOException("压缩包删除失败");
                    }
                    return path1;
                }
            }else{
                //解压完毕删除压缩包
                if (!appZip.delete()){
                    throw new IOException("压缩包删除失败");
                }
                return path1;
            }
        }else{
            //解压完毕删除压缩包
            if (!appZip.delete()){
                throw new IOException("压缩包删除失败");
            }
            return path2+ "/" +fileName;
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return 是否上传成功
     */
    @Override
    public boolean deleteFile(File file) {
        //文件为空直接返回true
        if (null == file){
            return true;
        }
        //如果有文件，直接删除
        else if (file.isFile()){
            return file.delete();
        }
        //如果是文件夹，遍历递归删除,如果遇到删除失败的文件直接返回false
        //否则如果它是一个目录
        else if (file.isDirectory()) {
            //声明目录下所有的文件 files[];
            File[] files = file.listFiles();
            //遍历目录下所有的文件
            int i = 0;
            assert files != null;
            while (i < files.length) {
                //把每个文件用这个方法进行迭代
                this.deleteFile(files[i]);
                i++;
            }
            //删除文件夹
            return file.delete();
        }else{
            //其他情况返回false
            return false;
        }
    }
}
