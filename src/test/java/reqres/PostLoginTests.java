package reqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PostLoginTests {


    @Test
    public void putLoginSuccessfulStatusCodeValidValues() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .andReturn();
        response.prettyPrint();

        int statusCode = response.statusCode();
        System.out.println("Status Code: \n " + statusCode);
        String contentType = response.contentType();
        System.out.println("Content Type : \n" + contentType);

        SoftAssertions softAssert = new SoftAssertions();

        softAssert.assertThat(200).isEqualTo(statusCode);
        softAssert.assertThat("application/json; charset=utf-8").isEqualTo(contentType);
        softAssert.assertAll();

    }

    @Test
    public void putLoginSuccessfulStatusCodeInvalidValues() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        ValidatableResponse response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void putLoginSuccessfulResponseTextInvalidValues() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        JsonPath respons = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .jsonPath();

        respons.prettyPrint();

        Assertions.assertEquals("Missing password", respons.get("error").toString());
    }

    @Test
    public void putLoginSuccessfulResponseTextValidValues() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        JsonPath respons = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .jsonPath();

        respons.prettyPrint();

        SoftAssertions softAssert = new SoftAssertions();

        softAssert.assertThat(4).isEqualTo(respons.get("id"));
        softAssert.assertThat("QpwL5tke4Pnpja7X4").isEqualTo(respons.get("token"));

        softAssert.assertAll();
    }

    @Test
    public void putLoginSuccessfulResponseTextUnregisteredUser() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "lihtenvald95@rt.er");
        body.put("password", "qwerty");

        Response respons = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .andReturn();

        System.out.println(respons.statusCode());

        Assertions.assertTrue(respons.prettyPrint().contains("Only defined users succeed registration"));
        Assertions.assertEquals(400, respons.statusCode());
    }
}
