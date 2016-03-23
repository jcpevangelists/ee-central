package io.javaee

import groovy.json.JsonSlurper
import groovy.transform.Memoized

import javax.ejb.Stateless
import javax.inject.Inject
import java.nio.charset.StandardCharsets

@Stateless
class ServiceGithub {

    @Inject
    private ServiceApplication application

    @Memoized
    String getRepoDescription(String projectName) {
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${projectName}".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return json.description as String
    }

    @Memoized
    List<DtoProjectContributor> getRepoContributors(String projectName) {
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${projectName}/contributors".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return json.collect {
            new DtoProjectContributor(
                    login: it.login as String,
                    contributions: it.contributions as Integer
            )
        }
    }

    @Memoized
    String getRepoPage(String projectName, String resourceName) {
        return "https://api.github.com/repos/${projectName}/contents/${resourceName}".toURL().getText([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3.html',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ], StandardCharsets.UTF_8.name())
    }

    @Memoized
    byte[] getRepoRaw(String projectName, String resourceName) {
        return "https://api.github.com/repos/${projectName}/contents/${resourceName}".toURL().getBytes([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3.raw',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ])
    }

    @Memoized
    DtoContributorInfo getContributor(String login) {
        def json = new JsonSlurper().parseText(
                "https://api.github.com/users/${login}".toURL().getText([
                        requestProperties: [
                                'Accept'       : 'application/vnd.github.v3+json',
                                'Authorization': "token ${application.githubAuthToken}"
                        ]
                ], StandardCharsets.UTF_8.name())
        )
        return new DtoContributorInfo(
                login: json.login as String,
                name: json.name as String,
                avatar: json.avatar_url as String
        )
    }

}

