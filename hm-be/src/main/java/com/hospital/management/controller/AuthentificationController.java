package com.hospital.management.controller;

import com.hospital.management.config.JwtToken;
import com.hospital.management.model.Employee;
import com.hospital.management.model.User;
import com.hospital.management.model.dto.auth.ChangePasswordRequest;
import com.hospital.management.model.dto.auth.LoginRequest;
import com.hospital.management.model.dto.auth.RegisterRequest;
import com.hospital.management.service.implementation.UserDetailsServiceImpl;
import com.hospital.management.service.util.EmployeeServiceUtil;
import com.hospital.management.service.util.UserServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    UserServiceUtil userService;

    @Autowired
    EmployeeServiceUtil employeeService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    protected final Log logger = LogFactory.getLog(getClass());
    final AuthenticationManager authenticationManager;
    final JwtToken jwtTokenUtil;

    public AuthentificationController(AuthenticationManager authenticationManager, JwtToken jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws JSONException  {

        JSONObject responseMap = new JSONObject();

        try {
            User user = userService.findByEmail(request.getEmail());
            if (null == user) {
                responseMap.put("error", true);
                responseMap.put("message", "invalid_credentials");
                return ResponseEntity.status(401).body(responseMap.toString());
            }

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
            if (!auth.isAuthenticated()) {
                responseMap.put("error", true);
                responseMap.put("message", "invalid_credentials");
                return ResponseEntity.status(401).body(responseMap.toString());
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);
            Employee employee = employeeService.findByUserId(user.getUserId());

            if(employee != null) {
                responseMap.put("employee_id", employee.getEmployeeId());
            }
            responseMap.put("email", user.getEmail());
            responseMap.put("username", user.getUsername());
            responseMap.put("user_id", user.getUserId());
            responseMap.put("first_name", user.getFirstName());
            responseMap.put("last_name", user.getLastName());
            responseMap.put("role", user.getRole());
            responseMap.put("error", false);
            responseMap.put("message", "logged_in");
            responseMap.put("token", token);
            responseMap.put("activated", user.isActivated());
            return ResponseEntity.ok(responseMap.toString());
        }catch (Exception exception) {
            exception.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "internal_server_error");
            responseMap.put("error_message", exception.getMessage());
            return ResponseEntity.status(500).body(responseMap.toString());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();

        try {
            user.setUsername(request.getFirstName() + "_" + request.getLastName());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());
            user.setCreatedDate(new Date());
            userService.save(user);

            UserDetails userDetails = userDetailsService.createUserDetails(user.getUsername(), user.getPassword());
            String token = jwtTokenUtil.generateToken(userDetails);

            responseMap.put("error", false);
            responseMap.put("email", user.getEmail());
            responseMap.put("message", "account_created_successfully");
            responseMap.put("token", token);
            return ResponseEntity.ok(responseMap);
        }catch (Exception exception) {
            responseMap.put("error", exception.getMessage());
            responseMap.put("message", "internal_error");
            responseMap.put("error_message", exception.getMessage());
            return ResponseEntity.status(500).body(responseMap.toString());
        }
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable(value = "userId") Long userId,
                                            @RequestBody ChangePasswordRequest request) {
        Date currentDate = new Date();
        Map<String, Object> responseMap = new HashMap<>();
        User user = userService.findByUserId(userId);

        if (request.getPassword().equals(request.getRepeatPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            user.setActivated(true);
            user.setModifiedDate(currentDate);

            userService.save(user);

            responseMap.put("error", false);
            responseMap.put("message", "reset_password_success");
            return ResponseEntity.ok(responseMap);
        } else {
            responseMap.put("error", true);
            responseMap.put("message", "password_does_not_match");
            return ResponseEntity.status(500).body(responseMap.toString());
        }
    }

}
