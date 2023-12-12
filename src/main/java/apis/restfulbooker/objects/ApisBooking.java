package apis.restfulbooker.objects;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public class ApisBooking {

    private SHAFT.API api;

    /// Constructor ///
    public ApisBooking(SHAFT.API api){
        this.api = api;
    }

    /// Services names ///
    private static final String booking_serviceName = "/booking";


    ////////////////// Business Methods | Actions \\\\\\\\\\\\\\\\\\\\\
    public void createBooking(String firstName, String lastName, String additionalNeeds) {
        String createBookingBody = """
                {
                    "firstname" : "{FIRST_NAME}",
                    "lastname" : "{LAST_NAME}",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2023-01-01",
                        "checkout" : "2024-01-01"
                    },
                    "additionalneeds" : "{ADDITIONAL_NEEDS}"
                }
                """
                .replace("{FIRST_NAME}", firstName)
                .replace("{LAST_NAME}", lastName)
                .replace("{ADDITIONAL_NEEDS}", additionalNeeds);

        api.post(booking_serviceName)
                .setContentType(ContentType.JSON)
                .setRequestBody(createBookingBody)
                .setTargetStatusCode(Apis.success_statusCode)
                .perform();
    }

    public String getBookingId(String firstName, String lastName) {
        api.get(booking_serviceName)
                .setUrlArguments("firstname=" + firstName + "&lastname=" + lastName)
                .perform();
        return api.getResponseJSONValue("$[0].bookingid");
    }

    public void deleteBooking(String bookingId) {
        api.delete(booking_serviceName + '/' + bookingId)
                .setContentType(ContentType.JSON)
                .setTargetStatusCode(Apis.successDelete_statusCode)
                .perform();
    }

    ////////// Validations \\\\\\\\\\\\\\\\\
    public void validateThatTheBookingIsCreated(String expectedFirstName, String expectedLastName, String expectedAdditionalNeeds) {
        api.verifyThatResponse().extractedJsonValue("booking.firstname").isEqualTo(expectedFirstName).perform();
        api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo(expectedLastName).perform();
        api.verifyThatResponse().extractedJsonValue("booking.additionalneeds").isEqualTo(expectedAdditionalNeeds).perform();
        api.verifyThatResponse().body().contains("\"bookingid\":").perform();
    }

    public void validateThatTheBookingDeleted() {
        api.verifyThatResponse().body().contains("Created");
    }
}
