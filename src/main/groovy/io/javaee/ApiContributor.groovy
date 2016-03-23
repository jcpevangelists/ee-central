package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/contributor')
class ApiContributor {

    @Inject
    private ServiceProject srv

    @GET
    List<DtoContributor> list() {
        return srv.allContributors
    }

}
