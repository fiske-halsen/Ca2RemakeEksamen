package entitiesdto;

import entities.Phone;

public class PhoneDTO {

    public String number;

    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
    }

}
