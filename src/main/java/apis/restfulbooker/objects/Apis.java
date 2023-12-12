package apis.restfulbooker.objects;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public class Apis {

    SHAFT.API api;

    public Apis(SHAFT.API api){
        this.api = api;
    }


    public static String baseURL = System.getProperty("baseURL");
    private static final String authentication_serviceName = "/auth";

    // Status Codes
    public static final int success_statusCode = 200;
    public static final int successDelete_statusCode = 201;

    public static void login(SHAFT.API api, String username, String password) {
        String tokenBody = """
                {
                    "username" : "{USERNAME}",
                    "password" : "{PASSWORD}"
                }
                """
                .replace("{USERNAME}", username)
                .replace("{PASSWORD}", password);

        api.post(authentication_serviceName)
                .setContentType(ContentType.JSON)
                .setRequestBody(tokenBody)
                .setTargetStatusCode(Apis.success_statusCode)
                .perform();

        String token = api.getResponseJSONValue("token");

        api.addHeader("Cookie", "token=" + token);
    }
}
