package io.javaee

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoGroupMessage {
    String id
    String author
    Date updated
    String link
    String title
}
