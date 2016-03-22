package io.javaee

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
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

@Singleton
@Startup
@Lock(LockType.READ)
class ServiceContributors {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(60)
    public static final long FIRST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(5)

    private Logger logger = Logger.getLogger('javaeeio')

    @Resource
    private TimerService timerService

    @Inject
    private ServiceGithub github

    @PersistenceContext(unitName = 'javaeeio-pu')
    private EntityManager em

    private Set<DtoContributor> contributors = []

    @Inject
    private HttpBean http

    Timer timer

    private Set<DtoContributor> getManagedContributors() {
        new Yaml().loadAll(http.loadGithubResource(ServiceGithub.CONFIG_PROJECT, 'master', 'contributors.yaml')).collect {
            new DtoContributor(
                    login: it.login,
                    bio: it.bio,
                    googlePlus: it.google_plus,
                    twitter: it.twitter,
                    linkedin: it.linkedin
            )
        }
    }

    @PostConstruct
    void init() {
        Map<String, DtoContributor> managedContributors = getManagedContributors().collectEntries { [(it.login): it] }
        // load cache from db
        def contributions = em.createQuery('SELECT e FROM EntityContributions e').resultList.collect({ EntityContributions entityContributions ->
            new DtoContributions(
                    login: entityContributions.contributor.login,
                    project: entityContributions.project.name,
                    contributions: entityContributions.contributions
            )
        }).groupBy { it.login }
        contributors = em.createQuery('SELECT e FROM EntityContributor e').resultList.collect { EntityContributor entityContributor ->
            def dto = managedContributors.get(entityContributor.login)
            if (dto) {
                dto.name = entityContributor.name
                dto.avatarUrl = entityContributor.avatarUrl
                dto.location = entityContributor.location
                dto.company = entityContributor.company
            } else {
                dto = new DtoContributor(
                        login: entityContributor.login,
                        name: entityContributor.name,
                        company: entityContributor.company,
                        location: entityContributor.location,
                        avatarUrl: entityContributor.avatarUrl
                )
            }
            dto.contributions = contributions.get(entityContributor.login)
            dto
        }
        timer = timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load contributors timer')
    }

    @Timeout
    void update() {
        try {
            timer?.cancel()
        } catch (ignore) {
            // no-op
        }
        def githubContributors = github.contributors
        if (githubContributors == null) {
            // Not yet initialized. Try again later.
            logger.info("The github contributors list is not yet initialized. I will try again in ${FIRST_UPDATE_DELAY}ms.")
            timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load contributors timer')
            return
        }
        def contributions = (github.contributions ?: []).groupBy { it.login }
        Map<String, DtoContributor> managedContributors = getManagedContributors().collectEntries { [(it.login): it] }
        contributors = githubContributors.collect { DtoContributor dtoContributor ->
            def dto = managedContributors.get(dtoContributor.login)
            if (dto) {
                dtoContributor.googlePlus = dto.googlePlus
                dtoContributor.twitter = dto.twitter
                dtoContributor.linkedin = dto.linkedin
                dtoContributor.bio = dto.bio
            }
            dtoContributor.contributions = contributions.get(dtoContributor.login)
            dtoContributor
        }
        // schedule next update
        timer = timerService.createTimer(UPDATE_INTERVAL, 'Contributors update timer')
    }

    Set<DtoContributor> getContributors() {
        return contributors
    }
}
