package team.cloud.platform.utils.k8s;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import team.cloud.platform.enums.PodTypeEnums;
import team.cloud.platform.enums.ResultEnums;
import team.cloud.platform.exception.CommonException;

import javax.ws.rs.core.MediaType;


/**
 * k8sApi类
 *
 * @author Ernest
 * @date 2018/9/11下午8:07
 */
public class K8sApi {

    /**
     * 删除项目
     *
     * @param api 不同项目的不同地址
     * @param name 名称
     * @return 是否成功
     */
    public static Boolean delete(String api, String name) {
        JSONObject response;
        response = JerseyClient.restDelete(api, name);
        try {
            if(api.equals(JerseyClient.getServiceApi())){
                String s="Success";
                String status = "status";
                return s.equals(response.getString(status));
            }else if(api.equals(JerseyClient.getPodApi())){
                String n="name";
                response = response.getJSONObject("metadata");
                return name.equals(response.getString(n));
            }else {
                return false;
            }
        } catch (Exception e) {
            if (e instanceof JSONException) {
                System.err.println("fail");
            }
            return false;
        }
    }

    /**
     * 删除pod，不删除service
     *
     * @param api 不同项目的不同地址
     * @param name 名称
     * @return 是否成功
     */
    public static Boolean deletePod(String api, String name) {
        JSONObject response;
        response = JerseyClient.restDelete(api, name);
        try {
            String n="name";
            response = response.getJSONObject("metadata");
            return name.equals(response.getString(n));
        } catch (Exception e) {
            if (e instanceof JSONException) {
                System.err.println("fail");
            }
            return false;
        }
    }

    /**
     * 获取容器状态
     *
     * @param name 名称
     * @return 状态
     */
    public static String getStatus(String name)
    {
        JSONObject obj;
        String url=JerseyClient.getPodApi()+name;
        obj=JerseyClient.restGet(url);
        try {
            obj=obj.getJSONObject("status");
            return  obj.getString("phase");
        }
        catch(Exception e) {
            throw new CommonException(ResultEnums.POD_NOT_FOUND);
        }
    }

    /**
     * 得到Log
     *
     * @param name 名称
     * @return 日志
     */
    public static String getLog(String name) {
        String url=JerseyClient.getPodApi()+name+"/log";
        try {
            ClientConfig cc = new DefaultClientConfig();
            Client client = Client.create(cc);
            WebResource resource = client.resource(url);

            ClientResponse response = resource
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            return response.getEntity(String.class);
        }
        catch(Exception e) {
            throw new CommonException(ResultEnums.POD_NOT_FOUND);
        }
    }

    /**
     * 得到IP地址
     *
     * @param name 名称
     * @return IP地址
     */
    public static String getIp(String name)
    {
        JSONObject obj1,obj2;
        JSONArray arr;
        String url1=JerseyClient.getPodApi() + name;
        obj1=JerseyClient.restGet(url1);

        String url2=JerseyClient.getServiceApi() + name;
        obj2=JerseyClient.restGet(url2);
        System.out.println(obj1);
        System.out.println(obj2);
        try {
            obj1=obj1.getJSONObject("status");
            obj2=obj2.getJSONObject("spec");
            arr=obj2.getJSONArray("ports");
            obj2=arr.getJSONObject(0);
            String s=obj2.getString("nodePort");

            return  obj1.getString("hostIP")+":"+s;
        }
        catch(Exception e) {
            if(e instanceof JSONException) {
                System.out.println(e.getMessage());
            }
            return "null";
        }
    }

    /**
     * 通用创建pod方法
     *
     * @param podType pod类型
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @param dllName .net需要的dll文件名称
     * @return 是否创建
     */
    public static String runApp(PodTypeEnums podType, String name, String image, String volume, String dllName){
        String text;
        switch (podType){
            case TOMCAT:
                text = tomcatJson(name, image, volume);
                break;
            case PHP:
                text = phpJson(name, image, volume);
                break;
            case PYTHON:
                text = pythonJson(name, image, volume);
                break;
            case NET:
                text = netJson(name, image, volume, dllName);
                break;
            case JAR:
                text = jarJson(name,image,volume);
                break;
            default:
                throw new CommonException(ResultEnums.UNSUPPORTED_POD_TYPE);
        }
        JSONObject response;

        response = JerseyClient.restPost(JerseyClient.getPodApi(),text);
        try {
            response = response.getJSONObject("metadata");
            return response.getString("name");
        }
        catch(Exception e)
        {
            return "fail";
        }
    }

