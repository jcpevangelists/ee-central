package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Produces('application/json')
@Path('/project')
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

    @GET
    @Path('/raw/{projectOwner}/{projectName}/{projectResource : .+}')
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response getFile(@PathParam("projectOwner") String projectOwner, @PathParam("projectName") String projectName,
                     @PathParam("projectResource") String projectResource) {
        byte[] data = srv.getRaw("${projectOwner}/${projectName}", projectResource)
        return Response.ok(data).build()
    }

}
