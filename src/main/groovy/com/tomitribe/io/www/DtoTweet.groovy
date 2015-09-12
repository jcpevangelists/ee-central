package com.tomitribe.io.www

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
class DtoTweet {
    @XmlElement
    String text

    @XmlElement
    String author

    @XmlElement
    Long timestamp
}
