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
import javax.ejb.Timer
import javax.ejb.TimerService
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithub {
    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(60)

    private Logger logger = Logger.getLogger('tribeio.github')
    private Asciidoctor asciidoctor = Asciidoctor.Factory.create()
    private String token = System.getenv()['github_atoken']

    private Set<DtoProject> projects
    private Set<DtoContributor> contributors
    private Set<DtoContributions> contributions

    @Inject
    private HttpBean http

    @Resource
    private TimerService timerService

    private Timer timer

    @PostConstruct
    void init() {
        timer = timerService.createTimer(0, 'First time ServiceGithub load')
    }

    def getContributors(String projectName) {
        def url = "https://api.github.com/repos/tomitribe/$projectName/contributors?access_token=$token"
        new JsonSlurper().parseText(http.getUrlContent(url)).collect {
            def githubContributor = new JsonSlurper().parseText(
                    http.getUrlContent("https://api.github.com/users/${it.login}?access_token=$token")
            )
            [
                    contributor  : new DtoContributor(
                            login: it.login,
                            avatarUrl: it.avatar_url,
                            name: githubContributor.name,
                            company: githubContributor.company,
                            location: githubContributor.location
                    ),
                    contributions: new DtoContributions(
                            project: projectName,
                            login: it.login,
                            contributions: it.contributions as Integer
                    )
            ]
        }
    }

    private String adocToHtml(String adoc) {
        if (adoc) {
            try {
                return asciidoctor.render(adoc, [:])
            } catch (exception) {
                logger.log(Level.WARNING, 'Impossible to generate html from adoc.', exception)
                logger.log(Level.FINE, adoc)
            }
        }
        return ''
    }

    @Timeout
    void update() {
        try {
            timer?.cancel()
        } catch (ignore) {
            // no-op
        }
        int page = 1
        def newProjects = []
        Map<String, DtoContributor> newContributors = [:]
        Set<DtoContributions> newContributions = []
        Map<String, Set<String>> publishedTagsMap = new Yaml().loadAll(
                http.loadGithubResource('tomitribe.io.config', 'master', 'published_docs.yaml')).collectEntries {
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
                tags.add(0, 'master') // all projects contain a master branch
                String release = tags.find({ publishedTags.contains(it) })
                if (!release) {
                    return emptyProject
                }
                def snapshot = http.loadGithubResourceEncoded('tomitribe.io.config', 'master', "docs/${it.name}/snapshot.png")
                def icon = http.loadGithubResourceEncoded('tomitribe.io.config', 'master', "docs/${it.name}/icon.png")
                def longDescription = adocToHtml(
                        http.loadGithubResource('tomitribe.io.config', 'master', "docs/${it.name}/long_description.adoc")
                ).trim()
                def documentation = adocToHtml(
                        http.loadGithubResource('tomitribe.io.config', 'master', "docs/${it.name}/documentation.adoc") ?:
                                http.loadGithubResource(it.name as String, release, 'README.adoc')
                ).trim()
                def shortDescription = (
                        http.loadGithubResource('tomitribe.io.config', 'master', "docs/${it.name}/short_description.txt")
                                ?: it.description)?.trim()
                if (!longDescription || !documentation || !shortDescription || !snapshot || !icon) {
                    return emptyProject
                }
                def projectContributors = getContributors(it.name as String)
                projectContributors.each { data ->
                    def projectContributor = data.contributor
                    newContributors.put(projectContributor.name, projectContributor)
                    newContributions << data.contributions
                }
                new DtoProject(
                        name: it.name,
                        shortDescription: shortDescription,
                        longDescription: longDescription,
                        snapshot: snapshot,
                        icon: icon,
                        documentation: documentation,
                        contributors: projectContributors.collect { it.contributor },
                        tags: tags.findAll({ publishedTags.contains(it) })
                )
            }).findAll { it != emptyProject })
        }
        this.projects = newProjects
        this.contributors = newContributors.values()
        this.contributions = newContributions
        timer = timerService.createTimer(UPDATE_INTERVAL, 'Reload ServiceGithub')
    }

    Set<DtoProject> getProjects() {
        projects
    }

    Set<DtoContributor> getContributors() {
        contributors
    }

    Set<DtoContributions> getContributions() {
        contributions
    }
}
