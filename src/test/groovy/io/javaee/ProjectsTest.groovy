package io.javaee

import spock.lang.Specification

import javax.ejb.TimerService
import javax.persistence.EntityManager
import javax.persistence.Query

class ProjectsTest extends Specification {


    def "init projects list"() {
        setup:
        def em = Mock(EntityManager)
        def query = Mock(Query)
        def github = Mock(ServiceGithub)
        def timerService = Mock(TimerService)

        def srv = new ServiceProjects(
                em: em,
                github: github,
                timerService: timerService
        )
        def queryResultContributor = new EntityContributor(
                login: 'cool_guy',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool guy',
                projects: []
        )
        def queryResultProject = new EntityProject(
                name: 'my-cool-project',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'http://dummy/snapshot.png',
                icon: 'http://dummy/avatar.png',
                documentation: 'my documentation',
                contributors: [queryResultContributor]
        )
        def projectDto = new DtoProject(
                name: 'my-cool-project',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'http://dummy/snapshot.png',
                icon: 'http://dummy/avatar.png',
                documentation: 'my documentation',
                contributors: [
                        new DtoContributor(
                                login: 'cool_guy',
                                avatarUrl: 'http://dummy/avatar.png',
                                name: 'cool guy',
                        )
                ]
        )

        queryResultContributor.projects << queryResultProject

        when:
        srv.init()
        def projects = srv.projects

        then:
        1 * em.createQuery('SELECT e FROM EntityProject e') >> query
        1 * query.resultList >> [queryResultProject]
        1 * timerService.createTimer(_, 'First time load documentation timer')
        projects == [projectDto]
    }

    def "update projects"() {
        setup:
        def github = Mock(ServiceGithub)
        def em = Mock(EntityManager)
        def timerService = Mock(TimerService)
        def srv = new ServiceProjects(
                github: github,
                em: em,
                timerService: timerService
        )
        def contributor = new EntityContributor(
                login: 'login',
                avatarUrl: 'avatarUrl',
                name: 'name'
        )

        when:
        srv.update()

        then:
        1 * timerService.createTimer(_, "Documentation update timer")
        1 * github.projects >> [new DtoProject(
                name: 'project-name',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'snapshot',
                icon: 'icon',
                documentation: 'documentation',
                contributors: [new DtoContributor(
                        login: 'login',
                        avatarUrl: 'avatarUrl',
                        name: 'name'
                )]
        ), new DtoProject(
                name: 'project-name-2',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'snapshot',
                icon: 'icon',
                documentation: 'documentation',
                contributors: [new DtoContributor( // this is a cached contributor
                        login: 'login',
                        avatarUrl: 'avatarUrl',
                        name: 'name'
                )]
        )]
        1 * em.merge(contributor) >> contributor
        1 * em.merge(new EntityProject(
                name: 'project-name',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'snapshot',
                icon: 'icon',
                documentation: 'documentation',
                contributors: [contributor]
        ))
        1 * em.merge(new EntityProject(
                name: 'project-name-2',
                shortDescription: 'my short description',
                longDescription: 'my long description',
                snapshot: 'snapshot',
                icon: 'icon',
                documentation: 'documentation',
                contributors: [contributor]
        ))
    }

}
