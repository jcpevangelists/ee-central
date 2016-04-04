package io.javaee

import groovy.json.JsonSlurper

import javax.ejb.Stateless
import javax.inject.Inject
import javax.interceptor.Interceptors
import java.nio.charset.StandardCharsets

@Stateless
class ServiceGithub {

    private String docRoot = System.getProperty(
            'javaeeio_config_root',
            System.getenv()['javaeeio_config_root'] ?: 'jcpevangelists/javaee.io.config'
    )
    private String specsUrl = new URI("https://api.github.com/repos/${docRoot}/").resolve('contents/specs').toString()

    @Inject
    private ServiceApplication application

    @Interceptors(InterceptorGithub)
    List<DtoConfigFile> getConfigurationFiles() {
        List<DtoConfigFile> result = []
        def names = new JsonSlurper().parseText(specsUrl.toURL().getText([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3+json',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ], StandardCharsets.UTF_8.name())).collect { it.name }
        names.each {
            result << new DtoConfigFile(
                    name: it,
                    content: new String(
                            getRepoRaw(docRoot, "specs/${it}"),
                            StandardCharsets.UTF_8.name()
                    )
            )
        }
        return result
    }

    @Interceptors(InterceptorGithub)
    String getRepoDescription(String projectName) {
        if (!projectName) {
            return null
        }
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

    @Interceptors(InterceptorGithub)
    List<DtoProjectContributor> getRepoContributors(String projectName) {
        if (!projectName) {
            return []
        }
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

    @Interceptors(InterceptorGithub)
    String getRepoPage(String projectName, String resourceName) {
        return "https://api.github.com/repos/${projectName}/contents/${resourceName}".toURL().getText([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3.html',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ], StandardCharsets.UTF_8.name())
    }

    @Interceptors(InterceptorGithub)
    byte[] getRepoRaw(String projectName, String resourceName) {
        return "https://api.github.com/repos/${projectName}/contents/${resourceName}".toURL().getBytes([
                requestProperties: [
                        'Accept'       : 'application/vnd.github.v3.raw',
                        'Authorization': "token ${application.githubAuthToken}"
                ]
        ])
    }

    @Interceptors(InterceptorGithub)
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
                avatar: json.avatar_url as String,
                company: json.company as String,
                location: json.location as String
        )
    }

}
