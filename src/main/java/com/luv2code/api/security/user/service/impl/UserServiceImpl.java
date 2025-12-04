package com.luv2code.api.security.user.service.impl;


import com.luv2code.api.security.user.dto.LoginDto;
import com.luv2code.api.security.user.dto.UserDto;
import com.luv2code.api.security.user.entity.User;
import com.luv2code.api.security.user.entity.enums.Role;
import com.luv2code.api.security.user.dto.AuthenticationResponse;
import com.luv2code.api.security.user.exception.AlreadyExistsException;
import com.luv2code.api.security.user.repository.UserRepository;
import com.luv2code.api.security.user.service.JwtService;
import com.luv2code.api.security.user.service.RefreshTokenService;
import com.luv2code.api.security.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${spring.mail.username}")
    private String username;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JavaMailSender javaMailSender;

    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           RefreshTokenService refreshTokenService,
                           JavaMailSender javaMailSender) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.javaMailSender = javaMailSender;
    }


    @Override
    public AuthenticationResponse register(UserDto userDto) {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw  new AlreadyExistsException("There is already a user with this");
        }
        var user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getFirstname() + year));
        user.setRole(Role.ADMIN);

        user = userRepository.save(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        this.sendEmail(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));

        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .roles(roles)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    private void sendEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Creation de compte");
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        String message = "Mot de passe : " + user.getFirstname() + year;
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
