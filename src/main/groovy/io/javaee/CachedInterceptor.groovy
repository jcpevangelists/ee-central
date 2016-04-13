package io.javaee

import javax.inject.Inject
import javax.interceptor.AroundInvoke
import javax.interceptor.Interceptor
import javax.interceptor.InvocationContext
import java.util.logging.Logger

@Interceptor
@Cached
class CachedInterceptor {
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    private CachedHolder holder

    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception {
        Object result
        try {
            result = holder.get(ctx.method.name, ctx.parameters)
        } catch (ExceptionCache ignore) {
            result = ctx.proceed()
            holder.set(ctx.method.name, ctx.parameters, result)
            logger.info("New cache entry for ${ctx.method.name}(${ctx.parameters?.join(', ')})")
        }
        return result;
    }

}
