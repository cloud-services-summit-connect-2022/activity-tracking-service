package org.globex.retail;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class ActivityTrackingResourceTest {

    @InjectMock
    KafkaService kafkaService;

    @Test
    public void testTrackEndpointCompletePayload() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\": \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\": {"
                + "    \"visitsCount\": 2,"
                + "    \"prevVisitTs\": 1647249258,"
                + "    \"firstVisitTs\" : 1647238460,"
                + "    \"campaign\": \"promo\","
                + "    \"newVisit\": 1,"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  },"
                + "  \"actionInfo\": {"
                + "    \"productId\": \"a12345\","
                + "    \"search\": \"waterbottle\","
                + "    \"searchCat\": \"swag\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(200).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService).emit(body);
    }

    @Test
    public void testTrackEndpointMinimalPayload() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(200).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService).emit(body);
    }

    @Test
    public void testTrackEndpointWithMissingIdSite() {

        String body = "{"
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingActivity() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingUserInfo() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingUserId() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingUrl() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingRand() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingType() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithMissingLocalTime() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithUserIdBadFormat() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"1234567890\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));
        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithUserRandBadFormat() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithUnknownProperty() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  },"
                + "  \"property\": \"value\""
                + "}";

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(400).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService, Mockito.never()).emit(Mockito.anyString());
    }

    @Test
    public void testTrackEndpointWithExceptionInKafkaService() {

        String body = "{"
                + "  \"idSite\": \"globex-retail\","
                + "  \"activity\": {"
                + "    \"userId\": \"cea10ebe-c5ef-4705-800b-939b351a1967\","
                + "    \"url\" : \"https://globex-retail.com/product/12345/details\","
                + "    \"rand\": \"ba6eb330-4f7f-11eb-a2fb-67c34e9ac07c\","
                + "    \"type\": \"like\""
                + "  },"
                + "  \"userInfo\" : {"
                + "    \"localTime\": \"2022-03-14T09:26:04+00:00\""
                + "  }"
                + "}";

        Mockito.doThrow(RuntimeException.class).when(kafkaService).emit(Mockito.anyString());

        RestAssured.given().when().with().body(body).header(new Header("Content-Type", "application/json"))
                .post("/track")
                .then().assertThat().statusCode(500).body(Matchers.equalTo(""));

        Mockito.verify(kafkaService).emit(body);
    }
}
