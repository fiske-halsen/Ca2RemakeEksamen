package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import entitiesdto.CreateUserDTO;
import entitiesdto.UserDTO;
import entitiesdto.UsersDTO;
import errorhandling.UserNotFoundException;
import facades.FacadeExample;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;
import utils.SetupTestUsers;

@Path("info")
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ExecutorService ES = Executors.newCachedThreadPool();
    private static final FacadeExample FACADE = FacadeExample.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String cachedResponse;
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @Path("parrallel")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getStarWarsParrallel() throws InterruptedException, ExecutionException, TimeoutException {
        String result = fetcher.StarWarsFetcher.responseFromExternalServersParrallel(ES, GSON);
        cachedResponse = result;
        return result;
    }

    @Path("cached")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getStarWarsCached() throws InterruptedException, ExecutionException, TimeoutException {
        return cachedResponse;
    }

    @Path("setupusers")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public void setUpUsers() {
        SetupTestUsers.setUpUsers();
    }

    @Path("planets")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPlanets() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String result = fetcher.StarWarsPlanetFetcher.responseFromExternalServersSequential(ES, GSON);
        cachedResponse = result;
        return result;
    }

    @Path("countries")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCountries() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String result = fetcher.CountriesFetcher.responseFromExternalServersSequential(ES, GSON);
        cachedResponse = result;
        return result;
    }

    @Path("phone/{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserByPhone(@PathParam("phone") String phone) throws UserNotFoundException {
        return GSON.toJson(FACADE.getUserByPhone(phone));
    }

    @Path("hobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsersByHobby(@PathParam("hobby") String hobby) throws UserNotFoundException {
        return GSON.toJson(FACADE.getAllUsersByHobby(hobby));
    }

    @Path("hobbycount/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsersCountByHobby(@PathParam("hobby") String hobby) {
        return GSON.toJson(FACADE.getUserCountByHobby(hobby));
    }

    @Path("allzips")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllZips() {
        return GSON.toJson(FACADE.getAllZipsInDenmark());
    }

    @Path("city/{city}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsersCountByCity(@PathParam("city") String city) throws UserNotFoundException {
        return GSON.toJson(FACADE.getAllUsersByCity(city));
    }
    @Disabled
    @Path("edituser")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(String user) throws UserNotFoundException {
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        userDTO = FACADE.editUser(userDTO);
        return Response.ok(userDTO).build();
    }
    @Disabled
    @Path("deleteuser")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteUser(String user) throws UserNotFoundException {
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        userDTO = FACADE.deleteUser(userDTO);

        return GSON.toJson(userDTO);
        //return Response.ok(userDTO).build();
    }
    @Disabled
    @Path("createuser")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String user) {
       CreateUserDTO createUserDTO = GSON.fromJson(user, CreateUserDTO.class);
       UserDTO userDTO = FACADE.createUser(createUserDTO);
        return GSON.toJson(userDTO);
        //return Response.ok(userDTO).build();
    }

    @Path("getallusers")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllUsers() throws UserNotFoundException {
        UsersDTO users = FACADE.getAllUsers();
        return GSON.toJson(users);
    }

    @Path("catfacts")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCatFacts() throws InterruptedException, ExecutionException, TimeoutException {
        String result = fetcher.CatFactFetcher.fetchCatFactsParallel(GSON, ES);
        cachedResponse = result;
        return result;
    }

}
