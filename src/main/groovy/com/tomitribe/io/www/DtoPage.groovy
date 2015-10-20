package com.tomitribe.io.www

import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoPage {
    @XmlElement
    Set<DtoContributor> contributors

    @XmlElement
    List<DtoProject> projects

    @XmlElement
    Set<DtoPicture> pictures

    @XmlElement
    Set<DtoTweet> tweets
}
