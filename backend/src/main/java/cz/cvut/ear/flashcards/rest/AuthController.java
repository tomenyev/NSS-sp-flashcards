package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.model.ERole;
import cz.cvut.ear.flashcards.model.Role;
import cz.cvut.ear.flashcards.model.User;
import cz.cvut.ear.flashcards.repository.RoleRepository;
import cz.cvut.ear.flashcards.security.UserPrincipal;
import cz.cvut.ear.flashcards.security.jwt.JwtUtils;
import cz.cvut.ear.flashcards.security.request.SignInRequest;
import cz.cvut.ear.flashcards.security.request.SignUpRequest;
import cz.cvut.ear.flashcards.security.response.JwtResponse;
import cz.cvut.ear.flashcards.security.response.MessageResponse;
import cz.cvut.ear.flashcards.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * auth rest controller
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@SuppressWarnings("ALL")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JmsTemplate jmsTemplate;


    /**
     * authenticate user by sign in request
     * @param signInRequest
     * @return response entity with user info
     */
    @PostMapping("/signin")
    @Transactional
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        System.out.println("Sending a login transaction...");
        jmsTemplate.convertAndSend("LoginTransactionQueue", signInRequest);



        return ResponseEntity.ok(new JwtResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getEmail(), roles));
    }

    /**
     * registrate user by sign up request
     * @param signUpRequest
     * @return respnse entity with new user
     */
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userDao.existsUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userDao.existsEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        Set<Role> roles = new HashSet<>();

        List<String> strRoles = signUpRequest.getRole();

        if (strRoles == null || strRoles.size() == 0) {
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
            if(userRole.isPresent()) {
                roles.add(userRole.get());
            } else {
                this.createRoles().forEach(r -> {
                    if(r.getName().equals(ERole.ROLE_USER)) {
                        roles.add(r);
                    }
                });
            }

        } else {
            Role userRole = new Role();
            Optional<Role> optRole;
            for(String r : strRoles) {
                switch (r) {
                    case "ROLE_USER":
                         optRole = roleRepository.findByName(ERole.ROLE_USER);
                        if(!optRole.isPresent()) {
                            for(Role role : this.createRoles()) {
                                if(role.getName().equals(ERole.ROLE_USER)) {
                                    userRole = role;
                                }
                            }
                        } else {
                            userRole = optRole.get();
                        }
                        break;
                    case "ROLE_ADMIN":
                        optRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                        if(!optRole.isPresent()) {
                            for(Role role : this.createRoles()) {
                                if(role.getName().equals(ERole.ROLE_ADMIN)) {
                                    userRole = role;
                                }
                            }
                        } else {
                            userRole = optRole.get();
                        }
                        break;
                    default:
                        optRole = roleRepository.findByName(ERole.ROLE_USER);
                        if(!optRole.isPresent()) {
                            for(Role role : this.createRoles()) {
                                if(role.getName().equals(ERole.ROLE_USER)) {
                                    userRole = role;
                                }
                            }
                        } else {
                            userRole = optRole.get();
                        }
                        break;
                }
                roles.add(userRole);
            }
        }

        if(signUpRequest.getUsername().equals("admin")) {
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
        }

        User user = new User.Builder()
                .withUsername(signUpRequest.getUsername())
                .withEmail(signUpRequest.getEmail())
                .withPassword(passwordEncoder.encode(signUpRequest.getPassword()))
                .withActive(true)
                .withRoles(roles)
                .build();

        userDao.persist(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * create application roles
     * @return roles
     */
    private List<Role> createRoles() {
        Role roleUser = new Role(ERole.ROLE_USER);
        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        return roleRepository.saveAll(Arrays.asList(roleUser, roleAdmin));
    }

}
