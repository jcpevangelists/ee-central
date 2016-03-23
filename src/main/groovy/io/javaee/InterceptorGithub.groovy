package io.javaee

import javax.ejb.EJB
import javax.interceptor.AroundInvoke
import javax.interceptor.InvocationContext
import java.util.logging.Logger

class InterceptorGithub {
    private Logger logger = Logger.getLogger(this.class.name)

    @EJB
    ServiceGithubCache cache

    @AroundInvoke
    Object interceptorMethod(InvocationContext ctx) throws Exception {
        Object result
        try {
            result = cache.get(ctx.method.name, ctx.parameters)
        } catch (ExceptionCache ignore) {
            result = ctx.proceed()
            cache.set(ctx.method.name, ctx.parameters, result)
            logger.info("New cache entry for ${ctx.method.name}(${ctx.parameters?.join(', ')})")
        }
        return result;
    }
}
