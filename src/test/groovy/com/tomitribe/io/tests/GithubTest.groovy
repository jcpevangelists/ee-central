package com.tomitribe.io.tests

import com.tomitribe.io.www.DtoContributor
import com.tomitribe.io.www.DtoProject
import com.tomitribe.io.www.HttpBean
import com.tomitribe.io.www.ServiceGithub
import groovy.json.JsonOutput
import org.asciidoctor.Asciidoctor
import spock.lang.Specification

import javax.ejb.TimerService

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
        contributors[0] == new DtoContributor(
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
        def projectName = 'my-cool-project'
        def projectNoRelease = 'my-cool-project-no-release'
        def projectNoPublishedRelease = 'my-cool-project-no-published-release'
        def projectWithBadDocs = 'my-cool-project-bad-asciidoc'
        def projectWithoutConfig = 'my-cool-project-no-config'
        def projectGithubWentMad = 'my-cool-project-github-mad'
        def projectNoDocWithReadme = 'my-cool-project-no-doc-with-readme'
        def projectList = [[name: projectName], [name: projectWithBadDocs],
                           [name: projectWithoutConfig], [name: projectGithubWentMad],
                           [name: projectNoDocWithReadme], [name: projectNoRelease],
                           [name: projectNoPublishedRelease]]
        def projectJson = JsonOutput.toJson(projectList)
        def contributorsJson = JsonOutput.toJson([[login: "my_user", avatar_url: 'http://dummy/avatar.png']])

        when:
        srv.updateProjects()
        def projects = srv.getProjects()

        then:
        projectList.each {
            if (it.name == projectNoRelease) {
                http.getUrlContent("https://api.github.com/repos/tomitribe/$projectNoRelease/tags?access_token=$token") >> JsonOutput.toJson([])
            } else {
                http.getUrlContent("https://api.github.com/repos/tomitribe/${it.name}/tags?access_token=$token") >> JsonOutput.toJson([[name: "v0.0.1"], [name: "v0.0.1-beta"]])
            }
        }
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=1&per_page=20&access_token=$token") >> projectJson
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=2&per_page=20&access_token=$token") >> '[]'
        // good configuration data
        http.getUrlContent("$rawUrlPrefix/$projectName/v0.0.1/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
documentation: my documentation
short_description: my short description
"""
        // good configuration data but no published release
        http.getUrlContent("$rawUrlPrefix/$projectNoPublishedRelease/v0.0.1/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
documentation: my documentation
short_description: my short description
"""
        // configuration data with bad field
        http.getUrlContent("$rawUrlPrefix/$projectWithBadDocs/v0.0.1/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: pretend this is bad doc
documentation: my documentation
short_description: my short description
"""
        // no documentation field
        http.getUrlContent("$rawUrlPrefix/$projectNoDocWithReadme/v0.0.1/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
short_description: my short description
"""
        http.getUrlContent("$rawUrlPrefix/$projectNoDocWithReadme/v0.0.1/README.adoc?access_token=$token") >> "doc from readme"
        // no configuration data
        http.getUrlContent("$rawUrlPrefix/$projectWithoutConfig/v0.0.1/community.yaml?access_token=$token") >> {
            throw new FileNotFoundException()
        }
        // with weird exceptions
        http.getUrlContent("$rawUrlPrefix/$projectGithubWentMad/v0.0.1/community.yaml?access_token=$token") >> {
            throw new Exception('weird exception')
        }
        http.getUrlContent("$restReposPrefix/$projectName/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("$restReposPrefix/$projectNoDocWithReadme/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("https://api.github.com/users/my_user?access_token=$token") >> '{"name": "my user name"}'
        asciidoctor.render('doc from readme', Collections.<String, Object> emptyMap()) >> '<p>doc from readme</p>'
        asciidoctor.render('my documentation', Collections.<String, Object> emptyMap()) >> '<p>my documentation</p>'
        asciidoctor.render('my long description', Collections.<String, Object> emptyMap()) >> '<p>my long description</p>'
        asciidoctor.render('pretend this is bad doc', Collections.<String, Object> emptyMap()) >> {
            throw new Exception('Bad adoc')
        }
        projects.size() == 2
        projects[0] == new DtoProject(
                name: projectName,
                shortDescription: 'my short description',
                longDescription: '<p>my long description</p>',
                snapshot: 'http://dummy/snapshot.png',
                icon: 'http://dummy/icon.png',
                documentation: '<p>my documentation</p>',
                contributors: [new DtoContributor(
                        name: 'my user name',
                        login: 'my_user',
                        avatarUrl: 'http://dummy/avatar.png'
                )],
                tags: ['v0.0.1']
        )
        projects[1] == new DtoProject(
                name: projectNoDocWithReadme,
                shortDescription: 'my short description',
                longDescription: '<p>my long description</p>',
                snapshot: 'http://dummy/snapshot.png',
                icon: 'http://dummy/icon.png',
                documentation: '<p>doc from readme</p>',
                contributors: [new DtoContributor(
                        name: 'my user name',
                        login: 'my_user',
                        avatarUrl: 'http://dummy/avatar.png'
                )],
                tags: ['v0.0.1']
        )
    }
}
