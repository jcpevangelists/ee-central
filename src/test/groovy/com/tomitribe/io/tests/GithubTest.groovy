package com.tomitribe.io.tests

import com.tomitribe.io.www.DtoContributor
import com.tomitribe.io.www.DtoProject
import com.tomitribe.io.www.HttpBean
import com.tomitribe.io.www.ServiceGithub
import groovy.json.JsonOutput
import org.asciidoctor.Asciidoctor
import spock.lang.Specification

import javax.ejb.TimerService
import java.nio.charset.StandardCharsets

class GithubTest extends Specification {
    def rawUrlPrefix = 'https://raw.githubusercontent.com/tomitribe'
    def restReposPrefix = 'https://api.github.com/repos/tomitribe'

    def "getting the list of contributors"() {
        setup:
        def http = Mock(HttpBean)
        def token = 'my_secret'
        def projectName = 'myproject'
        def srv = new ServiceGithub(
                token: token,
                http: http
        )
        def contributorsJson = JsonOutput.toJson([[login: "my_user", avatar_url: 'http://dummy/avatar.png']])

        when:
        def contributors = srv.getContributors(projectName)

        then:
        http.getUrlContent("$restReposPrefix/$projectName/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("https://api.github.com/users/my_user?access_token=$token") >> '{"name": "my user name"}'
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
        def token = 'my_secret'
        def asciidoctor = Mock(Asciidoctor)
        def timerService = Mock(TimerService)
        def srv = new ServiceGithub(
                token: token,
                http: http,
                asciidoctor: asciidoctor,
                timerService: timerService
        )

        def projectList = [[name: 'my-cool-project'], [name: 'my_bad_project']]
        def projectJson = JsonOutput.toJson(projectList)

        when:
        srv.update()
        def projects = srv.getProjects()

        then:

        http.loadGithubResource('tomitribe.io.config', 'master', 'published_docs.yaml') >>
                this.getClass().getResource('/published_docs.yaml').getText(StandardCharsets.UTF_8.name())

        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=1&per_page=20&access_token=$token") >> projectJson
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=2&per_page=20&access_token=$token") >> '[]'

        http.getUrlContent("https://api.github.com/repos/tomitribe/my-cool-project/tags?access_token=$token") >> JsonOutput.toJson([[name: "v0.0.1"], [name: "v0.0.1-beta"]])

        http.loadGithubResourceEncoded('tomitribe.io.config', 'master', "docs/my-cool-project/snapshot.png") >> 'my_snapshot.png'
        http.loadGithubResourceEncoded('tomitribe.io.config', 'master', "docs/my-cool-project/icon.png") >> 'my_icon.png'

        http.loadGithubResource('tomitribe.io.config', 'master', "docs/my-cool-project/long_description.adoc") >> 'my_long_description'
        asciidoctor.render('my_long_description', Collections.<String, Object> emptyMap()) >> '<p>my_long_description</p>'

        http.loadGithubResource('tomitribe.io.config', 'master', "docs/my-cool-project/documentation.adoc") >> 'my_documentation'
        asciidoctor.render('my_documentation', Collections.<String, Object> emptyMap()) >> '<p>my_documentation</p>'

        http.loadGithubResource('tomitribe.io.config', 'master', "docs/my-cool-project/short_description.txt") >> 'my_short_description'

        http.getUrlContent("https://api.github.com/repos/tomitribe/my-cool-project/contributors?access_token=$token") >> getClass().getResource('/contributors.json').getText(StandardCharsets.UTF_8.name())

        http.getUrlContent("https://api.github.com/users/my_login?access_token=$token") >> getClass().getResource('/my_login_contributor.json').getText(StandardCharsets.UTF_8.name())

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
}
