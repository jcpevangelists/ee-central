package io.javaee

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton

@Singleton
@Lock(LockType.READ)
class ServiceApplication {
    private URL documents

    @Lock(LockType.WRITE)
    void init(URL documents) {
        this.documents = documents
    }

    URL getDocuments() {
        return documents
    }
}

