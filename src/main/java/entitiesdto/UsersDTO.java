package entitiesdto;

import entities.User;
import java.util.ArrayList;
import java.util.List;

public class UsersDTO {

    public List<UserDTO> users = new ArrayList();

    public UsersDTO(List<User> users) {

        users.forEach(user -> {
            this.users.add(new UserDTO(user));
        });
    }
}
