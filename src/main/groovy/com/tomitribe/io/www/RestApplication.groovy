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

    @Inject
    private ServicePictures servicePictures

    @GET
    DtoPage get(@Context ServletContext context) {
        new DtoPage(
                contributors: serviceContributors.contributors,
                projects: serviceProjects.projects,
                pictures: servicePictures.pictures.findAll {
                    !it.name.startsWith('small_')
                },
                tweets: serviceTwitter.tweets
        )
    }
}
