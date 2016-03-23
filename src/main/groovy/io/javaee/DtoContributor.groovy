package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoContributor {
    String login
    Set<String> projects = []
    int contributions = 0
}