package com.tomitribe.io.www;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/twitter")
@Produces("application/json")
public class RestTwitter {

    @Inject
    ServiceTwitter serviceTwitter;

    @GET
    public List<DtoTweet> list() {
        return serviceTwitter.getTweets();
    }
}
