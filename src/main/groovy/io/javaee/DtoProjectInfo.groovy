package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoProjectInfo {
    String name
    String friendlyName
    String description
    String home
    Collection<DtoProjectInfo> related
    Collection<DtoProjectResource> resources
}
