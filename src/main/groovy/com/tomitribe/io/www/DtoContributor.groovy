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
class DtoContributor {
    @XmlElement
    String login

    @XmlElement
    String avatarUrl

    @XmlElement
    String name

    @XmlElement
    String company

    @XmlElement
    String location

    @XmlElement
    String googlePlus

    @XmlElement
    String twitter

    @XmlElement
    String linkedin

    @XmlElement
    String bio

}
