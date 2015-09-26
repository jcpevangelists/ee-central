package com.tomitribe.io.www

import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = 'metaClass')
class EntityContributions {
    @ManyToOne(optional = false)
    EntityProject project

    @ManyToOne(optional = false)
    EntityContributor contributor

    Integer contributions
}
