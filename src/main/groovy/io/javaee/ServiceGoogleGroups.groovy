package io.javaee

import org.tomitribe.sabot.Config

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Schedule
import javax.ejb.Singleton
import javax.inject.Inject
import java.nio.charset.StandardCharsets

@Singleton
@Lock(LockType.READ)
class ServiceGoogleGroups {

    @Inject
    @Config(value = 'google_forum_url')
    private String url

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

