package com.mdd.service;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class DevFestApplication extends Application {

    public Set<Class<?>> getClasses() {
               Set<Class<?>> s = new HashSet<Class<?>>();
               s.add(DevFestResource.class);
               return s;
           }
}
