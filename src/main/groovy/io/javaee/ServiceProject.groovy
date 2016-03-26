package io.javaee

import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless
import javax.inject.Inject
import java.util.logging.Level
import java.util.logging.Logger

@Stateless
class ServiceProject {
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    private ServiceGithub github

    private def loadYaml(String content) {
        try {
            return new Yaml().load(content)
        } catch (e) {
            logger.log(Level.WARNING, "Invalid yaml file", e)
        }
        return null
    }

    Set<DtoProjectInfo> getAvailableProjects() {
        Set<DtoProjectInfo> result = []
        github.getConfigurationFiles().each {
            def conf = loadYaml(it)
            if (!conf) {
                return
            }
            def info = new DtoProjectInfo(
                    name: conf.name as String,
                    friendlyName: conf.friendly_name as String,
                    description: github.getRepoDescription(conf.name as String),
                    home: conf.home as String,
                    resources: conf.resources?.collect { resource ->
                        def dto = new DtoProjectResource()
                        if (String.class.isInstance(resource)) {
                            dto.url = resource
                        } else {
                            dto.url = resource.url
                            dto.title = resource.title
                        }
                        return dto
                    },
                    related: [],
                    spec: true
            )

            conf.related?.each { relatedConf ->
                def relatedInfo = new DtoProjectInfo(
                        name: relatedConf.name as String,
                        friendlyName: relatedConf.friendly_name as String,
                        description: github.getRepoDescription(relatedConf.name as String),
                        home: relatedConf.home as String,
                        resources: relatedConf.resources?.collect { resource ->
                            def dto = new DtoProjectResource()
                            if (String.class.isInstance(resource)) {
                                dto.url = resource
                            } else {
                                dto.url = resource.url
                                dto.title = resource.title
                            }
                            return dto
                        }
                )
                result << relatedInfo
                info.related << relatedInfo
            }
            result << info
        }
        return result
    }

    DtoProjectDetail getDetails(String projectName) {
        DtoProjectInfo info = availableProjects.find { it.name == projectName }
        if (!info) {
            throw new ExceptionApplication("Project not found: '${projectName}'")
        }
        Set<DtoProjectContributor> contributors = github.getRepoContributors(projectName)
        info.related.each {
            contributors.addAll(github.getRepoContributors(it.name))
        }
        return new DtoProjectDetail(
                info: info,
                contributors: contributors
        )
    }

    String getPage(String projectName, String resourceName) {
        return github.getRepoPage(projectName, resourceName)
    }

    byte[] getRaw(String projectName, String resourceName) {
        return github.getRepoRaw(projectName, resourceName)
    }

    List<DtoContributor> getAllContributors() {
        Map<String, DtoContributor> contributors = [:]
        getAvailableProjects().each { project ->
            def details = getDetails(project.name)
            details.contributors.each { projContributor ->
                DtoContributor contributor = contributors.get(projContributor.login)
                if (!contributor) {
                    contributor = new DtoContributor(
                            login: projContributor.login
                    )
                    contributors.put(projContributor.login, contributor)
                }
                contributor.projects << project.name
                contributor.contributions += projContributor.contributions
            }
        }
        return contributors.values() as List<DtoContributor>
    }

}

