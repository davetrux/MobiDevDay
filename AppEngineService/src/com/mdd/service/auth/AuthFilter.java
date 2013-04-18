package com.mdd.service.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class AuthFilter implements ContainerRequestFilter {
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {

        //Get the HTTP authorization header
        String auth = containerRequest.getHeaderValue("authorization");

        //If the user does not provide any credentials
        if(auth == null){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String[] creds = BasicAuth.decode(auth);

        //If login or password fail
        if(creds == null || creds.length != 2){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        //Really should check from an actual user store here
        if("mdd".equalsIgnoreCase(creds[0]) && creds[1].equals("password123"))
            return containerRequest;
        else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
