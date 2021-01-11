package entitiesdto;

import entities.Hobby;

public class HobbyDTO {

    public String description;

    public HobbyDTO(Hobby hobby) {
        this.description = hobby.getDescription();
    }
}
