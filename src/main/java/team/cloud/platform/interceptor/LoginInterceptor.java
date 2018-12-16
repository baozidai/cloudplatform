package team.cloud.platform.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Ernest
 * @date 2018/9/16下午7:29
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler, ModelAndView modelAndView)
            throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO Auto-generated method stub
        HttpSession session = request.getSession(false);
        String userId = "userId";
        response.setDateHeader("expires", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        if((session==null)) {
            logger.info("未登录，session为空，不放行");
            response.setHeader("sessionStatus","nosession");
            response.sendRedirect(request.getContextPath()+"/index.html");
            return false;
        }
        else {
            if((session.getAttribute(userId)!=null)) {
                logger.info("用户已登录，放行");
                return true;
            }
            logger.info("登录失效，不放行");
            response.setHeader("sessionStatus","timeout");
            response.sendRedirect(request.getContextPath()+"/index.html");
            return false;
        }

    }

}

