package io.javaee

import javax.ejb.Stateless
import java.nio.charset.StandardCharsets

@Stateless
class ServiceGoogleGroups {

    private String url = 'https://groups.google.com/forum/feed/javaee-guardians/topics/atom.xml?num=50'

    Collection<DtoGroupMessage> listMessages() {
        String xmlText = url.toURL().getText([:], StandardCharsets.UTF_8.name())
        def xml = new XmlSlurper().parseText(xmlText)
        return xml.entry.collect {
            return new DtoGroupMessage(
                    id: it.id.text(),
                    author: it.author.text(),
                    updated: Date.parse("yyyy-MM-dd'T'HH:mm:ss", it.updated.text() as String),
                    link: it.link.text(),
                    title: it.title.text()
            )
        }
    }
}

