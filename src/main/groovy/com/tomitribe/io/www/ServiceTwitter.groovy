package com.tomitribe.io.www

import javax.annotation.PostConstruct
import javax.ejb.Singleton

@Singleton
class ServiceTwitter {
    private final List<DtoTweet> tweets = Collections.synchronizedList([])

    @PostConstruct
    void startup() {
        tweets << new DtoTweet(
                text: "Pellentesque sed velit tristique, sodales leo in, sodales magna",
                author: "mauris",
                timestamp: 0L
        )
        tweets << new DtoTweet(
                text: "Aliquam quis ex a mauris ornare feugiat a ac orci.",
                author: "turpis",
                timestamp: 0L
        )
        tweets << new DtoTweet(
                text: "Ut a mauris lectus. Aliquam rhoncus tincidunt ipsum commodo sollicitudin.",
                author: "egestas",
                timestamp: 0L
        )
    }

    List<DtoTweet> getTweets() {
        Collections.unmodifiableList(tweets)
    }
}
