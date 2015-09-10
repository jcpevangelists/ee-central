package com.tomitribe.io.www;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DtoProject {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String shortDescription;

    @Getter
    @Setter
    private String longDescription;
}
