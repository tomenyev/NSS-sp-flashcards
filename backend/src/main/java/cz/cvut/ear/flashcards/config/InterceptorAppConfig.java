package cz.cvut.ear.flashcards.config;

import cz.cvut.ear.flashcards.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/** Interceptor component & config
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Component
public class InterceptorAppConfig extends WebMvcConfigurationSupport {
    @Autowired
    private Interceptor interceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
