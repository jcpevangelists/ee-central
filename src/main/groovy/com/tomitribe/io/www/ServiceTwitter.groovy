package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Startup
import java.text.SimpleDateFormat

@Singleton
@Startup
class ServiceTwitter {
    private final SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy")
    private List<DtoTweet> tweets = []

    @PostConstruct
    void startup() {
        tweets = new Yaml().loadAll(this.getClass().getResource('/tweets.yaml').text).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: formatter.parse(it.date).time
            )
        }
    }

    @Lock(LockType.READ)
    List<DtoTweet> getTweets() {
        Collections.unmodifiableList(tweets)
    }
}
