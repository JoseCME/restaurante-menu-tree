package gt.edu.miumg.app;

import gt.edu.miumg.engine.strategy.CollectionsTreeStrategy;
import gt.edu.miumg.engine.strategy.CustomTreeStrategy;
import gt.edu.miumg.engine.strategy.TreeAlgorithmStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TreeConfig {

    @Bean
    @ConditionalOnProperty(
        name = "app.tree-strategy",
        havingValue = "custom"
    )
    public TreeAlgorithmStrategy customStrategy() {
        return new CustomTreeStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = "app.tree-strategy",
        havingValue = "collections"
    )
    public TreeAlgorithmStrategy collectionsStrategy() {
        return new CollectionsTreeStrategy();
    }
}