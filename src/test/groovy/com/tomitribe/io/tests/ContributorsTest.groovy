package com.tomitribe.io.tests

import com.tomitribe.io.www.DtoContributor
import com.tomitribe.io.www.EntityContributions
import com.tomitribe.io.www.EntityContributor
import com.tomitribe.io.www.HttpBean
import com.tomitribe.io.www.ServiceContributors
import com.tomitribe.io.www.ServiceGithub
import spock.lang.Specification

import javax.ejb.TimerService
import javax.persistence.EntityManager
import javax.persistence.Query
import java.nio.charset.StandardCharsets

class ContributorsTest extends Specification {


    def "init contributors list"() {
        setup:
        def em = Mock(EntityManager)
        def query = Mock(Query)
        def queryContributions = Mock(Query)
        def github = Mock(ServiceGithub)
        def timerService = Mock(TimerService)
        def http = Mock(HttpBean)

        def srv = new ServiceContributors(
                em: em,
                github: github,
                http: http,
                timerService: timerService
        )
        def queryResultContributor = [new EntityContributor(
                login: 'cool_guy',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool guy',
        ), new EntityContributor(
                login: 'cool_but_unmanaged',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool but unmanaged',
        )]
        when:
        srv.init()
        def contributors = srv.contributors

        then:
        1 * http.loadGithubResource('tomitribe.io.config', 'master', 'contributors.yaml') >>
                this.getClass().getResource('/contributors.yaml').getText(StandardCharsets.UTF_8.name())
        1 * em.createQuery('SELECT e FROM EntityContributor e') >> query
        1 * em.createQuery('SELECT e FROM EntityContributions e') >> queryContributions
        1 * queryContributions.resultList >> []
        1 * query.resultList >> queryResultContributor
        1 * timerService.createTimer(_, 'First time load contributors timer')
        contributors == [new DtoContributor(
                login: 'cool_guy',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool guy',
                bio: 'I love computers!'
        ), new DtoContributor(
                login: 'cool_but_unmanaged',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool but unmanaged'
        )] as Set
    }

    def "update contributors"() {
        setup:
        def github = Mock(ServiceGithub)
        def timerService = Mock(TimerService)
        def http = Mock(HttpBean)
        def srv = new ServiceContributors(
                github: github,
                timerService: timerService,
                http: http
        )

        when:
        srv.updateContributors()

        then:
        1 * http.loadGithubResource('tomitribe.io.config', 'master', 'contributors.yaml') >>
                this.getClass().getResource('/contributors.yaml').getText(StandardCharsets.UTF_8.name())
        1 * timerService.createTimer(_, "Contributors update timer")
        1 * github.contributors >> [new DtoContributor(
                login: 'cool_guy',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool guy'
        ), new DtoContributor(
                login: 'cool_but_unmanaged',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool but unmanaged'
        )]
        srv.contributors == [new DtoContributor(
                login: 'cool_guy',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool guy',
                bio: 'I love computers!'
        ), new DtoContributor(
                login: 'cool_but_unmanaged',
                avatarUrl: 'http://dummy/avatar.png',
                name: 'cool but unmanaged'
        )] as Set
    }

}
