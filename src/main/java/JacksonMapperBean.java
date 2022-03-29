import kotlin.Unit;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.kotlinModule;

@Configuration
public class JacksonMapperBean {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .modules(kotlinModule(x -> Unit.INSTANCE));
    }
}
