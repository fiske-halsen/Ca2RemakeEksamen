package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 25)
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;
    @JoinTable(name = "user_roles", joinColumns = {
        @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Hobby> hobbies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Phone> phones;
    
    public List<Phone> getPhones() {
        return phones;
    }
    
    public void setPhone(Phone phone) {
        this.phones = phones;
        if (phone != null) {
            phone.setUser(this);
            this.phones.add(phone);
        }
    }
    
    public void removePhone(Phone phone){
        if(phone != null){
            this.phones.remove(phone);
            phone.setUser(null);
        }
    }
    
    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void addHobby(Hobby hobby) {
        this.hobbies = hobbies;
        if (hobby != null) {
            this.hobbies.add(hobby);
            hobby.getUsers().add(this);
        }
    }
    
    public void removeHobby(Hobby hobby){
        if(hobby != null){
            this.hobbies.remove(hobby);
            hobby.getUsers().remove(this);
        }
    }
    
    public void removeRole(Role role){
        if(role != null){
            this.roleList.remove(role);
            role.getUserList().remove(role);
        }
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.getUsers().add(this);
        }
    }
    
    public void removeAddress (Address address){
        if(address != null){
            this.setAddress(null);
            address.getUsers().remove(this);
        }
    }
    
    
    

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public User() {
    }

    //TODO Change when password is hashed
    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, this.userPass));
    }

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
        this.hobbies = new ArrayList();
        this.phones = new ArrayList();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

}
