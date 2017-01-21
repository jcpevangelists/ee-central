package io.javaee

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Timeout
import javax.ejb.Timer
import javax.ejb.TimerService
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class CachedHolder {
    private Logger logger = Logger.getLogger(this.class.name)
    // used as value in instead of null, because ConcurrentHashMap in caches does not accept null
    private static Object EMPTY_VALUE = new Object();

    private ConcurrentMap<Object, ConcurrentHashMap<String, Object>> caches
    private ConcurrentMap<Long, Object> idInstance
    private ConcurrentMap<Object, Long> instanceId

    private AtomicLong instanceCounter = new AtomicLong(0)

    @Resource
    private TimerService timerService

    @PostConstruct
    void init() {
        this.caches = new ConcurrentHashMap<>()
        this.idInstance = new ConcurrentHashMap<>()
        this.instanceId = new ConcurrentHashMap<>()
    }

    void createCache(Object beanInstance) {
        this.caches.put(beanInstance, new ConcurrentHashMap<>())
        Long id = instanceCounter.incrementAndGet()
        idInstance.put(id, beanInstance)
        instanceId.put(beanInstance, id)
    }

    void removeCache(Object beanInstance) {
        this.caches.remove(beanInstance)
        def id = instanceId.get(beanInstance)
        instanceId.remove(beanInstance)
        idInstance.remove(id)
    }

    Object get(Object beanInstance, Method method, Object[] arguments) {
        def methodCache = this.caches.get(beanInstance)
        String key = "${method.name}(${arguments?.join(', ')})"
        if (methodCache.containsKey(key)) {
            def value = methodCache.get(key)
            return EMPTY_VALUE.equals(value) ? null : value
        }
        throw new ExceptionCache('cache entry not found')
    }

    void set(Object beanInstance, Method method, Object[] arguments, Object value) {
        def methodCache = this.caches.get(beanInstance)
        String key = "${method.name}(${arguments?.join(', ')})"
        if (value == null) {
            value = EMPTY_VALUE
        }
        if (!methodCache.put(key, value)) {
            logger.info("New cache entry for ${beanInstance}#${method.name}(${arguments?.join(', ')})")
            timerService.createTimer(TimeUnit.MINUTES.toMillis(60), new TimerPayload(
                    beanId: instanceId.get(beanInstance),
                    entryKey: key
            ))
        }
    }

    @Timeout
    void timeout(Timer timer) {
        TimerPayload payload = timer.info as TimerPayload
        def beanInstance = idInstance.get(payload.beanId)
        String key = payload.entryKey
        def methodCache = this.caches.get(beanInstance)
        methodCache.remove(key)
        logger.info("Removed cache entry for ${beanInstance}#${key})")
    }

    @Lock(LockType.WRITE)
    void clearCache() {
        this.caches.values().each {
            it.clear()
        }
        this.idInstance.clear()
        this.instanceId.clear()
    }

    class TimerPayload implements Serializable {
        public Long beanId
        public String entryKey
    }

}
