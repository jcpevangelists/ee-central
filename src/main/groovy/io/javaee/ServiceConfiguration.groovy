package io.javaee

import org.tomitribe.sabot.ConfigurationObserver
import org.tomitribe.sabot.ConfigurationResolver

import javax.ejb.Singleton

@Singleton
class ServiceConfiguration implements ConfigurationObserver {

    static {
        // https://github.com/tomitribe/sabot
        ConfigurationResolver.registerConfigurationObserver(new ServiceConfiguration());
    }

    @Override
    void mergeConfiguration(Properties properties) {
        def setIfEmpty = { String key, def value ->
            if (!properties.getProperty(key) && value) {
                String propVal
                if (String.class.isInstance(value)) {
                    propVal = value as String
                } else {
                    propVal = value()
                }
                properties.setProperty(key, propVal)
            }
        }
        
        // add all system properties that start with javaeeio_
        System.getProperties().entrySet().each { def entry ->
            if (entry.key.startsWith("javaeeio_")) {
                setIfEmpty(entry.key, entry.value)
                // also add without the javaeeio_ prefix in the key
                setIfEmpty(entry.key.substring("javaeeio_".length()), entry.value)
            }
        }
        
        // set defaults if values not set already
        setIfEmpty('google_forum_url', 'https://groups.google.com/forum/feed/javaee-guardians/topics/atom.xml?num=50')
        setIfEmpty('javaeeio_config_root', 'jcpevangelists/javaee.io.config')
    }
}
