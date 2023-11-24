package com.mercadolibro.service.impl;

import com.mercadolibro.dto.AuthResp;
import com.mercadolibro.dto.UserDTO;
import com.mercadolibro.dto.UserRegisterDTO;
import com.mercadolibro.dto.mapper.UserMapper;
import com.mercadolibro.entity.AppUser;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.exception.UnauthorizedException;
import com.mercadolibro.repository.AppUserRepository;
import com.mercadolibro.security.OauthProviders;
import com.mercadolibro.service.OauthService;
import com.mercadolibro.service.UserService;
import com.mercadolibro.util.JwtUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OauthServiceImpl implements OauthService {

    private final Map<String, OauthProviders.OatuhProvider> oauthProviders;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    public OauthServiceImpl(OauthProviders oauthProviders, RestTemplate restTemplate, JwtUtil jwtUtil, AppUserRepository appUserRepository, UserMapper userMapper, UserService userService) {
        this.oauthProviders = oauthProviders.getProviders();
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    private String handleAuthorizationCode(String authorizationCode, String provider) throws UnauthorizedException {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(oauthProviders.get(provider).getTokenEndpoint())
                .queryParam("code", authorizationCode)
                .queryParam("client_id", oauthProviders.get(provider).getClientId())
                .queryParam("client_secret", oauthProviders.get(provider).getClientSecret())
                .queryParam("redirect_uri", oauthProviders.get(provider).getRedirectUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("state", provider)
                .build(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response;

        try {
            response = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.POST, request, Map.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid authorization code");
        }
        String idToken = (String) response.getBody().get("id_token");
        if (idToken == null) {
            throw new UnauthorizedException("Invalid authorization code");
        }
        System.out.println("idToken: " + idToken);
        return idToken;
    }

    @Override
    public AuthResp loginWithAuthorizationCode(String provider, String authorizationCode) throws UnauthorizedException, ResourceAlreadyExistsException, ResourceNotFoundException {
        if (!oauthProviders.containsKey(provider)) {
            throw new ResourceNotFoundException("Provider not supported");
        }
        Map<String, String> claims = jwtUtil.getClaimsFromToken(handleAuthorizationCode(authorizationCode, provider));
        String email = claims.get("email");
        String givenName = claims.get("given_name");
        String familyName = claims.get("family_name");
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent()) {
            return new AuthResp(jwtUtil.generateToken(optionalAppUser.get()), userMapper.toUserDTO(optionalAppUser.get()));
        }
        String randomPassword = UUID.randomUUID().toString();
        UserDTO userDTO = userService.create(new UserRegisterDTO(givenName, familyName, email, randomPassword));
        return new AuthResp(jwtUtil.generateToken(userMapper.toAppUser(userDTO)), userDTO);
    }

    @Override
    public String loginUrl(String provider) throws ResourceNotFoundException {
        if (!oauthProviders.containsKey(provider)) {
            throw new ResourceNotFoundException("Provider not supported");
        }
        return oauthProviders.get(provider).getAuthorizationEndpoint() +
                "?client_id=" + oauthProviders.get(provider).getClientId() +
                "&redirect_uri=" + oauthProviders.get(provider).getRedirectUri() +
                "&response_type=code" +
                "&state=" + provider +
                "&scope=" + oauthProviders.get(provider).getScope().replace(" ", "%20") +
                "&response_mode=query";
    }
}
