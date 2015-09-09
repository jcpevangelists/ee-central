package com.tomitribe.io.www;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("/twitter")
@Produces("application/json")
public class RestTwitter {

    @GET
    public List<DtoTweet> list() {
        final List<DtoTweet> tweets = new ArrayList<DtoTweet>();
        tweets.add(new DtoTweet(
                "Pellentesque sed velit tristique, sodales leo in, sodales magna", // text
                "mauris", // author
                0L // timestamp
        ));
        tweets.add(new DtoTweet(
                "Aliquam quis ex a mauris ornare feugiat a ac orci.", // text
                "turpis", // author
                0L // timestamp
        ));
        tweets.add(new DtoTweet(
                "Ut a mauris lectus. Aliquam rhoncus tincidunt ipsum commodo sollicitudin.", // text
                "egestas", // author
                0L // timestamp
        ));
        return tweets;
    }
}
