package team.cloud.platform.service.impl;

import org.springframework.stereotype.Service;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;
import team.cloud.platform.service.NetService;
import team.cloud.platform.utils.k8s.K8sApi;

import java.io.File;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@Service
public class NetServiceImpl implements NetService {

    /**
     * 创建.net容器，封装k8s api
     *
     * @param podName      podName
     * @param image        镜像名
     * @param podDir 容器目录
     * @throws CommonException 没找到dll，创建失败
     */
    @Override
    public void createPod(String podName, String image, String podDir) throws CommonException {
        File dllFile = getDllFile(new File(podDir));
        if (null != dllFile){
            K8sApi.runApp(PodTypeEnums.NET, podName, image, podDir, dllFile.getName());
            K8sApi.runService(podName, 80);
        }else {
            throw new CommonException(ResultEnums.COMMON_EXCEPTION);
        }
    }

    /**
     * 扫描dll文件，递归方式
     *
     * @param destFile 要扫描的文件或文件夹
     * @return 扫描到返回dll文件，没扫描到返回null
     */
    @Override
    public File getDllFile(File destFile) {
        String matchSuffix = ".dll";
        if (destFile == null){
            return null;
        }
        //是dll文件就直接返回
        if (destFile.isFile() && destFile.getName().endsWith(matchSuffix)){
            return destFile;
        }else {
            //是文件夹就递归搜索
            if (destFile.isDirectory()){
                File[] files = destFile.listFiles();
                if (null != files){
                    for (File file : files){
                        File resultFile = getDllFile(file);
                        //搜索到就返回
                        if (resultFile != null && resultFile.getName().endsWith(matchSuffix)){
                            return resultFile;
                        }
                    }
                }
            }
        }
        //没搜索到也返回空
        return null;
    }
}
