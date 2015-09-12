package com.tomitribe.io.www

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToMany

@Entity
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

    @ManyToMany(mappedBy="projects")
    Set<EntityContributor> contributors
}
