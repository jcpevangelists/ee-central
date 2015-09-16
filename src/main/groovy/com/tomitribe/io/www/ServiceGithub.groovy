package com.tomitribe.io.www

import groovy.json.JsonSlurper
import org.asciidoctor.Asciidoctor
import org.yaml.snakeyaml.Yaml

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Timeout
import javax.ejb.TimerService
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithub {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(5)

    private Logger logger = Logger.getLogger(this.class.name)
    private Asciidoctor asciidoctor = Asciidoctor.Factory.create()
    private String token = System.getenv()['github_atoken']

    private Set<DtoProject> projects
    private Set<DtoContributor> contributors

    @Inject
    private HttpBean http

    @Resource
    private TimerService timerService

    @PostConstruct
    void init() {
        timerService.createTimer(0, "First time twitter load")
    }

    Set<DtoContributor> getContributors(String projectName) {
        def url = "https://api.github.com/repos/tomitribe/$projectName/contributors?access_token=$token"
        new JsonSlurper().parseText(http.getUrlContent(url)).collect {
            def githubContributor = new JsonSlurper().parseText(
                    http.getUrlContent("https://api.github.com/users/${it.login}?access_token=$token")
            )
            new DtoContributor(
                    login: it.login,
                    avatarUrl: it.avatar_url,
                    name: githubContributor.name
            )
        }
    }

    private String loadGithubResource(String projectName, String release, String resourceName) {
        def url = "https://raw.githubusercontent.com/tomitribe/${projectName}/$release/${resourceName}?access_token=$token"
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

    @Timeout
    void updateProjects() {
        int page = 1
        def newProjects = []
        Map<String, DtoContributor> newContributors = [:]
        Map<String, Set<String>> publishedTagsMap = new Yaml().loadAll(
                this.getClass().getResource('/published_docs.yaml').text
        ).collectEntries {
            [(it.project), it.tags]
        }
        while (true) {
            def pageUrl = "https://api.github.com/orgs/tomitribe/repos?page=${page++}&per_page=20&access_token=$token"
            def json = new JsonSlurper().parseText(http.getUrlContent(pageUrl))
            if (!json) {
                break
            }
            def emptyProject = new DtoProject()
            newProjects.addAll(json.collect({
                Set<String> publishedTags = publishedTagsMap.get(it.name)
                if (!publishedTags) {
                    return emptyProject
                }
                List<String> tags = new JsonSlurper().parseText(
                        http.getUrlContent("https://api.github.com/repos/tomitribe/${it.name}/tags?access_token=$token")
                ).collect({ it.name })
                String release = tags.find({ publishedTags.contains(it) })
                if (!release) {
                    return emptyProject
                }
                def ioConfig = new Yaml().load(loadGithubResource(
                        it.name as String, release, 'community.yaml')
                )
                if (!ioConfig || !ioConfig.snapshot?.trim() || !ioConfig.icon?.trim()) {
                    return emptyProject
                }
                def longDescription = adocToHtml(getText(ioConfig.long_description as String)).trim()
                def documentation = adocToHtml(getText(ioConfig.documentation as String) ?:
                        loadGithubResource(it.name as String, release, 'README.adoc')).trim()
                def shortDescription = (getText(ioConfig.short_description as String) ?: it.description)?.trim()
                if (!longDescription || !documentation || !shortDescription) {
                    return emptyProject
                }
                def projectContributors = getContributors(it.name as String)
                projectContributors.each { DtoContributor projectContributor ->
                    newContributors.put(projectContributor.name, projectContributor)
                }
                new DtoProject(
                        name: it.name,
                        friendlyName: ioConfig.friendly_name,
                        shortDescription: shortDescription,
                        longDescription: longDescription,
                        snapshot: ioConfig.snapshot,
                        icon: ioConfig.icon,
                        documentation: documentation,
                        contributors: projectContributors,
                        tags: tags.findAll({ publishedTags.contains(it) })
                )
            }).findAll { it != emptyProject })
        }
        this.projects = newProjects
        this.contributors = newContributors.values()
        timerService.createTimer(UPDATE_INTERVAL, "Reload twitter")
    }

    Set<DtoProject> getProjects() {
        projects
    }

    Set<DtoContributor> getContributors() {
        contributors
    }
}
