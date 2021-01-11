package facades;

import entities.Address;
import entities.Hobby;
import entities.Phone;
import entities.RenameMe;
import entities.Role;
import entities.User;
import entitiesdto.HobbyDTO;
import entitiesdto.UserDTO;
import entitiesdto.UsersDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class FacadeExample implements UserInterface {

    private static FacadeExample instance;
    private static EntityManagerFactory emf;

    private FacadeExample() {
    }

    public static FacadeExample getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FacadeExample();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    public long getRenameMeCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }

    }

    @Override
    public UserDTO getUserByPhone(String number) {

        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT u FROM User u JOIN u.phones p WHERE p.number = :number", User.class);
            query.setParameter("number", number);
            User user = (User) query.getSingleResult();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

    @Override
    public UsersDTO getAllUsersByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT u FROM User u JOIN u.hobbies h WHERE h.description = :hobby", User.class);
            query.setParameter("hobby", hobby);
            List<User> users = query.getResultList();
            return new UsersDTO(users);
        } finally {
            em.close();
        }
    }

    @Override
    public UsersDTO getAllUsersByCity(String city) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT u FROM User u JOIN u.address.cityInfo c WHERE c.city = :city", User.class);
            query.setParameter("city", city);
            List<User> users = query.getResultList();
            return new UsersDTO(users);
        } finally {
            em.close();
        }
    }

    @Override
    public long getUserCountByHobby(String hobby) {

        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(u) from User u JOIN u.hobbies h WHERE h.description = :hobby ");
            query.setParameter("hobby", hobby);
            long count = (long) query.getSingleResult();
            return count;
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> getAllZipsInDenmark() {

        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("Select c.zip FROM CityInfo c");
            List<String> zips = query.getResultList();
            return zips;
        } finally {
            em.close();
        }
    }
 // Virker ikke helt men vi gider ikke bruge mere tid på det
    @Override
    public UserDTO editUser(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();

        User user = em.find(User.class, userDTO.userName);

        String editedHobby = "";

        try {

            em.getTransaction().begin();
            user.setUserName(userDTO.userName);
            user.getAddress().setStreet(userDTO.street);
            

            for (Hobby hobby : user.getHobbies()) {
                for (HobbyDTO hobbyDTO : userDTO.hobbies) {
                    if (!hobby.getDescription().equals(hobbyDTO.description)) {

                        editedHobby = hobbyDTO.description;
                        hobby.setDescription(editedHobby);
                        
                    }
                }
            }

            em.merge(user);

            em.getTransaction().commit();

            return new UserDTO(user);

        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO deleteUser(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, userDTO.userName);

            em.getTransaction().begin();

            for (Phone p : user.getPhones()) {
                em.remove(p);
            }

            for (Hobby h : user.getHobbies()) {
                if (h.getUsers().size() <= 1) {
                    em.remove(h);
                } else {
                    h.getUsers().remove(h);
                }
            }

            em.remove(user);

            if (user.getAddress().getUsers().size() <= 1) {
                em.remove(user.getAddress());
            } else {
                user.getAddress().getUsers().remove(user);
            }
            em.remove(user.getAddress().getCityInfo());
            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        utils.SetupTestUsers.setUpUsers();
        EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        FacadeExample fe = FacadeExample.getFacadeExample(EMF);
        EntityManager em = emf.createEntityManager();
        User user1 = em.find(User.class, "user");
         user1.getHobbies().get(0).setDescription("fodbold");
        user1.getHobbies().set(0, user1.getHobbies().get(0));
        System.out.println(user1.getUserName());
        UserDTO user = new UserDTO(user1);

        fe.editUser(user);

    }

}
