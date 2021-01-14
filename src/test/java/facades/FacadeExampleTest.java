package facades;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Phone;
import utils.EMF_Creator;
import entities.RenameMe;
import entities.Role;
import entities.User;
import entitiesdto.HobbyDTO;
import entitiesdto.UserDTO;
import entitiesdto.UsersDTO;
import errorhandling.UserNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
@Disabled
public class FacadeExampleTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static FacadeExample fe;
    private static User user, admin, both;
    private static Address a1;
    private static CityInfo c1;
    private static Phone p1, p2;
    private static Hobby h1;
    private static Role userRole, adminRole;

    public FacadeExampleTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
        fe = FacadeExample.getFacadeExample(emf);
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
            /*  em.createNativeQuery("DELETE FROM PHONE").executeUpdate();
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
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testVerifyUser() throws AuthenticationException {
        User user = facade.getVeryfiedUser("admin", "testadmin");
        assertEquals("admin", admin.getUserName());
    }

    @Test
    public void getUserByPhone() throws UserNotFoundException {
        UserDTO userDTO = fe.getUserByPhone("56789");
        String expUserName = "user";
        assertEquals(expUserName, userDTO.userName, "Expected username is 'User' ");
    }

    @Test
    public void getUsersByCity() throws UserNotFoundException {
        UsersDTO usersDTO = fe.getAllUsersByCity("Taastrup");

        UserDTO u = new UserDTO(user);
        UserDTO a = new UserDTO(admin);

        assertEquals(2, usersDTO.users.size(), "Expects two users  with the given city");
        assertThat(usersDTO.users, containsInAnyOrder(u, a));
    }

    @Test
    public void getUsersByHobby() throws UserNotFoundException {
        UsersDTO usersDTO = fe.getAllUsersByHobby("Fitness");

        UserDTO u = new UserDTO(user);
        UserDTO a = new UserDTO(admin);

        assertEquals(2, usersDTO.users.size(), "Expects two users  with the given city");
        assertThat(usersDTO.users, containsInAnyOrder(u, a));
    }

    @Test
    public void getUserCountByHobby() {
        long userCount = fe.getUserCountByHobby("Fitness");
        long exp = 2;
        assertEquals(exp, userCount, "Expects two users  with the given city");
    }

    @Test
    public void getAllZipsInDenmark() {
        List<Long> zips = fe.getAllZipsInDenmark();
        long zip = 2630;
        assertThat(zips, hasSize(1));
        assertThat(zips, contains(zip));
    }

    
    @Test
    public void editUser() throws UserNotFoundException {
        Hobby hobby = admin.getHobbies().get(0);
        hobby.setDescription("Papir og blyant");
        admin.getHobbies().set(0, hobby);
        UserDTO u1 = new UserDTO(admin);
        UserDTO editedUser = fe.editUser(u1);
        assertThat(editedUser.hobbies, containsInAnyOrder(new HobbyDTO(hobby)));
    }

    @Test
    public void deleteUser() throws UserNotFoundException {
        UserDTO u1 = new UserDTO(user);
        UserDTO deletedUser = fe.deleteUser(u1);
        System.out.println(deletedUser + "llllllllllllllllllllllllllllllllllll");
        assertEquals(u1.userName, deletedUser.userName);

    }
    
    
    
    
    
    
    
    
}
