package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Phone;
import entities.RenameMe;
import entities.Role;
import entities.User;
import entitiesdto.HobbyDTO;
import entitiesdto.PhoneDTO;
import entitiesdto.UserDTO;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test

@Disabled
public class RenameMeResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User user, admin, both;
    private static Address a1;
    private static CityInfo c1;
    private static Phone p1, p2;
    private static Hobby h1;
    private static Role userRole, adminRole;
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        EntityManager em = emf.createEntityManager();

        user = new User("user", "testuser");
        admin = new User("admin", "testadmin");
        both = new User("user_admin", "testuseradmin");
        a1 = new Address("Street");
        c1 = new CityInfo(2630, "Taastrup");
        p1 = new Phone("56789");
        p2 = new Phone("12345");
        h1 = new Hobby("Fitness");
        userRole = new Role("user");
        adminRole = new Role("admin");

        try {

            em.getTransaction().begin();
            /* em.createNativeQuery("DELETE FROM PHONE").executeUpdate();
            em.createNativeQuery("DELETE FROM HOBBY_users").executeUpdate();
            em.createNativeQuery("DELETE FROM user_roles").executeUpdate();
            em.createNativeQuery("DELETE FROM users").executeUpdate();
            em.createNativeQuery("DELETE FROM ADDRESS").executeUpdate();
            em.createNativeQuery("DELETE FROM CITYINFO").executeUpdate();
            em.createNativeQuery("DELETE FROM roles").executeUpdate();
            em.createNativeQuery("DELETE FROM HOBBY").executeUpdate();*/

            user.addHobby(h1);
            user.addPhone(p1);
            a1.setCityInfo(c1);
            user.setAddress(a1);

            admin.addHobby(h1);
            admin.addPhone(p2);
            admin.setAddress(a1);

            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);

            em.persist(both);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testServerIsUp() {
        given().when().get("/info").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello anonymous"));
    }

    @Disabled
    @Test
   
    public void testParrallel() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/parrallel").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("peopleName", equalTo("Luke Skywalker"))
                .body("planetName", equalTo("Yavin IV"))
                .body("speciesName", equalTo("Ewok"))
                .body("starshipName", equalTo("Star Destroyer"))
                .body("vehicleName", equalTo("Sand Crawler"));

    }

    @Disabled
    @Test
    
    public void testCached() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/cached").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("peopleName", equalTo("Luke Skywalker"))
                .body("planetName", equalTo("Yavin IV"))
                .body("speciesName", equalTo("Ewok"))
                .body("starshipName", equalTo("Star Destroyer"))
                .body("vehicleName", equalTo("Sand Crawler"));
    }
    @Order(2)
    @Test
    public void testGetUserByPhone() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/phone/" + p2.getNumber()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("userName", equalTo("admin"))
                .body("street", equalTo("Street"))
                .body("zip", equalTo(2630))
                .body("city", equalTo("Taastrup"))
                .body("hobbies.description", hasItems("Fitness"))
                .body("phones.number", hasItems("12345"));
    }
    @Order(1)
    @Test
    public void testGetUsersByCity() throws Exception {
        List<UserDTO> usersDTO;

        usersDTO = given()
                .contentType("application/json")
                .when()
                .get("/info/city/" + user.getAddress().getCityInfo().getCity())
                .then()
                .extract().body().jsonPath().getList("users", UserDTO.class);

        UserDTO u1 = new UserDTO(user);
        UserDTO u2 = new UserDTO(admin);

        assertThat(usersDTO, containsInAnyOrder(u1, u2));

    }
    @Order(3)
    @Test
    public void testDeleteUser() throws Exception {

        JSONObject request = new JSONObject();
        JSONArray hobbies = new JSONArray();
        JSONArray phones = new JSONArray();

        hobbies.add(new HobbyDTO(new Hobby("fodbold")));

        phones.add(new PhoneDTO(new Phone("42913009")));

        request.put("userName", "admin");
        request.put("street", "Street");
        request.put("zip", "2630");
        request.put("city", "Taastrup");
        request.put("hobbies", hobbies);
        request.put("phones", phones);

        given()
                .contentType("application/json")
                .body(new UserDTO(admin))
                .when()
                .delete("/info/deleteuser")
                .then()
                .assertThat()
                .statusCode((HttpStatus.OK_200.getStatusCode()));

    }
    
   @Order(4)
    @Test
    public void testCreateUser() throws Exception {

        User user = new User("hej", "noooo");

        Address a1 = new Address("Kaskaderne");
        CityInfo c1 = new CityInfo(2630, "Hillerød");
        Phone p1 = new Phone("5123132131");
        Hobby h1 = new Hobby("Fiske");
        Role userRole = new Role("user");

        a1.setCityInfo(c1);
        user.setAddress(a1);
        user.addPhone(p1);
        user.addHobby(h1);

        given()
                .contentType("application/json")
                .body(new UserDTO(user))
                .when()
                .post("/info/createuser")
                .then()
                .assertThat()
                .statusCode((HttpStatus.OK_200.getStatusCode()));

    }

}
