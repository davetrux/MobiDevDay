package com.mdd.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


public class AuthFilter implements ContainerRequestFilter {
    private static final Logger log = Logger.getLogger(AuthFilter.class.getName());

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {

        //Get the HTTP authorization header
        String auth = containerRequest.getHeaderValue("authorization");

        log.warning(String.format("Header: %s", auth));

        MultivaluedMap<String, String> querystring = containerRequest.getQueryParameters();
        //If the user does not provide any credentials
        if(auth == null && querystring.size() == 0){
            log.severe("No authentication info found");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        boolean authenticated = false;

        if(auth != null && auth.startsWith("Basic")) {

            String[] creds = BasicAuth.decode(auth);

            //If login or password fail
            if(creds == null || creds.length != 2){
                log.severe("Invalid basic auth credentials - missing id or password");
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            //Really should check from an actual user store here
            if("mdd".equalsIgnoreCase(creds[0]) && creds[1].equals("password123")) {
                authenticated = true;
            }
        } else if(!querystring.isEmpty()){

            if(querystring.containsKey("token")) {
                String token = querystring.getFirst("token");

                Key clientKey = new Key();

                Checker validator = new Checker(new String[]{clientKey.getGoogleKey()}, clientKey.getAudience());

                GoogleIdToken.Payload payload = validator.check(token);

                if(payload == null) {
                    String error = validator.getProblem();
                    log.severe(error);
                    authenticated = false;
                } else {
                    authenticated = true;
                }
            }
        }

        if(authenticated) {
            return containerRequest;
        }
        else {
            log.severe("Authentication failed");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
