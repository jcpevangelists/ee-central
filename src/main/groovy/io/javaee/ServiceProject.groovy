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
        return new File(application.documents.file).listFiles().collect {
            def conf = new Yaml().load(it.getText('UTF-8'))
            return new DtoProjectInfo(
                    name: conf.name as String,
                    friendlyName: conf.friendly_name as String,
                    description: github.getRepoDescription(conf.name as String),
            )
        }
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

}

