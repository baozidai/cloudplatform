package team.cloud.platform.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ernest
 * @date 2018/9/16下午7:34
 */
public class LogBackTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBackTest.class);

    public static void main(String[] args) {
        LOGGER.info("logback的--info日志--输出了");
        LOGGER.error("logback的--error日志--输出了");
    }

}