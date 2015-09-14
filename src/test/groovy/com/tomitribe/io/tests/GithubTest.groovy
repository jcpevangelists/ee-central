package com.tomitribe.io.tests

import com.tomitribe.io.www.DtoContributor
import com.tomitribe.io.www.DtoProject
import com.tomitribe.io.www.HttpBean
import com.tomitribe.io.www.ServiceGithub
import groovy.json.JsonOutput
import org.asciidoctor.Asciidoctor
import spock.lang.Specification

class GithubTest extends Specification {
    private Asciidoctor asciidoctor = Asciidoctor.Factory.create()

    def "geting the list of contributors"() {
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
        http.getUrlContent("https://api.github.com/repos/tomitribe/$projectName/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("https://api.github.com/users/my_user") >> '{"name": "my user name"}'
        contributors.size() == 1
        contributors[0] == new DtoContributor(
                name: 'my user name',
                login: 'my_user',
                avatarUrl: 'http://dummy/avatar.png'
        )
    }

    def "geting the list of projects"() {
        setup:
        def http = Mock(HttpBean)
        def token = 'my_secret'
        def srv = new ServiceGithub(
                token: token,
                http: http
        )
        def projectName = 'my-cool-project'
        def projectJson = JsonOutput.toJson([[name: projectName]])
        def contributorsJson = JsonOutput.toJson([[login: "my_user", avatar_url: 'http://dummy/avatar.png']])

        when:
        def projects = srv.getProjects()

        then:
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=1&per_page=20&access_token=$token") >> projectJson
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=2&per_page=20&access_token=$token") >> '[]'
        http.getUrlContent("https://raw.githubusercontent.com/tomitribe/$projectName/master/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
documentation: my documentation
short_description: my short description
"""
        http.getUrlContent("https://api.github.com/repos/tomitribe/$projectName/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("https://api.github.com/users/my_user") >> '{"name": "my user name"}'
        projects.size() == 1
        projects[0] == new DtoProject(
                name: projectName,
                shortDescription: 'my short description',
                longDescription: asciidoctor.render('my long description', Collections.<String, Object> emptyMap()),
                snapshot: 'http://dummy/snapshot.png',
                icon: 'http://dummy/icon.png',
                documentation: asciidoctor.render('my documentation', Collections.<String, Object> emptyMap()),
                contributors: [new DtoContributor(
                        name: 'my user name',
                        login: 'my_user',
                        avatarUrl: 'http://dummy/avatar.png'
                )]
        )
    }
}
