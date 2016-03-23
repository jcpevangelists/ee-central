package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoContributor {
    String login
    Set<String> projects = []
    int contributions = 0
}
