package com.tomitribe.io.www;

import org.asciidoctor.Asciidoctor;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    private final List<DtoProject> projects = Collections.synchronizedList(new ArrayList<DtoProject>());

    @PostConstruct
    void startup() throws IOException {
        final Asciidoctor asciidoctor = create();
        final Map<String, Object> adocOptions = new HashMap<String, Object>();
        // TODO grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui_long_description.adoc
        final String adocText = new Scanner(ServiceProjects.class.getResourceAsStream("/docs/crest/long_description.adoc"), "UTF-8").useDelimiter("\\A").next();
        projects.add(new DtoProject(
                "crest", //name
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus ante diam, dapibus id vehicula auctor, blandit in leo. Maecenas dolor felis, gravida non dapibus ac, euismod vitae tortor.", //shortDescription
                asciidoctor.render(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "http-signatures-java", //name
                "Morbi ornare dictum placerat. Donec at est erat. Vivamus ac vulputate nulla. Fusce id viverra purus. Proin vestibulum quam eget risus facilisis ornare.", //shortDescription
                asciidoctor.render(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "hoao", //name
                "Nulla rutrum diam placerat malesuada consequat. Nam nec justo non justo pretium rhoncus.", //shortDescription
                asciidoctor.render(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "crest-ssh-connector", //name
                "Curabitur porta ex id risus dictum congue mollis at odio. Mauris sed arcu scelerisque tortor ultricies sollicitudin in in ipsum.", //shortDescription
                asciidoctor.render(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "sabot", //name
                "Curabitur laoreet arcu ut metus bibendum, eget semper justo dignissim. Suspendisse quis justo efficitur, mattis dui in, dignissim tellus.", //shortDescription
                asciidoctor.render(adocText, adocOptions)
        ));
    }

    public List<DtoProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

}
