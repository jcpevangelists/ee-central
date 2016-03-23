package io.javaee

import org.yaml.snakeyaml.Yaml

import javax.ejb.Stateless
import javax.inject.Inject

@Stateless
class ServiceProject {

    @Inject
    private ServiceApplication application

    @Inject
    private ServiceGithub github

    List<DtoProjectInfo> getAvailableProjects() {
        List<DtoProjectInfo> result = []
        new File(application.documents.file).listFiles().each {
            def conf = new Yaml().load(it.getText('UTF-8'))
            result << new DtoProjectInfo(
                    name: conf.name as String,
                    friendlyName: conf.friendly_name as String,
                    description: github.getRepoDescription(conf.name as String),
                    home: conf.home as String,
                    spec: true
            )
            conf.related?.each { relatedConf ->
                result << new DtoProjectInfo(
                        name: relatedConf.name as String,
                        friendlyName: relatedConf.friendly_name as String,
                        description: github.getRepoDescription(relatedConf.name as String),
                        home: relatedConf.home as String,
                        spec: false
                )
            }
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

