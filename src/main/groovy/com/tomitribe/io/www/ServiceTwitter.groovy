package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Stateless

@Stateless
class ServiceTwitter {

    @Lock(LockType.READ)
    List<DtoTweet> getTweets() {
        new Yaml().loadAll(this.getClass().getResource('/tweets.yaml').text).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: Date.parse('M/d/yyyy', it.date as String).time
            )
        }
    }
}
