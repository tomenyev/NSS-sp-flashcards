package cz.cvut.ear.flashcards.config;


import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Hazelcast configuration file
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelCastConfig() {
        
        // setup cache for topics, decks, cards and reviews
        return new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(
                        new MapConfig().setName("topicsCache")
                        .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                        .setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(2000)
                )
                .addMapConfig(
                        new MapConfig().setName("decksCache")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(2000)
                )
                .addMapConfig(
                        new MapConfig().setName("cardsCache")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(2000)
                )
                .addMapConfig(
                        new MapConfig().setName("reviewsCache")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(2000)
                );
    }

}
