package team.cloud.platform.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.cloud.platform.entity.Pod;

import java.util.List;

/**
 * @author Ernest
 * @date 2018/9/16下午7:27
 */
@Repository
public interface PodMapper {

    /**
     * 插入一条完整的用户容器数据 , Id自增长
     *
     * @param pod 容器信息
     * @return 插入成功条数
     */
    int insertPod(Pod pod);

    /**
     * 根据PodId更新用户自定义名称
     *
     * @param name 用户自定义名称
     * @param podId podId
     * @return 更新成功条数
     */
    int updateNameByPodId(@Param("name") String name, @Param("podId") Integer podId);

    /**
     * 根据podName更新IP地址
     *
     * @param ipPort ip地址
     * @param podName 容器名称
     * @return 更新成功条数
     */
    int updateIpPortByPodName(@Param("ipPort") String ipPort, @Param("podName") String podName);

    /**
     * 根据podId更新容器是否存在
     *
     * @param exist 是否存在
     * @param podId podId
     * @return 更新成功条数
     */
    int updateExistByPodId(@Param("exist") Boolean exist, @Param("podId") Integer podId);


    /**
     * 根据主键删除一条容器记录
     *
     * @param podId 主键id
     * @return 删除成功的记录数量
     */
    int deletePodByPodId(@Param("podId") Integer podId);

    /**
     * 查询所有用户容器信息
     *
     * @return 所有用户容器信息
     */
    List<Pod> listAllPod();

    /**
     * 根据用户id和容器类型查询容器信息
     *
     * @param userId 用户id
     * @param type   容器类型
     * @return 容器信息列表
     */
    List<Pod> listPodByUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);

    /**
     * 查询容器存在总数
     *
     * @param exist 是否存在
     * @return 容器数量
     */
    int countPodByExist(@Param("exist") Boolean exist);

    /**
     * 查询podId通过userId
     *
     * @param userId 用户id
     * @return List<podId>
     */
    List<Integer> listPodIdByUserId(@Param("userId") Integer userId);

    /**
     * 通过容器类型查询某个用户的容器记录数量
     *
     * @param userId 用户id
     * @param type   容器类型
     * @return 容器数量
     */
    int countPodByUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);

    /**
     * 根据podId查询容器是否存在
     *
     * @param podId podId
     * @return 是否存在
     */
    Boolean getExistByPodId(@Param("podId") Integer podId);

    /**
     * 根据podId查询容器映射主机文件夹路径
     *
     * @param podId podId
     * @return 容器映射主机文件夹路径
     */
    String getDirSrcByPodId(@Param("podId") Integer podId);

    /**
     * 根据podId获取容器信息
     *
     * @param podId podId
     * @return 容器信息
     */
    Pod getPodByPodId(@Param("podId") Integer podId);
}
