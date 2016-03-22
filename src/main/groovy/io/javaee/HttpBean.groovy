package io.javaee

import groovy.json.JsonSlurper

import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger

class HttpBean {
    public static final String BASE_URL = System.getProperty(
            'io_root_url',
            System.getenv()['io_root_url'] ?: 'https://api.github.com'
    )
    private Logger logger = Logger.getLogger('javaeeio')
    private String token = System.getProperty("io.github.token", System.getenv()['github_atoken'])

    URL getUrlWithToken(String path) {
        (path + (path.contains('?') ? '&' : '?') + "access_token=$token").toURL()
    }

    String getUrlContentWithToken(String path) {
        getUrlWithToken(path).getText(StandardCharsets.UTF_8.name())
    }

    def loadGithubResourceJson(String projectName, String release, String resourceName) {
        def url = "${BASE_URL}/repos/${ServiceGithub.DOCUMENTATION_ROOT}/$projectName/contents/$resourceName?access_token=$token&ref=$release"
        new JsonSlurper().parseText(getUrlContentWithToken(url))
    }

    String loadGithubResourceHtml(String projectName, String release, String resourceName) {
        def url = "${BASE_URL}/repos/${ServiceGithub.DOCUMENTATION_ROOT}/$projectName/contents/$resourceName?access_token=$token&ref=$release"
        try {
            getUrlWithToken(url).getText(
                    [requestProperties: [Accept: 'application/vnd.github.3.html']],
                    StandardCharsets.UTF_8.name()
            )
        } catch (FileNotFoundException ignore) {
            return ''
        }
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
