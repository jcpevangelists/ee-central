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
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

@Singleton
@Startup
class ServiceProjects {
    private Logger logger = Logger.getLogger(this.class.name)

    @Resource
    private TimerService timerService

    @Inject
    private ServiceGithub github

    @PersistenceContext(unitName = "tribeio-pu")
    private EntityManager em

    private Set<DtoProject> projects = []

    @PostConstruct
    @Lock(LockType.READ)
    // No need to lock it. We simply create a new instance of the projects list on every refresh.
    void init() {
        // load cache from db
        projects = em.createQuery('SELECT e FROM EntityProject e').resultList.collect { EntityProject entityProject ->
            new DtoProject(
                    name: entityProject.name,
                    shortDescription: entityProject.shortDescription,
                    longDescription: entityProject.longDescription,
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
        timerService.createTimer(
                TimeUnit.SECONDS.toMillis(projects ? 0 : 15),
                TimeUnit.MINUTES.toMillis(30),
                "Update documentation timer"
        )
    }

    @Timeout
    @Lock(LockType.READ)
    void updateProjects() {
        try {
            projects = github.projects
        } catch (issue) {
            logger.log(Level.WARNING, "Impossible to load projects from github", issue)
            return
        }
        Map<String, EntityContributor> mappedContributors = [:]
        // update cache
        projects.each { DtoProject projectBean ->
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
                    tags: projectBean.tags
            ))
        }
    }

    @Lock(LockType.READ)
    Set<DtoProject> getProjects() {
        new HashSet<DtoProject>(projects);
    }
}
