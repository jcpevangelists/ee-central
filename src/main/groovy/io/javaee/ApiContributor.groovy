package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/contributor')
class ApiContributor {

    @Inject
    private ServiceProject project

    @Inject
    private ServiceGithub github

    @GET
    List<DtoContributor> list() {
        return project.allContributors
    }

    @GET
    @Path('/{login}')
    DtoContributorInfo get(@PathParam("login") String login) {
        return github.getContributor(login)
    }

}
