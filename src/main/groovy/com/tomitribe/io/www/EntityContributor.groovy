package com.tomitribe.io.www

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class EntityContributor {
    @Id
    String login

    String avatarUrl

    String name

    @ManyToMany
    Set<EntityProject> projects
}
