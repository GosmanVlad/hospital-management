package com.hospital.management.controller;

import com.hospital.management.config.JwtToken;
import com.hospital.management.model.dto.auth.LoginRequest;
import com.hospital.management.model.dto.auth.RegisterRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    protected final Log logger = LogFactory.getLog(getClass());

    final AuthenticationManager authenticationManager;
    final JwtToken jwtTokenUtil;

    public AuthentificationController(AuthenticationManager authenticationManager, JwtToken jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Map<String, Object> responseMap = new HashMap<>();
        //Todo: Make the logic
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        //Todo: Make the logic
        return ResponseEntity.ok(responseMap);
    }

}
