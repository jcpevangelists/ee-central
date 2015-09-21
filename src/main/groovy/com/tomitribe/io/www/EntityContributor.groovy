package com.tomitribe.io.www

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
@EqualsAndHashCode(excludes = ['projects'])
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = 'metaClass')
class EntityContributor {
    @Id
    String login

    String avatarUrl

    String name

    String company

    String location

    @ManyToMany
    Set<EntityProject> projects
}
