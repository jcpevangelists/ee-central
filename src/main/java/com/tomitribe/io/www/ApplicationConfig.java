package com.tomitribe.io.www;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> clsSet = new HashSet<Class<?>>();
        clsSet.add(RestProjects.class);
        clsSet.add(RestTwitter.class);
        return clsSet;
    }
}
