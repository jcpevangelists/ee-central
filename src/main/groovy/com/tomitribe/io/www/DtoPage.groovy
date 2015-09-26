package com.tomitribe.io.www

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
class DtoPage {
    @XmlElement
    Set<DtoContributor> contributors

    @XmlElement
    Set<DtoProject> projects

    @XmlElement
    Set<DtoPicture> pictures

    @XmlElement
    Set<DtoTweet> tweets
}
