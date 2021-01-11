package facades;

import entitiesdto.UserDTO;
import entitiesdto.UsersDTO;
import java.util.List;


public interface UserInterface {
    
   public abstract UserDTO getUserByPhone(String number);
   public abstract UsersDTO getAllUsersByHobby(String hobby);
   public abstract UsersDTO getAllUsersByCity(String city);
   public abstract long getUserCountByHobby(String hobby);
   public abstract List<String> getAllZipsInDenmark();
   public abstract UserDTO editUser(UserDTO userDTO);
   public abstract UserDTO deleteUser(UserDTO userDTO);
}
