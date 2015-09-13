package com.tomitribe.io.www

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path("/projects")
@Produces("application/json")
class RestProjects {
    @Inject
    private ServiceProjects serviceProjects

    @Inject
    private ServiceGithub serviceGithub

    @GET
    Set<DtoProject> list() {
        serviceProjects.projects
    }

    @GET
    @Path("{name}/contributors")
    Set<DtoContributor> listContributors(@PathParam("name") String projectName) {
        serviceGithub.getContributors(projectName)
    }
}
