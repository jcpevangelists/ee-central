package com.tomitribe.io.www;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
@Startup
public class ServiceTwitter {
    private final List<DtoTweet> tweets = Collections.synchronizedList(new ArrayList<DtoTweet>());

    @PostConstruct
    void startup() {
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
    }

    public List<DtoTweet> getTweets() {
        return Collections.unmodifiableList(tweets);
    }
}
