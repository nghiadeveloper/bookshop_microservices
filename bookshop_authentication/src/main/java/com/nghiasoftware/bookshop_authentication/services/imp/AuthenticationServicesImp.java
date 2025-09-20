package com.nghiasoftware.bookshop_authentication.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nghiasoftware.bookshop_authentication.dto.UserDTO;
import com.nghiasoftware.bookshop_authentication.entity.Users;
import com.nghiasoftware.bookshop_authentication.enumable.StatusUser;
import com.nghiasoftware.bookshop_authentication.exception.DataNotFound;
import com.nghiasoftware.bookshop_authentication.payload.request.SignupRequest;
import com.nghiasoftware.bookshop_authentication.repository.UsersRepository;
import com.nghiasoftware.bookshop_authentication.services.AuthenticationServices;
import com.nghiasoftware.bookshop_authentication.utils.CommonHelper;
import com.nghiasoftware.bookshop_authentication.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public String signIn(String email, String password) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFound("User not found with email: " + email));

        if(users.getStatus() == StatusUser.BLOCK) {
            unBLockUser(users);
        }

        if (!passwordEncoder.matches(password, users.getPassword())) {
            blockUser(users);
            throw new DataNotFound("Invalid password for user: " + email);
        } else {
            return jwtHelper.generateToken(users.getEmail());
        }
    }

    @Override
    public void signUp(SignupRequest signupRequest) {
        Optional<Users> existingUser = userRepository.findByEmail(signupRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new DataNotFound("Email already exists with email: " + signupRequest.getEmail());
        }

        String randomPassword = CommonHelper.generateRandomPassword(8);

        Users newUser = new Users();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setStatus(StatusUser.CHANGE_PASSWORD);
        newUser.setAttemp(0);
        newUser.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(newUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(newUser.getEmail());
        userDTO.setPassword(randomPassword);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonData = objectMapper.writeValueAsString(userDTO);
            kafkaTemplate.send("email", jsonData);
        } catch (Exception e) {
            throw new RuntimeException("Error converting UserDTO to JSON", e);
        }
    }

    private void blockUser(Users users) {
        int count = users.getAttemp() + 1;
        users.setAttemp(count);
        if(count >= 3) {
            users.setStatus(StatusUser.BLOCK);
            redisTemplate.opsForValue().set(users.getEmail(), users.getId(), Duration.ofMinutes(15));
        }
        userRepository.save(users);
    }

    private void unBLockUser(Users users) {
        Optional<String> data = Optional.ofNullable(redisTemplate.opsForValue().get(users.getEmail()));
        if(data.isEmpty()) {
            users.setStatus(StatusUser.ACTIVE);
            users.setAttemp(0);
            userRepository.save(users);
        } else {
            throw new DataNotFound("User is not blocked or does not exist in Redis cache.");
        }
    }

}
