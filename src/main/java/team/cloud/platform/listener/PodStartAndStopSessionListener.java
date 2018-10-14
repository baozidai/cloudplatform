package team.cloud.platform.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import team.cloud.platform.dao.PodMapper;
import team.cloud.platform.entity.User;
import team.cloud.platform.service.PodService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
@WebListener
public class PodStartAndStopSessionListener implements HttpSessionListener,ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(PodStartAndStopSessionListener.class);
    private PodMapper podMapper;

    private PodService podService;

    private ServletContext application = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("context init");
        application = servletContextEvent.getServletContext();
        Set<String> onlineUserSet = new HashSet<String>();
        application.setAttribute("onlineUserSet", onlineUserSet);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("context destroy");
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.info("session create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Integer userId = (Integer)httpSessionEvent.getSession().getAttribute("userId");
        podService = WebApplicationContextUtils.getWebApplicationContext(httpSessionEvent.getSession().getServletContext()).getBean(PodService.class);
        podMapper = WebApplicationContextUtils.getWebApplicationContext(httpSessionEvent.getSession().getServletContext()).getBean(PodMapper.class);
        List<Integer> idList = podMapper.listPodIdByUserId(userId);
        if(idList!=null){
            for(Integer podId:idList){
                podService.stopPod(podId);
            }
        }
        Set<String> onlineUserSet = (Set<String>)application.getAttribute("onlineUserSet");
        String userName = ((User)httpSessionEvent.getSession().getAttribute("user")).getUserName();
        onlineUserSet.remove(userName);
        application.setAttribute("onlineUserSet", onlineUserSet);
        onlineUserSet = (Set<String>)application.getAttribute("onlineUserSet");
        logger.info(onlineUserSet.toString());
        logger.info(userName + "超时退出");
        logger.info("session destroy");
    }
}
