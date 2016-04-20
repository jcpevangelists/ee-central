package io.javaee

import org.tomitribe.sabot.Config
import org.yaml.snakeyaml.Yaml

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import java.nio.charset.StandardCharsets

@ApplicationScoped
class ServiceContributor {

    @Inject
    private ServiceGithub github

    @Inject
    private ServiceTwitter twitter

    @Inject
    private ServiceProject project

    @Inject
    @Config(value = 'javaeeio_config_root')
    private String docRoot

    DtoContributorInfo getContributor(String login) {
        return github.getContributor(login)
    }

    Collection<DtoContributorInfo> getContributorDetails() {
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

    Collection<DtoGuardian> getGuardians() {
        byte[] raw
        try {
            raw = github.getRepoRaw(docRoot, 'guardians.yaml')
        } catch (FileNotFoundException ignore) {
            return []
        }
        def yaml = new Yaml().loadAll(new String(raw, StandardCharsets.UTF_8.name()))
        return yaml.collect {
            def twitterBean = null
            if (it.twitter) {
                twitterBean = twitter.getUser(it.twitter as String)
            }
            return new DtoGuardian(twitter: twitterBean)
        }
    }

}

