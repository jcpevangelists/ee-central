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
        adocOptions.put("doctype", "inline"); // how to remove header? I just want the parent div to be rendered. TODO
        final String adocText = new Scanner(new URL("https://github.com/tomitribe/crest/blob/master/README.adoc").openStream(), "UTF-8").useDelimiter("\\A").next();
        projects.add(new DtoProject(
                "crest", //name
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus ante diam, dapibus id vehicula auctor, blandit in leo. Maecenas dolor felis, gravida non dapibus ac, euismod vitae tortor.", //shortDescription
                // grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui_long_description.adoc
                asciidoctor.convert(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "http-signatures-java", //name
                "Morbi ornare dictum placerat. Donec at est erat. Vivamus ac vulputate nulla. Fusce id viverra purus. Proin vestibulum quam eget risus facilisis ornare.", //shortDescription
                // grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui.adoc
                asciidoctor.convert(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "hoao", //name
                "Nulla rutrum diam placerat malesuada consequat. Nam nec justo non justo pretium rhoncus.", //shortDescription
                // grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui.adoc
                asciidoctor.convert(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "crest-ssh-connector", //name
                "Curabitur porta ex id risus dictum congue mollis at odio. Mauris sed arcu scelerisque tortor ultricies sollicitudin in in ipsum.", //shortDescription
                // grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui.adoc
                asciidoctor.convert(adocText, adocOptions)
        ));
        projects.add(new DtoProject(
                "sabot", //name
                "Curabitur laoreet arcu ut metus bibendum, eget semper justo dignissim. Suspendisse quis justo efficitur, mattis dui in, dignissim tellus.", //shortDescription
                // grab the string from https://raw.githubusercontent.com/tomitribe/<PROJECT_NAME>/master/tomitribe_ui.adoc
                asciidoctor.convert(adocText, adocOptions)
        ));
    }

    public List<DtoProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

}
