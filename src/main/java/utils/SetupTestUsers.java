package utils;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Phone;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void setUpUsers() {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        User user = new User("user", "testuser");
        User admin = new User("admin", "testadmin");
        User both = new User("user_admin", "testuseradmin");
        Address a1 = new Address("Street");
        CityInfo c1 = new CityInfo(2630, "Taastrup");
        Phone p1 = new Phone("26154886");
        Hobby h1 = new Hobby("Fitness");

        if (admin.getUserPass().equals("test") || user.getUserPass().equals("test") || both.getUserPass().equals("test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }

        user.addHobby(h1);
        user.addPhone(p1);
        a1.setCityInfo(c1);
        user.setAddress(a1);
        
        
        admin.addHobby(h1);
        admin.addPhone(new Phone("42913009"));
        admin.setAddress(a1);
        
        
        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
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
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

    }

}
