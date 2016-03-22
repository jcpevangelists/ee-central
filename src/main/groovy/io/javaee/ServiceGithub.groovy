package io.javaee

import groovy.json.JsonSlurper

import javax.ejb.Stateless
import java.nio.charset.StandardCharsets

@Stateless
class ServiceGithub {

    String getRepoDescription(String projectName) {
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${projectName}".toURL().getText([
                        requestProperties: ['Accept': 'application/vnd.github.v3+json']
                ], StandardCharsets.UTF_8.name())
        )
        return json.description as String
    }

    List<DtoProjectContributor> getRepoContributors(String projectName) {
        def json = new JsonSlurper().parseText(
                "https://api.github.com/repos/${projectName}/contributors".toURL().getText([
                        requestProperties: ['Accept': 'application/vnd.github.v3+json']
                ], StandardCharsets.UTF_8.name())
        )
        return json.collect {
            new DtoProjectContributor(
                    login: it.login as String,
                    contributions: it.contributions as Integer
            )
        }
    }

    String getRepoPage(String projectName, String resourceName) {
        return "https://api.github.com/repos/${projectName}/contents/${resourceName}".toURL().getText([
                requestProperties: [Accept: 'application/vnd.github.v3.html']
        ], StandardCharsets.UTF_8.name())
    }


}

