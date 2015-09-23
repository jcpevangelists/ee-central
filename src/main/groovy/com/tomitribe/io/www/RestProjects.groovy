package com.tomitribe.io.www

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path('/projects')
@Produces('application/json')
class RestProjects {
    @Inject
    private ServiceProjects serviceProjects

    @GET
    Set<DtoProject> list() {
        serviceProjects.projects
    }
}
