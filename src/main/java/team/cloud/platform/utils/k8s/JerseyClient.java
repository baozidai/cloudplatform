package team.cloud.platform.utils.k8s;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import lombok.Data;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;

/**
 * k8s POST GET DELETE基础类
 *
 * @author Ernest
 * @date 2018/9/10下午9:07
 */
public class JerseyClient {

    private static String apiIp="10.4.208.76:8080" ;

    private static String namespace="default" ;

    private static String podApi="http://" + apiIp + "/api/v1/namespaces/" + namespace + "/pods/";

    private static String serviceApi="http://" + apiIp + "/api/v1/namespaces/" + namespace + "/services/";

    public static String getPodApi() {
        return podApi;
    }

    public static String getServiceApi() {
        return serviceApi;
    }

    public static JSONObject restPost(String api, String json) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource resource = client.resource(api);

        ClientResponse response = resource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, json);
        return response.getEntity(JSONObject.class);
    }

    public static JSONObject restDelete(String api, String name) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource resource = client.resource( api + name);
        JSONObject req = new JSONObject();

        ClientResponse response = resource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class, req);
        return response.getEntity(JSONObject.class);
    }

    public static JSONObject restGet(String api) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource resource = client.resource(api);

        ClientResponse response = resource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        return response.getEntity(JSONObject.class);
    }

    /*public static JSONObject restPut(String api, String name, String json) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource resource = client.resource(api + name);

        ClientResponse response = resource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, json);
        return response.getEntity(JSONObject.class);
    }*/

}
