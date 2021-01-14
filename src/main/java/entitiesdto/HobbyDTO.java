package entitiesdto;

import entities.Hobby;
import java.util.Objects;

public class HobbyDTO {

    public String description;

    public HobbyDTO(Hobby hobby) {
        this.description = hobby.getDescription();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.description);
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
        final HobbyDTO other = (HobbyDTO) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
