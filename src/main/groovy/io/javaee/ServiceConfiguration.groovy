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
        def setIfEmpty = { String key, String value ->
            if (!properties.getProperty(key) && value) {
                properties.setProperty(key, value)
            }
        }
        setIfEmpty('google_forum_url', 'https://groups.google.com/forum/feed/javaee-guardians/topics/atom.xml?num=50')
        setIfEmpty('javaeeio_config_root', 'jcpevangelists/javaee.io.config')
        setIfEmpty('github_atoken', System.getenv()['github_atoken'])
        setIfEmpty('javaeeio_twitter_oauth_consumer_key', System.getenv()['javaeeio_twitter_oauth_consumer_key'])
        setIfEmpty('javaeeio_twitter_oauth_consumer_secret', System.getenv()['javaeeio_twitter_oauth_consumer_secret'])
        setIfEmpty('javaeeio_twitter_oauth_access_token', System.getenv()['javaeeio_twitter_oauth_access_token'])
        setIfEmpty('javaeeio_twitter_oauth_access_token_secret', System.getenv()['javaeeio_twitter_oauth_access_token_secret'])
    }
}
