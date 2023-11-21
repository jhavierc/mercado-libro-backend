package com.mercadolibro.service;

import com.mercadolibro.dto.AuthResp;
import com.mercadolibro.exception.ResourceAlreadyExistsException;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.exception.UnauthorizedException;

public interface OauthService {
    /**
     * Handles the authorization code flow.
     * @param provider the provider to use e.g. google, facebook, etc. (must be supported by the application)
     * @param authorizationCode the authorization code received from the provider
     * @return the token and user information
     */
    AuthResp loginWithAuthorizationCode(String provider, String authorizationCode) throws UnauthorizedException, ResourceAlreadyExistsException, ResourceNotFoundException;

    /**
     * Returns the URL to redirect the user to in order to start the OAuth2 flow
     * @param provider the provider to use e.g. google, facebook, etc. (must be supported by the application)
     * @return the URL to redirect the user to
     */
    String loginUrl(String provider) throws ResourceNotFoundException;
}
