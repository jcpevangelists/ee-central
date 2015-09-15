package com.tomitribe.io.www

import groovy.json.JsonSlurper
import org.asciidoctor.Asciidoctor
import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless
import javax.inject.Inject
import java.util.logging.Level
import java.util.logging.Logger

@Stateless
class ServiceGithub {
    private Logger logger = Logger.getLogger(this.class.name)
    private Asciidoctor asciidoctor = Asciidoctor.Factory.create()
    private String token = System.getenv()['github_atoken']

    @Inject
    private HttpBean http

    Set<DtoContributor> getContributors(String projectName) {
        def url = "https://api.github.com/repos/tomitribe/$projectName/contributors?access_token=$token"
        new JsonSlurper().parseText(http.getUrlContent(url)).collect {
            def githubContributor = new JsonSlurper().parseText(
                    http.getUrlContent("https://api.github.com/users/${it.login}")
            )
            new DtoContributor(
                    login: it.login,
                    avatarUrl: it.avatar_url,
                    name: githubContributor.name
            )
        }
    }

    private String loadGithubResource(String projectName, String resourceName) {
        def url = "https://raw.githubusercontent.com/tomitribe/${projectName}/master/${resourceName}?access_token=$token"
        try {
            return http.getUrlContent(url)
        } catch (FileNotFoundException ignore) {
            // this project does not have a documentation
        } catch (exception) {
            logger.log(Level.WARNING, "Impossible to load resource ${url}", exception)
        }
        return ""
    }

    private String adocToHtml(String adoc) {
        if (adoc) {
            try {
                return asciidoctor.render(adoc, Collections.<String, Object> emptyMap())
            } catch (exception) {
                logger.log(Level.WARNING, 'Impossible to generate html from adoc.', exception)
                logger.log(Level.FINE, adoc)
            }
        }
        return ""
    }

    private String getText(String ioConfigFieldContent) {
        try {
            // Return the content of the url, if this string represents one.
            return ioConfigFieldContent.toURL().text
        } catch (ignore) {
            return ioConfigFieldContent
        }
    }

    List<DtoProject> getProjects() {
        int page = 1
        def result = []
        while (true) {
            def pageUrl = "https://api.github.com/orgs/tomitribe/repos?page=${page++}&per_page=20&access_token=$token"
            def json = new JsonSlurper().parseText(http.getUrlContent(pageUrl))
            if (!json) {
                break
            }
            def emptyProject = new DtoProject()
            result.addAll(json.collect({
                def ioConfig = new Yaml().load(loadGithubResource(
                        it.name as String, 'community.yaml')
                )
                if (!ioConfig || !ioConfig.snapshot?.trim() || !ioConfig.icon?.trim()) {
                    return emptyProject
                }
                def longDescription = adocToHtml(getText(ioConfig.long_description as String)).trim()
                def documentation = adocToHtml(getText(ioConfig.documentation as String) ?:
                        loadGithubResource(it.name as String, 'README.adoc')).trim()
                def shortDescription = (getText(ioConfig.short_description as String) ?: it.description)?.trim()
                if (!longDescription || !documentation || !shortDescription) {
                    return emptyProject
                }
                new DtoProject(
                        name: it.name,
                        friendlyName: ioConfig.friendly_name,
                        shortDescription: shortDescription,
                        longDescription: longDescription,
                        snapshot: ioConfig.snapshot,
                        icon: ioConfig.icon,
                        documentation: documentation,
                        contributors: getContributors(it.name as String)
                )
            }).findAll { it != emptyProject })
        }
        result
    }
}
