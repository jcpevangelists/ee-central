package com.tomitribe.io.www

import javax.activation.MimetypesFileTypeMap
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.CacheControl
import javax.ws.rs.core.Response
import java.util.concurrent.TimeUnit

@Path('/images')
@Produces('image/*')
class RestPictures {
    @Inject
    private ServicePictures servicePictures

    @Inject
    private ServiceProjects serviceProjects

    @GET
    @Path('/project/snapshot/{projectName}')
    Response getProjectSnapshot(@PathParam("projectName") String projectName) {
        def builder = Response.ok(
                serviceProjects.projects.find({ it.name == projectName }).snapshot.decodeBase64(),
                new MimetypesFileTypeMap().getContentType('snapshot.png')
        )
        def cc = new CacheControl()
        cc.setPrivate(false)
        cc.setMaxAge(TimeUnit.DAYS.toSeconds(1) as Integer)
        builder.cacheControl(cc)
        builder.build()
    }

    @GET
    @Path('/about/{image}')
    Response getAboutPicture(@PathParam("image") String image) {

        def builder = Response.ok(
                servicePictures.getPictureByName(image).content.decodeBase64(),
                new MimetypesFileTypeMap().getContentType(image)
        )
        def cc = new CacheControl()
        cc.setPrivate(false)
        cc.setMaxAge(TimeUnit.DAYS.toSeconds(1) as Integer)
        builder.cacheControl(cc)
        builder.build()
    }
}
