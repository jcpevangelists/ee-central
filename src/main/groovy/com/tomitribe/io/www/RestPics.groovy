package com.tomitribe.io.www

import javax.servlet.ServletContext
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context

@Path("/pictures")
@Produces("application/json")
class RestPics {

    @GET
    Set<DtoPicture> list(@Context ServletContext context) {
        new File(context.getRealPath('/pics/')).listFiles({ File dir, String name ->
            !name.startsWith('small_')
        } as FilenameFilter).collect {
            new DtoPicture(name: it.name)
        }
    }
}
