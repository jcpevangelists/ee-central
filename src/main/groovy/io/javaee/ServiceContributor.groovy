package io.javaee

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ServiceContributor {

    @Inject
    private ServiceGithub github

    @Inject
    private ServiceProject project

    DtoContributorInfo getContributor(String login) {
        return github.getContributor(login)
    }

    List<DtoContributorInfo> getContributorDetails() {
        Map<String, DtoContributor> contributions = project.allContributors.collectEntries {
            [(it.login): it]
        }
        return project.allContributors.collect { contributor ->
            def info = getContributor(contributor.login)
            def userContributions = contributions.get(contributor.login)
            info.projects = userContributions.projects
            info.contributions = userContributions.contributions
            info
        }
    }
}