    /**
     * 创建service暴露外部端口
     *
     * @param name 名称
     * @param port 端口号
     * @return 是否成功
     */
    public static String runService(String name, Integer port) {
        JSONObject response;
        String test;
        test="{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Service\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\"\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"type\": \"NodePort\",\r\n" +
                "    \"ports\": [\r\n" +
                "      {\r\n" +
                "        \"port\": "+port+",\r\n" +
                "        \"targetPort\": "+port+"\r\n" +
                "      }\r\n" +
                "    ],\r\n" +
                "    \"selector\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  }\r\n" +
                "}";
        response = JerseyClient.restPost(JerseyClient.getServiceApi(),test);
        try {
            response = response.getJSONObject("metadata");
            return response.getString("name");
        }
        catch(Exception e)
        {
            return "fail";
        }
    }

    /**
     * tomcatJson文件
     *
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @return json文本的String形式
     */
    private static String tomcatJson(String name, String image, String volume) {
        String tomcatJson;
        tomcatJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+8080+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"env\": [\r\n" +
                "          {\r\n" +
                "            \"name\": \"JAVA_OPTS\",\r\n" +
                "            \"value\": \"-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap\"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/usr/local/tomcat/webapps/ROOT\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ]\r\n" +
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return tomcatJson;
    }

    /**
     * phpJson文件
     *
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @return json文本的String形式
     */
    private static String phpJson(String name, String image, String volume) {
        String phpJson;
        phpJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+80+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/var/www/html\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ]\r\n" +
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return phpJson;
    }

    /*private static String mysqlJson(String name, String image, String volume) {
        String mysqlJson;
        mysqlJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+3306+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +    //添加json对象时记得加逗号
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/var/lib/mysql\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"env\": [\r\n" +
                "          {\r\n" +
                "            \"name\": \"MYSQL_ROOT_PASSWORD\",\r\n" +
                "            \"value\": \""+"123456"+"\"\r\n" +
                "          }\r\n" +
                "        ]\r\n" +
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return mysqlJson;
    }*/

    /**
     * netJson文件
     *
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @param dllName dllName
     * @return json文本的String形式
     */
    private static String netJson(String name, String image, String volume, String dllName) {
        String netJson;
        netJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+80+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +    //添加json对象时记得加逗号
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/root/web\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"command\": [\r\n" +
                "          \"dotnet\",\r\n" +
                "          \""+dllName+"\"\r\n" +
                "        ]"+
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return netJson;
    }

    /**
     * pythonJson文件
     *
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @return json文本的String形式
     */
    private static String pythonJson(String name, String image, String volume) {
        String pythonJson;
        pythonJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+80+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/data\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"command\": [\r\n" +
                "          \"./abc\"\r\n" +
                "        ]"+
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return pythonJson;
    }

    /**
     * jarJson文件
     *
     * @param name 名称
     * @param image 镜像
     * @param volume 存储
     * @return json文本的String形式
     */
    private static String jarJson(String name, String image, String volume) {
        String jarJson;
        jarJson = "{\r\n" +
                "  \"apiVersion\": \"v1\",\r\n" +
                "  \"kind\": \"Pod\",\r\n" +
                "  \"metadata\": {\r\n" +
                "    \"name\": \""+name+"\",\r\n" +
                "    \"labels\": {\r\n" +
                "      \"name\": \""+name+"\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  \"spec\": {\r\n" +
                "    \"containers\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \""+name+"\",\r\n" +
                "        \"image\": \""+image+"\",\r\n" +
                "        \"ports\": [\r\n" +
                "          {\r\n" +
                "            \"containerPort\": "+80+"\r\n" +
                "          }\r\n" +
                "        ],\r\n" +
                "        \"volumeMounts\": [\r\n" +
                "          {\r\n" +
                "            \"mountPath\": \"/test\",\r\n" +
                "            \"name\": \"test-volume\"\r\n" +
                "          }\r\n" +
                "        ]\r\n" +
                "      }\r\n" +
                "    ],\r\n" +
                "    \"volumes\": [\r\n" +
                "      {\r\n" +
                "        \"name\": \"test-volume\",\r\n" +
                "        \"hostPath\": {\r\n" +
                "          \"path\": \""+volume+"\"\r\n" +
                "        }\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  }\r\n" +
                "}";
        return jarJson;
    }
}
