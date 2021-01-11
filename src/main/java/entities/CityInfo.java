
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zip;
    private String city;
    
    @OneToMany(mappedBy = "cityInfo")
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(Address address) {
        this.addresses = addresses;
        if(address != null){
            address.setCityInfo(this);
            this.addresses.add(address);
        }
    }
    
    

    public CityInfo() {
    }

    public CityInfo(String zip, String city) {
        this.zip = zip;
        this.city = city;
        this.addresses = new ArrayList();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    
    
  
    
}
