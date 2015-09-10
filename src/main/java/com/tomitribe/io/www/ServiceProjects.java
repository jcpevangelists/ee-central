package com.tomitribe.io.www;

import org.asciidoctor.Asciidoctor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.asciidoctor.Asciidoctor.Factory.create;

/*
These are the projects that will be available on tomitribe.io.

https://github.com/tomitribe/crest
https://github.com/tomitribe/http-signatures-java
https://github.com/tomitribe/hodao
https://github.com/tomitribe/crest-ssh-connector
https://github.com/tomitribe/sabot
 */
@Singleton
@Startup
public class ServiceProjects {
    @Resource
    private TimerService timerService;

    private final Set<DtoProject> projects = Collections.synchronizedSet(new HashSet<DtoProject>());
    private final Asciidoctor asciidoctor = create();

    private String getText(String projectName, String documentName) {
        try {
            // TODO grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui_long_description.adoc
            final String url = "http://127.0.0.1:8080/tomitribe-io/docs/" + projectName + "/" + documentName;
            return new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new UnmanagedException(e);
        } catch (NoSuchElementException noSuchElementException) {
            return "";
        }
    }

    private DtoProject mountDtoProject(String name) {
        return new DtoProject(
                name,
                getText(name, "short_description.txt"),
                asciidoctor.render(getText(name, "long_description.adoc"), Collections.<String, Object>emptyMap()),
                "https://tests.veronezi.org/tomitribe-io/docs/" + name + "/snapshot.png", // TODO use the real path
                "https://tests.veronezi.org/tomitribe-io/docs/" + name + "/icon.png", // TODO use the real path
                asciidoctor.render(getText(name, "documentation.adoc"), Collections.<String, Object>emptyMap())
        );
    }

    @PostConstruct
    void init() {
        timerService.createTimer(0, TimeUnit.MINUTES.toMillis(1), "Update documentation timer");
    }

    @Timeout
    public void updateProjects() {
        projects.add(mountDtoProject("crest"));
        projects.add(mountDtoProject("crest-ssh-connector"));
        projects.add(mountDtoProject("hodao"));
        projects.add(mountDtoProject("http-signatures-java"));
        projects.add(mountDtoProject("sabot"));
    }

    public Set<DtoProject> getProjects() {
        return Collections.unmodifiableSet(projects);
    }

}
