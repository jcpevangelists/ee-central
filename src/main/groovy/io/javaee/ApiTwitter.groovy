package io.javaee

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Produces('application/json')
@Path('/twitter')
class ApiTwitter {

    @Inject
    private ServiceTwitter srv

    @GET
    Set<DtoTweet> list() {
        return srv.getTweets()
    }

}
