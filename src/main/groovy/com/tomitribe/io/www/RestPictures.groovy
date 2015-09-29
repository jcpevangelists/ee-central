package com.tomitribe.io.www

import javax.activation.MimetypesFileTypeMap
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path('/images')
@Produces('image/*')
class RestPictures {
    @Inject
    private ServicePictures servicePictures

    @GET
    @Path('/about/{image}')
    Response getAboutPicture(@PathParam("image") String image) {
        Response.ok(
                servicePictures.getPictureByName(image).content.decodeBase64(),
                new MimetypesFileTypeMap().getContentType(image)
        ).build()
    }
}
