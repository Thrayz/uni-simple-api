package uni.com.demo.Service;

import uni.com.demo.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);
    List<User> getAllUsers();
    void deleteUser(Long userId);
    User getUserById(Long userId);
    User updateUser(User user, Long userId);
    List<Object> isUserPresent(User user);
    Optional<User> findById(Long userId);


}
