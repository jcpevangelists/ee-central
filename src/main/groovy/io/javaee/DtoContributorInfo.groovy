package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoContributorInfo {
    String login
    String name
    String avatar
    String company
    String location
    Set<String> projects
    int contributions
}
