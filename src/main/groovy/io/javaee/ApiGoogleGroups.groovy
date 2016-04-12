package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/googlegroups')
class ApiGoogleGroups {

    @Inject
    private ServiceGoogleGroups srv

    @GET
    @Path('/messages')
    Set<DtoGroupMessage> listMessages() {
        return srv.listMessages()
    }

}
