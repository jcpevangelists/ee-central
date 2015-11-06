package com.tomitribe.io.www

import groovy.json.JsonSlurper
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
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class ServiceGithub {
    public static final String DOCUMENTATION_ROOT = System.getProperty(
            'io_config_root',
            System.getenv()['io_config_root'] ?: 'tomitribe'
    )
    public static final String CONFIG_PROJECT = System.getProperty(
            'io_config_project',
            System.getenv()['io_config_project'] ?: 'tomitribe.io.config'
    )
    private Logger logger = Logger.getLogger('tribeio')

    public static final int UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(60)

    private List<DtoProject> projects
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
        def url = HttpBean.BASE_URL + "/repos/${DOCUMENTATION_ROOT}/$projectName/contributors"
        new JsonSlurper().parseText(http.getUrlContentWithToken(url)).collect {
            def githubContributor = new JsonSlurper().parseText(
                    http.getUrlContentWithToken(HttpBean.BASE_URL + "/users/${it.login}")
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

    private List<DtoProject> sortMyConfigFile(List publishedDocsConfiguration, List newProjects) {
        def publishProjectsNames = publishedDocsConfiguration.collect { it.project }
        return newProjects.sort { publishProjectsNames.indexOf(it.name) }
    }

    private def parseJsonText(String text) {
        if (!text) {
            return null
        }
        return new JsonSlurper().parseText(text)
    }

    @Timeout
    void update() {
        try {
            timer?.cancel()
        } catch (ignore) {
            // no-op
        }
        def newProjects = []
        Map<String, DtoContributor> newContributors = [:]
        Set<DtoContributions> newContributions = []
        def publishedDocsConfiguration = new Yaml().loadAll(
                http.loadGithubResource(CONFIG_PROJECT, 'master', 'published_docs.yaml')
        ).collect({ it })
        Map<String, Set<String>> publishedTagsMap = publishedDocsConfiguration.collectEntries {
            [(it.project), it.tags]
        }
        publishedTagsMap.keySet().each { String projectName ->
            def pageUrl = HttpBean.BASE_URL + "/repos/${DOCUMENTATION_ROOT}/${projectName}"
            def json = parseJsonText(http.getUrlContentWithToken(pageUrl))
            if (!json) {
                logger.warning("${projectName} expected to be published but project does not exist.")
                return
            }
            Set<String> publishedTags = publishedTagsMap.get(json.name)
            if (!publishedTags) {
                logger.warning("${projectName} expected to be published but it has no publishable tag. Please check " +
                        "the ${HttpBean.BASE_URL}/${DOCUMENTATION_ROOT}/${CONFIG_PROJECT}/blob/master/published_docs.yaml file.")
                return
            }
            List<String> tags = parseJsonText(
                    http.getUrlContentWithToken("${HttpBean.BASE_URL}/repos/tomitribe/${json.name}/tags")
            ).collect({ it.name })
            tags.add(0, 'master') // all projects contain a master branch
            String release = tags.find({ publishedTags.contains(it) })
            if (!release) {
                logger.warning("${projectName} expected to be published but actual project does not contain any tag " +
                        "listed in the " +
                        "${HttpBean.BASE_URL}/${DOCUMENTATION_ROOT}/${CONFIG_PROJECT}/blob/master/published_docs.yaml file.")
                return
            }
            def snapshot = http.loadGithubResourceEncoded(CONFIG_PROJECT, 'master', "docs/${json.name}/snapshot.png")
            def icon = http.loadGithubResourceEncoded(CONFIG_PROJECT, 'master', "docs/${json.name}/icon.png")
            def longDescription = http.loadGithubResourceHtml(CONFIG_PROJECT, 'master', "docs/${json.name}/long_description.adoc")?.trim()
            def documentation = http.loadGithubResourceHtml(CONFIG_PROJECT, 'master', "docs/${json.name}/documentation.adoc")?.trim() ?:
                    http.loadGithubResourceHtml(json.name as String, release, 'README.adoc')?.trim()
            def shortDescription = (
                    http.loadGithubResource(CONFIG_PROJECT, 'master', "docs/${json.name}/short_description.txt")
                            ?: json.description)?.trim()
            if (!longDescription || !documentation || !shortDescription || !snapshot || !icon) {
                logger.warning("${projectName} expected to be published but it has missing data. " +
                        "is long description empty? ${!longDescription};" +
                        "is documentation empty? ${!documentation};" +
                        "is short description empty? ${!shortDescription};" +
                        "is snapshot empty? ${!snapshot};" +
                        "is icon empty? ${!icon};")
                return
            }
            def longDocumentation = http.loadGithubResourceHtml(json.name as String, release, 'documentation.adoc')?.trim()
            def projectContributors = getContributors(json.name as String)
            projectContributors.each { data ->
                def projectContributor = data.contributor
                newContributors.put(projectContributor.name, projectContributor)
                newContributions << data.contributions
            }
            newProjects.add(new DtoProject(
                    name: json.name,
                    shortDescription: shortDescription,
                    longDescription: longDescription,
                    snapshot: snapshot,
                    icon: icon,
                    documentation: documentation,
                    contributors: projectContributors.collect { it.contributor },
                    tags: tags.findAll({ publishedTags.contains(it) }),
                    longDocumentation: longDocumentation
            ))
        }
        this.projects = sortMyConfigFile(publishedDocsConfiguration, newProjects)
        this.contributors = newContributors.values()
        this.contributions = newContributions
        timer = timerService.createTimer(UPDATE_INTERVAL, 'Reload ServiceGithub')
    }

    List<DtoProject> getProjects() {
        projects
    }

    Set<DtoContributor> getContributors() {
        contributors
    }

    Set<DtoContributions> getContributions() {
        contributions
    }
}
