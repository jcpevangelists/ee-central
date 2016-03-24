package io.javaee

import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless
import javax.inject.Inject

@Stateless
class ServiceProject {

    @Inject
    private ServiceGithub github

    List<DtoProjectInfo> getAvailableProjects() {
        List<DtoProjectInfo> result = []
        github.configurationFiles.each {
            def conf = new Yaml().load(it)
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
                    related: []
            )

            conf.related?.each { relatedConf ->
                def relatedInfo = new DtoProjectInfo(
                        name: relatedConf.name as String,
                        friendlyName: relatedConf.friendly_name as String,
                        description: github.getRepoDescription(relatedConf.name as String),
                        home: relatedConf.home as String,
                        resources: conf.resources?.collect { resource ->
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
        return new DtoProjectDetail(
                info: info,
                contributors: github.getRepoContributors(projectName)
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

