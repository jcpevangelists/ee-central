package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.ejb.Timeout
import javax.ejb.Timer
import javax.ejb.TimerService
import javax.inject.Inject
import java.util.concurrent.TimeUnit

@Singleton
@Startup
@Lock(LockType.READ)
class ServiceTwitter {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(60)
    public static final long FIRST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(5)

    @Resource
    private TimerService timerService


    @Inject
    private HttpBean http

    private List<DtoTweet> tweets = []

    private Timer timer

    @PostConstruct
    void init() {
        timer = timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load tweets timer')
    }

    @Timeout
    void update() {
        try {
            timer?.cancel()
        } catch (ignore) {
            // no-op
        }
        tweets = new Yaml().loadAll(http.loadGithubResource(ServiceGithub.CONFIG_PROJECT, 'master', 'tweets.yaml')).collect {
            new DtoTweet(
                    text: it.text,
                    author: it.author,
                    timestamp: Date.parse('M/d/yyyy', it.date as String).time
            )
        }
        // schedule next update
        timer = timerService.createTimer(UPDATE_INTERVAL, 'Tweets update timer')
    }

    List<DtoTweet> getTweets() {
        tweets
    }
}
