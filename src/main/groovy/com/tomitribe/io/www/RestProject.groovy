package com.tomitribe.io.www

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path('/project')
@Produces('application/json')
class RestProject {
    @Inject
    private ServiceProjects serviceProjects

    @GET
    @Produces('text/plain')
    @Path('/{projectName}/long_documentation')
    String get(@PathParam("projectName") String projectName) {
        serviceProjects.projects.find({
            it.name == projectName
        })?.longDocumentation
    }
}
