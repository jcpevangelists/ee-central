package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless
import javax.inject.Inject

@Stateless
class ServiceTwitter {

    @Inject
    private HttpBean http

    List<DtoTweet> getTweets() {
        new Yaml().loadAll(http.loadGithubResource('tomitribe.io.config', 'master', 'tweets.yaml')).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: Date.parse('M/d/yyyy', it.date as String).time
            )
        }
    }
}
