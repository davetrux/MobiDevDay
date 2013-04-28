package com.mdd.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


public class AuthFilter implements ContainerRequestFilter {
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {

        //Get the HTTP authorization header
        String auth = containerRequest.getHeaderValue("authorization");
        MultivaluedMap<String, String> querystring = containerRequest.getQueryParameters();
        //If the user does not provide any credentials
        if(auth == null && querystring.size() == 0){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        boolean authenticated = false;

        if(auth != null && auth.startsWith("Basic")) {

            String[] creds = BasicAuth.decode(auth);

            //If login or password fail
            if(creds == null || creds.length != 2){
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
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
