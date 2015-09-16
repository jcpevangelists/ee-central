package com.tomitribe.io.www

import org.yaml.snakeyaml.Yaml

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.ejb.Timeout
import javax.ejb.TimerService
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

@Singleton
@Startup
@Lock(LockType.READ)
class ServiceContributors {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(5)
    public static final long FIRST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(5)

    private Logger logger = Logger.getLogger(this.class.name)

    @Resource
    private TimerService timerService

    @Inject
    private ServiceGithub github

    @PersistenceContext(unitName = "tribeio-pu")
    private EntityManager em

    private Set<DtoContributor> contributors = []

    private Set<DtoContributor> getManagedContributors() {
        new Yaml().loadAll(this.getClass().getResource('/contributors_bio.yaml').text).collect {
            new DtoContributor(
                    login: it.login,
                    title: it.title,
                    bio: it.bio
            )
        }
    }

    @PostConstruct
    void init() {
        Map<String, DtoContributor> managedContributors = getManagedContributors().collectEntries { [(it.login): it] }
        // load cache from db
        contributors = em.createQuery('SELECT e FROM EntityContributor e').resultList.collect { EntityContributor entityContributor ->
            def dto = managedContributors.get(entityContributor.login)
            if (dto) {
                dto.name = entityContributor.name
                dto.avatarUrl = entityContributor.avatarUrl
            } else {
                dto = new DtoContributor(
                        login: entityContributor.login,
                        name: entityContributor.name,
                        avatarUrl: entityContributor.avatarUrl
                )
            }
            dto
        }
        timerService.createTimer(FIRST_UPDATE_DELAY, "First time load contributors timer")
    }

    @Timeout
    void updateContributors() {
        def githubContributors = github.contributors
        if (githubContributors == null) {
            // Not yet initialized. Try again later.
            logger.info("The github contributors list is not yet initialized. I will try again in ${FIRST_UPDATE_DELAY}ms.")
            timerService.createTimer(FIRST_UPDATE_DELAY, "First time load contributors timer")
            return
        }
        Map<String, DtoContributor> managedContributors = getManagedContributors().collectEntries { [(it.login): it] }
        contributors = githubContributors.collect { DtoContributor dtoContributor ->
            def dto = managedContributors.get(dtoContributor.login)
            if (dto) {
                dtoContributor.title = dto.title
                dtoContributor.bio = dto.bio
            }
            dtoContributor
        }
        // schedule next update
        timerService.createTimer(UPDATE_INTERVAL, "Contributors update timer")
    }

    Set<DtoContributor> getContributors() {
        return contributors
    }
}
