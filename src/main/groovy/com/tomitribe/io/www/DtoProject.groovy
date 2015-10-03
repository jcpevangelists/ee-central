package com.tomitribe.io.www

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoProject {
    @XmlElement
    String name

    @XmlElement
    String friendlyName

    @XmlElement
    String shortDescription

    @XmlElement
    String longDescription

    String snapshot

    @XmlElement
    String icon

    @XmlElement
    String documentation

    @XmlElement
    Set<DtoContributor> contributors

    @XmlElement
    Set<String> tags
}
