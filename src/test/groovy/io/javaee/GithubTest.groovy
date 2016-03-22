package io.javaee

import groovy.json.JsonOutput
import spock.lang.Specification

import javax.ejb.TimerService
import java.nio.charset.StandardCharsets

class GithubTest extends Specification {
    def restReposPrefix = HttpBean.BASE_URL + '/repos/jcpevangelists'

    def "getting the list of contributors"() {
        setup:
        def http = Mock(HttpBean)
        def projectName = 'myproject'
        def srv = new ServiceGithub(
                http: http
        )
        def contributorsJson = JsonOutput.toJson([[login: "my_user", avatar_url: 'http://dummy/avatar.png']])

        when:
        def contributors = srv.getContributors(projectName)

        then:
        http.getUrlContentWithToken("$restReposPrefix/$projectName/contributors") >> contributorsJson
        http.getUrlContentWithToken("${HttpBean.BASE_URL}/users/my_user") >> '{"name": "my user name"}'
        contributors.size() == 1
        contributors[0].contributor == new DtoContributor(
                name: 'my user name',
                login: 'my_user',
                avatarUrl: 'http://dummy/avatar.png'
        )
    }

    def "getting the list of projects"() {
        setup:
        def http = Mock(HttpBean)
        def timerService = Mock(TimerService)
        def srv = new ServiceGithub(
                http: http,
                timerService: timerService
        )

        when:
        srv.update()
        def projects = srv.getProjects()

        then:

        http.loadGithubResource('javaee.io.config', 'master', 'published_docs.yaml') >>
                this.getClass().getResource('/published_docs.yaml').getText(StandardCharsets.UTF_8.name())

        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project-no-release") >> JsonOutput.toJson([name: 'my-cool-project-no-release'])
        http.getUrlContentWithToken(" ${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project-no-listed-tag") >> JsonOutput.toJson([name: 'my-cool-project-no-listed-tag'])

        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project-no-long-description") >> JsonOutput.toJson([name: 'my-cool-project-no-long-description'])
        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project-no-long-description/tags") >> JsonOutput.toJson([[name: "v0.0.1"]])
        http.loadGithubResourceEncoded('javaee.io.config', 'master', "docs/my-cool-project-no-long-description/snapshot.png") >> 'my_snapshot.png'
        http.loadGithubResourceEncoded('javaee.io.config', 'master', "docs/my-cool-project-no-long-description/icon.png") >> 'my_icon.png'
        http.loadGithubResourceHtml('javaee.io.config', 'master', "docs/my-cool-project-no-long-description/documentation.adoc") >> '<p>my_documentation</p>'
        http.loadGithubResource('javaee.io.config', 'master', "docs/my-cool-project-no-long-description/short_description.txt") >> 'my_short_description'
        // no long description -> http.loadGithubResourceHtml('javaee.io.config', 'master', "docs/my-cool-project-no-long-description/long_description.adoc") >> '<p>my_long_description</p>'

        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project") >> JsonOutput.toJson([name: 'my-cool-project'])
        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project/tags") >> JsonOutput.toJson([[name: "v0.0.1"], [name: "v0.0.1-beta"]])
        http.loadGithubResourceEncoded('javaee.io.config', 'master', "docs/my-cool-project/snapshot.png") >> 'my_snapshot.png'
        http.loadGithubResourceEncoded('javaee.io.config', 'master', "docs/my-cool-project/icon.png") >> 'my_icon.png'
        http.loadGithubResourceHtml('javaee.io.config', 'master', "docs/my-cool-project/long_description.adoc") >> '<p>my_long_description</p>'
        http.loadGithubResourceHtml('javaee.io.config', 'master', "docs/my-cool-project/documentation.adoc") >> '<p>my_documentation</p>'
        http.loadGithubResource('javaee.io.config', 'master', "docs/my-cool-project/short_description.txt") >> 'my_short_description'
        http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/jcpevangelists/my-cool-project/contributors") >> getClass().getResource('/contributors.json').getText(StandardCharsets.UTF_8.name())
        http.getUrlContentWithToken("${HttpBean.BASE_URL}/users/my_login") >> getClass().getResource('/my_login_contributor.json').getText(StandardCharsets.UTF_8.name())

        projects.size() == 1
        projects[0] == new DtoProject(
                name: 'my-cool-project',
                shortDescription: 'my_short_description',
                longDescription: '<p>my_long_description</p>',
                snapshot: 'my_snapshot.png',
                icon: 'my_icon.png',
                documentation: '<p>my_documentation</p>',
                contributors: [
                        new DtoContributor(
                                login: 'my_login',
                                avatarUrl: 'my_avatar_url',
                                name: 'my_name',
                                company: 'my_company',
                                location: 'my_location',
                        )
                ],
                tags: ['v0.0.1']
        )

    }

    def "getting sorted list of projects"() {
        setup:
        def srv = new ServiceGithub()

        when:
        def projects = srv.sortMyConfigFile(
                [[project: 'z'], [project: 'a'], [project: 'b']],
                [[name: 'b'], [name: 'a'], [name: 'z']]
        ).collect { it.name }

        then:
        projects == ['z', 'a', 'b']
    }
}
