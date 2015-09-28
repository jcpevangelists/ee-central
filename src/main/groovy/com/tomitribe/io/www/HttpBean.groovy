package com.tomitribe.io.www

import groovy.json.JsonSlurper

import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger

class HttpBean {
    private Logger logger = Logger.getLogger('tribeio.http')
    private String token = System.getenv()['github_atoken']

    String getUrlContent(String path) {
        path.toURL().getText(StandardCharsets.UTF_8.name())
    }

    def loadGithubResourceJson(String projectName, String release, String resourceName) {
        def url = "https://api.github.com/repos/tomitribe/$projectName/contents/$resourceName?access_token=$token&ref=$release"
        new JsonSlurper().parseText(getUrlContent(url))
    }

    String loadGithubResourceEncoded(String projectName, String release, String resourceName) {
        try {
            def json = loadGithubResourceJson(projectName, release, resourceName)
            return json.content as String
        } catch (FileNotFoundException ignore) {
            // no-op
        } catch (exception) {
            logger.log(Level.INFO, "Impossible to load resource [$projectName, $release, $resourceName]", exception)
        }
        return ''
    }

    String loadGithubResource(String projectName, String release, String resourceName) {
        def encoded = loadGithubResourceEncoded(projectName, release, resourceName)
        if (encoded) {
            return new String(encoded.decodeBase64(), StandardCharsets.UTF_8.name())
        }
        return ''
    }

    List<String> loadGithubResourceNames(String projectName, String release, String resourceDir) {
        try {
            def json = loadGithubResourceJson(projectName, release, resourceDir)
            return json.collect { it.name }
        } catch (FileNotFoundException ignore) {
            // no-op
        } catch (exception) {
            logger.log(Level.INFO, "Impossible to load resource [$projectName, $release, $resourceName]", exception)
        }
    }
}
