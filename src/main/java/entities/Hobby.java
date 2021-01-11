package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    
    @ManyToMany
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users = users;
        if(user != null){
            this.users.add(user);
            user.getHobbies().add(this);
        }
    }
    

    public Hobby() {
    }

    public Hobby(String description) {
        this.description = description;
        this.users = new ArrayList();
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

   
    
}
