package com.tomitribe.io.tests

import com.tomitribe.io.www.DtoContributor
import com.tomitribe.io.www.DtoProject
import com.tomitribe.io.www.HttpBean
import com.tomitribe.io.www.ServiceGithub
import groovy.json.JsonOutput
import org.asciidoctor.Asciidoctor
import spock.lang.Specification

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
        http.getUrlContent("https://api.github.com/users/my_user") >> '{"name": "my user name"}'
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
        def srv = new ServiceGithub(
                token: token,
                http: http,
                asciidoctor: asciidoctor
        )
        def projectName = 'my-cool-project'
        def projectWithBadDocs = 'my-cool-project-bad-asciidoc'
        def projectWithoutConfig = 'my-cool-project-no-config'
        def projectGithubWentMad = 'my-cool-project-github-mad'
        def projectNoDocWithReadme = 'my-cool-project-no-doc-with-readme'
        def projectJson = JsonOutput.toJson([[name: projectName], [name: projectWithBadDocs],
                                             [name: projectWithoutConfig], [name: projectGithubWentMad],
                                             [name: projectNoDocWithReadme]])
        def contributorsJson = JsonOutput.toJson([[login: "my_user", avatar_url: 'http://dummy/avatar.png']])

        when:
        def projects = srv.getProjects()

        then:
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=1&per_page=20&access_token=$token") >> projectJson
        http.getUrlContent("https://api.github.com/orgs/tomitribe/repos?page=2&per_page=20&access_token=$token") >> '[]'
        http.getUrlContent("$rawUrlPrefix/$projectName/master/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
documentation: my documentation
short_description: my short description
"""
        // configuration data with bad field
        http.getUrlContent("$rawUrlPrefix/$projectWithBadDocs/master/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: pretend this is bad doc
documentation: my documentation
short_description: my short description
"""
        // no documentation field
        http.getUrlContent("$rawUrlPrefix/$projectNoDocWithReadme/master/community.yaml?access_token=$token") >> """
icon: http://dummy/icon.png
snapshot: http://dummy/snapshot.png
long_description: my long description
short_description: my short description
"""
        http.getUrlContent("$rawUrlPrefix/$projectNoDocWithReadme/master/README.adoc?access_token=$token") >> "doc from readme"
        // no configuration data
        http.getUrlContent("$rawUrlPrefix/$projectWithoutConfig/master/community.yaml?access_token=$token") >> {
            throw new FileNotFoundException()
        }
        // with weird exceptions
        http.getUrlContent("$rawUrlPrefix/$projectGithubWentMad/master/community.yaml?access_token=$token") >> {
            throw new Exception('weird exception')
        }
        http.getUrlContent("$restReposPrefix/$projectName/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("$restReposPrefix/$projectNoDocWithReadme/contributors?access_token=$token") >> contributorsJson
        http.getUrlContent("https://api.github.com/users/my_user") >> '{"name": "my user name"}'
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
                )]
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
                )]
        )
    }
}
