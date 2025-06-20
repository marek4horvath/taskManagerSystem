package taskmanager.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanager.dto.UserDto;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.mapper.UserMapper;
import taskmanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User createUser(@NotNull UserDto userDto) {
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(hashedPassword);
        User user = new User(
                userDto.getName(),
                userDto.getSurname(),
                userDto.getPassword()
        );

        return userRepository.save(user);
    }

    public boolean updateUser(UUID userId, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (userDto.getPassword() != null) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userMapper.updateUserFromDto(userDto, user);

        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        for (Task task : user.getTasks()) {
            task.getAssignedUsers().remove(user);
        }
        user.getTasks().clear();

        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

