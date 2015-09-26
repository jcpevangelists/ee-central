package com.tomitribe.io.www

import javax.inject.Inject
import javax.servlet.ServletContext
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context

@Path('/application')
@Produces('application/json')
class RestApplication {
    @Inject
    private ServiceContributors serviceContributors

    @Inject
    private ServiceProjects serviceProjects

    @Inject
    private ServiceTwitter serviceTwitter

    @GET
    DtoPage get(@Context ServletContext context) {
        Set<DtoPicture> pics = new File(context.getRealPath('/pics/')).listFiles({ File dir, String name ->
            !name.startsWith('small_')
        } as FilenameFilter).collect {
            new DtoPicture(name: it.name)
        }
        new DtoPage(
                contributors: serviceContributors.contributors,
                projects: serviceProjects.projects,
                pictures: pics,
                tweets: serviceTwitter.tweets
        )
    }
}
