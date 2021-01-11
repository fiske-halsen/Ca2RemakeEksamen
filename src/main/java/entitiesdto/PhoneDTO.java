package entitiesdto;

import entities.Phone;

class PhoneDTO {

    public String number;

    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
    }

}
