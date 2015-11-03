package com.tomitribe.io.www

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
class ServiceProjects {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(60)
    public static final long FIRST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(5)

    private Logger logger = Logger.getLogger('tribeio')

    @Resource
    private TimerService timerService

    @Inject
    private ServiceGithub github

    @PersistenceContext(unitName = 'tribeio-pu')
    private EntityManager em

    private List<DtoProject> projects = []

    private Timer timer

    @PostConstruct
    void init() {
        // load cache from db
        projects = em.createQuery('SELECT e FROM EntityProject e').resultList.collect { EntityProject entityProject ->
            new DtoProject(
                    name: entityProject.name,
                    shortDescription: entityProject.shortDescription,
                    longDescription: entityProject.longDescription,
                    friendlyName: entityProject.friendlyName,
                    snapshot: entityProject.snapshot,
                    icon: entityProject.icon,
                    documentation: entityProject.documentation,
                    contributors: entityProject.contributors.collect { EntityContributor entityContributor ->
                        new DtoContributor(
                                login: entityContributor.login,
                                name: entityContributor.name,
                                avatarUrl: entityContributor.avatarUrl
                        )
                    },
                    tags: entityProject.tags
            )
        }
        timer = timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load documentation timer')
    }

    @Timeout
    @Lock(LockType.READ)
    void update() {
        try {
            timer?.cancel()
        } catch (ignore) {
            // no-op
        }
        def githubProjects = github.projects
        def githubContributions = github.contributions
        if (githubProjects == null) {
            // Not yet initialized. Try again later.
            logger.info("The github projects list is not yet initialized. I will try again in ${FIRST_UPDATE_DELAY}ms.")
            timerService.createTimer(FIRST_UPDATE_DELAY, 'First time load documentation timer')
            return
        }
        Map<String, EntityContributor> mappedContributors = [:]
        // update cache
        githubProjects.each { DtoProject projectBean ->
            Set<EntityContributor> contributors = projectBean.contributors.collect { DtoContributor contributorBean ->
                EntityContributor contributor = mappedContributors.get(contributorBean.login) ?: em.merge(new EntityContributor(
                        login: contributorBean.login,
                        name: contributorBean.name,
                        avatarUrl: contributorBean.avatarUrl
                ))
                mappedContributors << [(contributorBean.login): contributor]
                contributor
            }
            em.merge(new EntityProject(
                    name: projectBean.name,
                    shortDescription: projectBean.shortDescription,
                    longDescription: projectBean.longDescription,
                    snapshot: projectBean.snapshot,
                    icon: projectBean.icon,
                    documentation: projectBean.documentation,
                    contributors: contributors,
                    tags: projectBean.tags,
                    friendlyName: projectBean.friendlyName
            ))
        }
        githubContributions.each { DtoContributions contributions ->
            em.merge(new EntityContributions(
                    contributions: contributions.contributions,
                    project: em.find(EntityProject, contributions.project),
                    contributor: em.find(EntityContributor, contributions.login),
            ))
        }
        projects = githubProjects
        // schedule next update
        timer = timerService.createTimer(UPDATE_INTERVAL, 'Documentation update timer')
    }

    @Lock(LockType.READ)
    List<DtoProject> getProjects() {
        projects;
    }
}
