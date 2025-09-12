package dailyNews.dailyNews.userController;

import dailyNews.dailyNews.dto.AuthResponse;
import dailyNews.dailyNews.dto.UserLogInDTO;
import dailyNews.dailyNews.dto.UserRegisterDTO;
import dailyNews.dailyNews.dto.UserResponseDTO;
import dailyNews.dailyNews.entity.User;
import dailyNews.dailyNews.repositories.UserRepository;
import dailyNews.dailyNews.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

     // User Log In
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> logInUser(@RequestBody UserLogInDTO userLogInDTO) {
        Optional<User> optionalUser = userRepo.findByEmail(userLogInDTO.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (this.passwordEncoder.matches(userLogInDTO.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user);

                UserResponseDTO userResponseDTO = new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                );

                AuthResponse authResponse = new AuthResponse(userResponseDTO, token);
                return ResponseEntity.status(HttpStatus.OK).body(authResponse);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // User List
    @GetMapping("/")
    public List<User> getAll() {return userRepo.findAll();}

    // User registration
    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> newUser(@RequestBody UserRegisterDTO userRegisterDTO) {

        if(userRepo.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            User user = new User();
            user.setEmail(userRegisterDTO.getEmail());
            user.setName(userRegisterDTO.getName());
            user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            user.setCreatedAt(new Date());
            userRepo.save(user);

            String token = jwtService.generateToken(user);
            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );

            AuthResponse authResponse = new AuthResponse(userResponseDTO, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
            }
        }

    @GetMapping("/auth/check-status")
        public ResponseEntity<AuthResponse> checkAuthStatus(@RequestHeader("Authorization") String authorizationHeader) {
            String token = authorizationHeader.replace("Bearer ", "");
            String username = jwtService.extractUsername(token);

            Optional<User> optionalUser = userRepo.findByEmail(username);
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                boolean isValid = jwtService.isTokenValid(token, user);

                if (isValid) {
                    UserResponseDTO userResponseDTO = new UserResponseDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail()
                    );

                    AuthResponse authResponse = new AuthResponse(userResponseDTO, token);
                    return ResponseEntity.ok(authResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
