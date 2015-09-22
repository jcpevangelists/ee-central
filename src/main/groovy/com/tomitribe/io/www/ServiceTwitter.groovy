package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless

@Stateless
class ServiceTwitter {

    List<DtoTweet> getTweets() {
        new Yaml().loadAll(getClass().getResource('/tweets.yaml').text).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: Date.parse('M/d/yyyy', it.date as String).time
            )
        }
    }
}
