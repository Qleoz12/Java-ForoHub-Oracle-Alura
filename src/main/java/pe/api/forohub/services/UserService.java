package pe.api.forohub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.api.forohub.domain.user.CreateUserDTO;
import pe.api.forohub.domain.user.ResponseUserDTO;
import pe.api.forohub.domain.user.User;
import pe.api.forohub.domain.user.UserRepository;

import java.net.URI;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseUserDTO createUser(CreateUserDTO newUser) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setName(newUser.name());
        user.setEmail(newUser.email());
        user.setPassword(encoder.encode(newUser.password()));
        userRepository.save(user);
        return new ResponseUserDTO(user);
    }

    public ResponseUserDTO getUserById(Long id) {
        User user = userRepository.getReferenceById(id);
        return new ResponseUserDTO(user);
    }
}

