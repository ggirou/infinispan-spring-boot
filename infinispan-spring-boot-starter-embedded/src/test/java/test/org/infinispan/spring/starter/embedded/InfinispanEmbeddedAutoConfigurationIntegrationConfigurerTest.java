package test.org.infinispan.spring.starter.embedded;

import java.util.Collections;

import org.infinispan.configuration.cache.StorageType;
import org.infinispan.eviction.EvictionType;
import org.infinispan.spring.starter.embedded.InfinispanEmbeddedAutoConfiguration;
import org.infinispan.spring.starter.embedded.InfinispanEmbeddedCacheManagerAutoConfiguration;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.org.infinispan.spring.starter.embedded.testconfiguration.InfinispanCacheTestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
    classes = {
        InfinispanEmbeddedAutoConfiguration.class,
        InfinispanEmbeddedCacheManagerAutoConfiguration.class,
        InfinispanCacheTestConfiguration.class
    },
    properties = {
        "spring.main.banner-mode=off"
    }
)
public class InfinispanEmbeddedAutoConfigurationIntegrationConfigurerTest {

   @Autowired
   EmbeddedCacheManager defaultCacheManager;

   @Test
   public void contextLoads() {

   }

   @Test
   public void testWithCacheConfigurer() {
      assertThat(defaultCacheManager.getCacheNames()).isEqualTo(
            Collections.singleton(InfinispanCacheTestConfiguration.TEST_CACHE_NAME));

      final Configuration testCacheConfiguration = defaultCacheManager.getCacheConfiguration(InfinispanCacheTestConfiguration.TEST_CACHE_NAME);
      assertThat(testCacheConfiguration.jmxStatistics().enabled()).isTrue();
      assertThat(testCacheConfiguration.memory().storageType()).isEqualTo(StorageType.OBJECT);
      assertThat(testCacheConfiguration.memory().evictionType()).isEqualTo(EvictionType.COUNT);
      assertThat(testCacheConfiguration.memory().evictionStrategy()).isEqualTo(EvictionStrategy.MANUAL);
   }

   @Test
   public void testWithGlobalConfigurer() {
      final GlobalConfiguration globalConfiguration = defaultCacheManager.getCacheManagerConfiguration();

      assertThat(globalConfiguration.globalJmxStatistics().enabled()).isTrue();
      assertThat(globalConfiguration.globalJmxStatistics().domain()).isEqualTo(InfinispanCacheTestConfiguration.TEST_GLOBAL_JMX_DOMAIN);
      assertThat(globalConfiguration.transport().clusterName()).isEqualTo(InfinispanCacheTestConfiguration.TEST_CLUSTER);
   }
}
