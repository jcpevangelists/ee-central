package com.tomitribe.io.www

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.ejb.Timeout
import javax.ejb.TimerService
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

@Singleton
@Startup
@Lock(LockType.READ)
class ServicePictures {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(5)
    public static final long FIRST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(5)

    private Logger logger = Logger.getLogger(this.class.name)

    @Resource
    private TimerService timerService

    private Set<DtoPicture> pictures = []
    private Map<String, DtoPicture> picturesMap = [:]

    @Inject
    private HttpBean http

    @PostConstruct
    void init() {
        timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load pictures timer')
    }

    @Timeout
    void update() {
        pictures = http.loadGithubResourceNames('tomitribe.io.config', 'master', 'pics').collect {
            def dto = new DtoPicture(
                    name: it,
                    content: http.loadGithubResourceEncoded('tomitribe.io.config', 'master', "pics/$it")
            )
            picturesMap.put(it, dto)
            dto
        }
        // schedule next update
        timerService.createTimer(UPDATE_INTERVAL, 'Pictures update timer')
    }

    Set<DtoPicture> getPictures() {
        return pictures
    }

    DtoPicture getPictureByName(String name) {
        return picturesMap.get(name)
    }
}
