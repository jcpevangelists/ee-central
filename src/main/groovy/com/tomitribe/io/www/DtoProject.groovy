package com.tomitribe.io.www

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
class DtoProject {
    @XmlElement
    String name

    @XmlElement
    String shortDescription

    @XmlElement
    String longDescription

    @XmlElement
    String snapshot

    @XmlElement
    String icon

    @XmlElement
    String documentation

    @XmlElement
    Set<DtoContributor> contributors
}
