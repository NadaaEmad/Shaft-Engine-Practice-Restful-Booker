import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;
import org.python.antlr.ast.Str;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestfulBookerApiTest {
    SHAFT.API api;
    private String token;
    private String bookingId;
    public void setBookingId(String bookingId){
        this.bookingId = bookingId;
    }
    public String getBookingIdValue(){
        return bookingId;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getTokenValue(){
        return token;
    }

    @Test
    public void getTokenTest(){
        String tokenBody = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }
                """;
        api.post("/auth")
                .setContentType(ContentType.JSON)
                .setRequestBody(tokenBody)
                .setTargetStatusCode(200)
                .perform();
        api.assertThatResponse().body().contains("token");
        setToken(api.getResponseJSONValue("$.token"));
        System.out.println(token);
    }

    @Test
    public void createBookingTest(){
        String createBookingBody = """
                {
                    "firstname" : "Jim",
                    "lastname" : "Brown",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }
                """;
        api.post("/booking")
                .setContentType(ContentType.JSON)
                .setRequestBody(createBookingBody)
                .setTargetStatusCode(200)
                .perform();
        api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo("Brown").perform();
        api.verifyThatResponse().extractedJsonValue("booking.additionalneeds").isEqualTo("Breakfast").perform();
        api.verifyThatResponse().body().contains("\"bookingid\":").perform();
        setBookingId(api.getResponseJSONValue("$.bookingid"));
    }

    @Test(dependsOnMethods = { "getTokenTest", "createBookingTest"})
    public void deleteBooking(){
        System.out.println(getTokenValue());
        api.delete("/booking/" + getBookingIdValue())
                .setContentType(ContentType.JSON)
                .addCookie("token", getTokenValue())
                .setTargetStatusCode(201)
                .perform();
        api.verifyThatResponse().body().contains("Created");

    }

    @BeforeClass
    public void beforeClass(){
        api = new SHAFT.API("https://restful-booker.herokuapp.com");

    }

}
