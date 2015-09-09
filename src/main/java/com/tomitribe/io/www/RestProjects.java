package com.tomitribe.io.www;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("/projects")
@Produces("application/json")
public class RestProjects {

    @GET
    public List<DtoProject> list() {
        /*
        https://github.com/tomitribe/crest
        https://github.com/tomitribe/http-signatures-java
        https://github.com/tomitribe/hodao
        https://github.com/tomitribe/crest-ssh-connector
        https://github.com/tomitribe/sabot
         */
        final List<DtoProject> projects = new ArrayList<DtoProject>();
        projects.add(new DtoProject(
                "crest", //name
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus ante diam, dapibus id vehicula auctor, blandit in leo. Maecenas dolor felis, gravida non dapibus ac, euismod vitae tortor." //shortDescription
        ));
        projects.add(new DtoProject(
                "http-signatures-java", //name
                "Morbi ornare dictum placerat. Donec at est erat. Vivamus ac vulputate nulla. Fusce id viverra purus. Proin vestibulum quam eget risus facilisis ornare." //shortDescription
        ));
        projects.add(new DtoProject(
                "hoao", //name
                "Nulla rutrum diam placerat malesuada consequat. Nam nec justo non justo pretium rhoncus." //shortDescription
        ));
        projects.add(new DtoProject(
                "crest-ssh-connector", //name
                "Curabitur porta ex id risus dictum congue mollis at odio. Mauris sed arcu scelerisque tortor ultricies sollicitudin in in ipsum." //shortDescription
        ));
        projects.add(new DtoProject(
                "sabot", //name
                "Curabitur laoreet arcu ut metus bibendum, eget semper justo dignissim. Suspendisse quis justo efficitur, mattis dui in, dignissim tellus." //shortDescription
        ));
        return projects;
    }
}
