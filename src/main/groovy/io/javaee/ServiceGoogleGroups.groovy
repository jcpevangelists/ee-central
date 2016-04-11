package io.javaee

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Schedule
import javax.ejb.Singleton
import java.nio.charset.StandardCharsets

@Singleton
@Lock(LockType.READ)
class ServiceGoogleGroups {

    private String url = 'https://groups.google.com/forum/feed/javaee-guardians/topics/atom.xml?num=50'

    Collection<DtoGroupMessage> messages

    @Schedule(hour = "*")
    @Lock(LockType.WRITE)
    @PostConstruct
    void load() {
        String xmlText = url.toURL().getText([:], StandardCharsets.UTF_8.name())
        def xml = new XmlSlurper().parseText(xmlText)
        this.messages = xml.entry.collect {
            return new DtoGroupMessage(
                    id: it.id.text(),
                    author: it.author.text(),
                    updated: it.updated.text(),
                    title: it.title.text(),
                    summary: it.summary.text()
            )
        }
    }

    Collection<DtoGroupMessage> listMessages() {
        return this.messages
    }
}

