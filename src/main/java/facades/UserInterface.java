package facades;

import entitiesdto.CreateUserDTO;
import entitiesdto.UserDTO;
import entitiesdto.UsersDTO;
import errorhandling.UserNotFoundException;
import java.util.List;


public interface UserInterface {
    
   public abstract UserDTO getUserByPhone(String number) throws UserNotFoundException;
   public abstract UsersDTO getAllUsersByHobby(String hobby) throws UserNotFoundException;
   public abstract UsersDTO getAllUsersByCity(String city) throws UserNotFoundException;
   public abstract long getUserCountByHobby(String hobby);
   public abstract List<Long> getAllZipsInDenmark();
   public abstract UserDTO editUser(UserDTO userDTO) throws UserNotFoundException;
   public abstract UserDTO deleteUser(UserDTO userDTO) throws UserNotFoundException;
   public abstract UsersDTO getAllUsers() throws UserNotFoundException;
   public abstract UserDTO createUser(CreateUserDTO createUserDTO);
}



