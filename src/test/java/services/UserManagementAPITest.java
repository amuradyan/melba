package services;

import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.UserSpec;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

public class UserManagementAPITest {
  private static Config config = ConfigFactory.load().resolve();
  private static String rootUrl;
  private static Gson gson;
  private static String testUserEmail = "test_user@email.com";
  private static String testUserPassword = "password";


  @BeforeAll
  public static void setup() {
    String host = config.getString("app.host");
    String port = config.getString("app.port");
    rootUrl = String.format("http://%s:%s/", host, port);
    gson = new Gson();

    UserSpec testUser = new UserSpec();
    testUser.setEmail(testUserEmail);
    testUser.setPassword(testUserPassword);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(rootUrl + "users"))
      .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(testUser)))
      .build();

    try {
      client.send(req, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @DisplayName("It works")
  @Test
  public void test_Ping() {
    when().
      get(rootUrl).
      then().
      assertThat().
      statusCode(200).
      and().
      contentType(ContentType.JSON).
      and().
      body(is("\"It works!\""));
  }

  @DisplayName("User creation with invalid email fails with code 422")
  @Test
  public void test_UserCreationWithInvalidEmailFailsWith_422() {
    UserSpec invalidUserSpec = new UserSpec();
    invalidUserSpec.setEmail("invalid_email");
    invalidUserSpec.setPassword("valid_password");

    given().
      body(gson.toJson(invalidUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(422)).
      and().
      body(is(""));
  }

  @DisplayName("User creation with empty email fails with code 422")
  @Test
  public void test_UserCreationWithEmptyEmailFailsWith_422() {
    UserSpec invalidUserSpec = new UserSpec();
    invalidUserSpec.setPassword("valid_password");

    given().
      body(gson.toJson(invalidUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(422)).
      and().
      body(is(""));
  }

  @DisplayName("User creation with invalid password fails with code 422")
  @Test
  public void test_UserCreationWithInvalidPasswordFailsWith_422() {
    UserSpec invalidUserSpec = new UserSpec();
    invalidUserSpec.setEmail("vaild@email.io");
    invalidUserSpec.setPassword("pass"); // Too short

    given().
      body(gson.toJson(invalidUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(422)).
      and().
      body(is(""));
  }

  @DisplayName("User creation with empty password fails with code 422")
  @Test
  public void test_UserCreationWithEmptyPasswordFailsWith_422() {
    UserSpec invalidUserSpec = new UserSpec();
    invalidUserSpec.setEmail("vaild@email.io");

    given().
      body(gson.toJson(invalidUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(422)).
      and().
      body(is(""));
  }

  @DisplayName("User creation with invalid JSON fails with code 400")
  @Test
  public void test_UserCreationWithInvalidJsonFailsWith_400() {
    given().
      body("{ invalid_json").
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(400)).
      and().
      body(is(""));
  }

  @DisplayName("User creation with duplicate email fails with code 422")
  @Test
  public void test_UserCreationWithDuplicateEmailFailsWith_422() {
    UserSpec duplicateEmailUserSpec = new UserSpec();
    duplicateEmailUserSpec.setEmail(testUserEmail);
    duplicateEmailUserSpec.setPassword(testUserPassword);

    given().
      body(gson.toJson(duplicateEmailUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(422)).
      and().
      body(is("Email must be unique. You are already registered."));
  }


  @DisplayName("User creation with correct specs succeeds with 201 and newly created user as response")
  @Test
  public void test_UserCreationWithCorrectSpecsSucceedsWith_201() {
    UserSpec validUserSpec = new UserSpec();
    validUserSpec.setEmail("vaild@email.com");
    validUserSpec.setPassword("password");

    given().
      body(gson.toJson(validUserSpec)).
      post(rootUrl + "users").
      then().
      assertThat().
      statusCode(is(201)).
      and().
      body("email", is("vaild@email.com"));
  }
}
