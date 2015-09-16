package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Stateless
import java.text.SimpleDateFormat

@Stateless
class ServiceTwitter {
    private final SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy")

    @Lock(LockType.READ)
    List<DtoTweet> getTweets() {
        new Yaml().loadAll(this.getClass().getResource('/tweets.yaml').text).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: formatter.parse(it.date).time
            )
        }
    }
}
