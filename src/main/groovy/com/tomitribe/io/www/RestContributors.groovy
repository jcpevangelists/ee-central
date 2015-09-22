package com.tomitribe.io.www

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path('/contributors')
@Produces('application/json')
class RestContributors {
    @Inject
    private ServiceContributors serviceContributors

    @GET
    Set<DtoContributor> list() {
        serviceContributors.contributors
    }

}
