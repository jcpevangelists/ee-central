
package io.javaee.devel

import io.javaee.ServiceProject
import org.tomitribe.sabot.Config
import javax.inject.Inject
import javax.enterprise.inject.Specializes
import javax.enterprise.inject.Alternative
import java.nio.charset.StandardCharsets
import java.util.logging.Logger

/**
 * When this decorator is enabled, it will try to read the contents of a page from local directory. 
 * If a file with name resourceName plus .html suffix exists, it will use the HTML content of the file.
 * Otherwise it will continue geting application page as usual.
 * 
 * This is for developer purposes to preview a page generated from adoc/md without the need to commit it to a github repo.
 * 
 * To enable this decorator, add this class as an alternative to beans.xml.
 * 
 * Note: it would be cleaner to use a CDI decorator instead of specializes, but I could not get it triggered by any means.
 * 
 * @author OndrejM
 */
@Specializes
@Alternative
class ServiceProjectDevelSpecializer extends ServiceProject {
    
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    @Config(value = 'devel_pages_root')
    String develPagesRoot
    
    String getApplicationPage(String resourceName) {
        File f = new File(develPagesRoot, resourceName + ".html")
        logger.info("file = " + f)
        if (f.exists()) {
            return getPageFromFile(f)
        }
        int i = resourceName.lastIndexOf(".");
        if (i >= 0) {
            f = new File(develPagesRoot, resourceName.substring(0, i) + ".html")
            logger.info("file = " + f)
            if (f.exists()) {
                return getPageFromFile(f)
            }
        }
        logger.warning("resource " + resourceName + " not found, searching in standard location")
        return super.getApplicationPage(resourceName)
    }
    
    private String getPageFromFile(File f) {
        def rawContent = f.getText(StandardCharsets.UTF_8.name())
        "<div>${rawContent}</div>"
    }
}

