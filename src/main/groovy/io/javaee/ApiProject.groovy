package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path('/project')
@Produces('application/json')
class ApiProject {

    @Inject
    private ServiceProject srv

    @GET
    List<DtoProjectInfo> list() {
        return srv.availableProjects
    }

    @GET
    @Path('/{projectOwner}/{projectName}')
    DtoProjectDetail get(@PathParam("projectOwner") String projectOwner, @PathParam("projectName") String projectName) {
        return srv.getDetails("${projectOwner}/${projectName}")
    }

    @GET
    @Path('/{projectOwner}/{projectName}/{projectResource : .+}')
    DtoProjectPage getPage(@PathParam("projectOwner") String projectOwner, @PathParam("projectName") String projectName,
                           @PathParam("projectResource") String projectResource) {
        return new DtoProjectPage(
                name: projectResource,
                content: srv.getPage("${projectOwner}/${projectName}", projectResource)
        )
    }

}
