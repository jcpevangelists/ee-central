package com.tomitribe.io.www

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/twitter")
@Produces("application/json")
class RestTwitter {
    @Inject
    private ServiceTwitter serviceTwitter

    @GET
    List<DtoTweet> list() {
        serviceTwitter.tweets
    }
}
