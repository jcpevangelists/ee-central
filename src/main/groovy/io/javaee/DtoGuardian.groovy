package io.javaee

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoGuardian {
    DtoTwitterUser twitter
}
