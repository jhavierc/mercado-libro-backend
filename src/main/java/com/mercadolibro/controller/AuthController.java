package com.mercadolibro.controller;

import com.mercadolibro.dto.AuthReq;
import com.mercadolibro.dto.AuthResp;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.exception.UnauthorizedException;
import com.mercadolibro.service.OauthService;
import com.mercadolibro.service.UserService;
import com.mercadolibro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final OauthService oauthService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, OauthService oauthService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.oauthService = oauthService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResp> createToken(@RequestBody AuthReq request) throws ResourceNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
            UserDTO user = userService.findByEmail(userDetails.getUsername());
            String jwt = jwtUtil.generateToken(user);
            return new ResponseEntity<>(new AuthResp(jwt, user), HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "Refresh token", notes = "Returns the user", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<AuthResp> refreshToken(HttpServletRequest request) throws ResourceNotFoundException {
        String token = request.getHeader("Authorization");
        if (token == null ) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDTO user = userService.findByEmail(email);
        return new ResponseEntity<>(new AuthResp(jwt, user), HttpStatus.OK);
    }

    @GetMapping("/oauth/{provider}")
    @ApiOperation(
            value = "Get the URL to redirect the user to in order to start the OAuth2 flow",
            notes = "Returns the URL to redirect the user to"
    )
    public ResponseEntity<String> loginUrl(@ApiParam(name = "provider", value = "Supported google or facebook", required = true, defaultValue = "google") @PathVariable String provider) throws ResourceNotFoundException {
        return ResponseEntity.ok(oauthService.loginUrl(provider));
    }

    @PostMapping("/oauth/{provider}")
    @ApiOperation(
            value = "Login with OAuth2",
            notes = "Returns the user")

    public ResponseEntity<AuthResp> google(@ApiParam(name = "code", value = "The code returned by the OAuth2 provider", required = true) @RequestParam String code, @ApiParam(name = "provider", value = "Supported google or facebook", required = true, example = "google", defaultValue = "google") @PathVariable String provider) throws ResourceNotFoundException, UnauthorizedException, ResourceAlreadyExistsException {
        return new ResponseEntity<>(oauthService.loginWithAuthorizationCode(provider, code), HttpStatus.OK);
    }

}
