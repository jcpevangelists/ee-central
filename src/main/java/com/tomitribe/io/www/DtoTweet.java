package com.tomitribe.io.www;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DtoTweet {
    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private Long timestamp;

}
