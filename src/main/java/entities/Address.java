package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;

    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private List<User> users;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private CityInfo cityInfo;

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
        if (cityInfo != null) {
            cityInfo.getAddresses().add(this);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.street);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        return true;
    }
    
    
    public void deleteCityInfo(CityInfo cityInfo){
        
        if(cityInfo != null){
           this.setCityInfo(null);
           cityInfo.getAddresses().remove(this);
            
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUser(User user) {
        this.users = users;
        if (user != null) {
            user.setAddress(this);
            this.users.add(user);
        }
    }

    public Address() {
    }

    public Address(String street) {
        this.street = street;
        this.users = new ArrayList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
