package team.cloud.platform.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import static team.cloud.platform.utils.DESUtil.getDecryptString;

/**
 * @author Ernest
 * @date 2018/9/16下午7:34
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    /**
     * 需要加密的字段数组
     */
    private String[] encryptPropNames = { "username", "password" };

    /**
     * 对关键的属性进行转换
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对已加密的字段进行解密工作
            return getDecryptString(propertyValue);
        } else {
            return propertyValue;
        }
    }

    /**
     * 该属性是否已加密
     * @param propertyName propertyName
     * @return true or false
     */
    private boolean isEncryptProp(String propertyName) {
        // 若等于需要加密的field，则进行加密
        for (String encryptPropertyName : encryptPropNames) {
            if (encryptPropertyName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
