import apis.restfulbooker.objects.Apis;
import apis.restfulbooker.objects.ApisBooking;
import com.shaft.driver.SHAFT;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestfulBookerLinearTests {
    private ApisBooking apisBooking;

    //////////// Tests \\\\\\\\\\\\
    @Test
    public void createBookingTest() {
        apisBooking.createBooking("Mahmoud", "ElSharkawy", "Metdla3a");
        apisBooking.validateThatTheBookingIsCreated("Mahmoud", "ElSharkawy", "Metdla3a");
    }

    @Test(dependsOnMethods = {"createBookingTest"})
    public void deleteBookingTest() {
        apisBooking.deleteBooking(apisBooking.getBookingId("Mahmoud", "ElSharkawy"));
        apisBooking.validateThatTheBookingDeleted();
    }


    /////////// Configurations \\\\\\\\\\\\\\\
    @BeforeClass
    public void beforeClass() {
        SHAFT.API api = new SHAFT.API(Apis.baseURL);
        Apis.login(api,"admin", "password123");

        apisBooking = new ApisBooking(api);
    }

}