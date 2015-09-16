package com.tomitribe.io.www

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToMany

@Entity
@EqualsAndHashCode(excludes = ['contributors'])
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class EntityProject {
    @Id
    String name

    String shortDescription

    @Lob
    String longDescription
    String snapshot
    String icon

    @Lob
    String documentation

    @ManyToMany(mappedBy = "projects")
    Set<EntityContributor> contributors

    @ElementCollection
    Set<String> tags
}
