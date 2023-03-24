package edu.uoc.epcsd.showcatalog.application.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.showcatalog.application.request.CreateCompanyRequest;
import edu.uoc.epcsd.showcatalog.domain.Company;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static io.restassured.RestAssured.withArgs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CompanyControllerRestAssuredUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String REST_COMPANIES_PATH = "/companies";
    private static final String REST_PARAMETER_ID_PATH = "/{id}";
    private static final String USERNAME_ADMIN = "admin1@fakemail.com";
    private static final String PASSWORD_ADMIN = "admin";

    @Before
    public void before(){
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    public String getTokenLoginAdmin(){

        MockMvcResponse response = RestAssuredMockMvc.given()
                .log().all()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", USERNAME_ADMIN)
                .formParam("password", PASSWORD_ADMIN)
                .when()
                .post("/login");
        return (response.header("authorization"));

    }

    @Test
    public void getTestFindCompanies(){

        RestAssuredMockMvc.given()
                .log().all()
                .contentType("application/json")
                .when()
                .get(REST_COMPANIES_PATH)
                .then()
                .log().all()
                .statusCode(OK.value())
                .body("size()", is(2))
                .body("id",hasItems(1,2))
                .body("name",hasItems("Teatro Nacional de Cataluña","Gran Teatre del Liceu"))
                .body("[0].managers.size()",is(1))
                .rootPath("findAll{i -> i.id == %d}.description")
                .body(withArgs(1), hasItem("Teatro Nacional de Cataluña TNC"))
                .body(withArgs(2), hasItem("El Gran Teatro del Liceo de Barcelona, conocido como El Liceo (Gran Teatre del Liceu o El Liceu en catalán), es el teatro en activo más antiguo y prestigioso de Barcelona"));

    }

    @Test
    public void getTestFindCompanyById(){

        RestAssuredMockMvc.given()
                .log().all()
                .contentType("application/json")
                .when()
                .get(REST_COMPANIES_PATH + REST_PARAMETER_ID_PATH,1)
                .then()
                .log().all()
                .statusCode(OK.value())
                .body("id",is(1))
                .body("name",is("Teatro Nacional de Cataluña"))
                .body("description",is("Teatro Nacional de Cataluña TNC"));

    }

    @Test
    public void postTestCreateCompany() throws JsonProcessingException {

        String tokenADMIN = getTokenLoginAdmin();
        assertNotSame(tokenADMIN,"");

        Company companyTest= Company.builder().name("test").description("test").image("test").build();
        CreateCompanyRequest companyRequest = new CreateCompanyRequest(companyTest);

        RestAssuredMockMvc.given()
                .log().all()
                .headers(
                        "Authorization",
                        tokenADMIN,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(companyRequest))
                .post(REST_COMPANIES_PATH)
                .then()
                .log().all()
                .statusCode(CREATED.value())
                .header("Location", resp-> matchesRegex(".*" + REST_COMPANIES_PATH + "/[0-9]+"));

    }

    @Test
    public void deleteTestDeleteCompany() throws JsonProcessingException {

        String tokenADMIN = getTokenLoginAdmin();
        assertNotSame(tokenADMIN,"");

        Long idCompany=1L;
        RestAssuredMockMvc.given()
                .log().all()
                .headers(
                        "Authorization",
                        tokenADMIN,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .contentType("application/json")
                .when()
                .delete(REST_COMPANIES_PATH + REST_PARAMETER_ID_PATH,idCompany)
                .then()
                .log().all()
                .statusCode(OK.value());

    }


    @Test
    public void putTestUpdateCompany() throws JsonProcessingException {

        String tokenADMIN = getTokenLoginAdmin();
        assertNotSame(tokenADMIN,"");

        Long idCompany= 1L;
        Company companyTest= Company.builder().id(idCompany).name("test").description("test").image("test").build();
        CreateCompanyRequest companyRequest2 = new CreateCompanyRequest(companyTest);

        RestAssuredMockMvc.given()
                .log().all()
                .headers(
                        "Authorization",
                        tokenADMIN,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(companyRequest2))
                .when()
                .put(REST_COMPANIES_PATH + REST_PARAMETER_ID_PATH,idCompany)
                .then()
                .log().all()
                .statusCode(OK.value());
    }
}
