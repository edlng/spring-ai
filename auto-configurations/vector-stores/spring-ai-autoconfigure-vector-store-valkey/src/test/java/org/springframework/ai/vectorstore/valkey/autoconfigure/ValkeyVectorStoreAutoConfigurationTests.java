/*
 * Copyright 2023-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.vectorstore.valkey.autoconfigure;

import glide.api.BaseClient;
import glide.api.GlideClient;
import glide.api.models.configuration.GlideClientConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.valkey.ValkeyVectorStore;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yongtae Kim
 */
class ValkeyVectorStoreAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(ValkeyVectorStoreAutoConfiguration.class))
		.withUserConfiguration(Config.class, BaseClientConfig.class);

	@Test
	void autoConfigurationEnabledByDefault() {
		this.contextRunner.run(context -> {
			assertThat(context.getBeansOfType(ValkeyVectorStoreProperties.class)).isNotEmpty();
			assertThat(context.getBeansOfType(GlideClientConfiguration.class)).isNotEmpty();
			assertThat(context.getBeansOfType(GlideClient.class)).isEmpty();
			assertThat(context.getBeansOfType(VectorStore.class)).isNotEmpty();
			assertThat(context.getBean(VectorStore.class)).isInstanceOf(ValkeyVectorStore.class);
		});
	}

	@Test
	void autoConfigurationEnabledWhenTypeIsValkey() {
		this.contextRunner.withPropertyValues("spring.ai.vectorstore.type=valkey").run(context -> {
			assertThat(context.getBeansOfType(ValkeyVectorStoreProperties.class)).isNotEmpty();
			assertThat(context.getBeansOfType(VectorStore.class)).isNotEmpty();
			assertThat(context.getBean(VectorStore.class)).isInstanceOf(ValkeyVectorStore.class);
		});
	}

	@Test
	void autoConfigurationDisabledWhenTypeIsNone() {
		this.contextRunner.withPropertyValues("spring.ai.vectorstore.type=none").run(context -> {
			assertThat(context.getBeansOfType(ValkeyVectorStoreProperties.class)).isEmpty();
			assertThat(context.getBeansOfType(GlideClient.class)).isEmpty();
			assertThat(context.getBeansOfType(VectorStore.class)).isEmpty();
		});
	}

	@Test
	void glideClientUsesDefaultClientName() {
		this.contextRunner.run(context -> {
			GlideClientConfiguration configuration = context.getBean(GlideClientConfiguration.class);

			assertThat(configuration.getClientName()).isEqualTo(ValkeyVectorStore.DEFAULT_CLIENT_NAME);
		});
	}

	@Test
	void glideClientUsesConfiguredClientName() {
		this.contextRunner.withPropertyValues("spring.ai.vectorstore.valkey.client-name=my-client").run(context -> {
			GlideClientConfiguration configuration = context.getBean(GlideClientConfiguration.class);

			assertThat(configuration.getClientName()).isEqualTo("my-client");
		});
	}

	@Test
	void backsOffWhenBaseClientIsProvided() {
		this.contextRunner.withUserConfiguration(BaseClientConfig.class).run(context -> {
			assertThat(context.getBeansOfType(GlideClient.class)).isEmpty();
			assertThat(context.getBean(BaseClient.class)).isSameAs(context.getBean("baseClient"));
			assertThat(context.getBean(VectorStore.class)).isInstanceOf(ValkeyVectorStore.class);
		});
	}

	@Configuration(proxyBeanMethods = false)
	static class Config {

		@Bean
		EmbeddingModel embeddingModel() {
			return Mockito.mock(EmbeddingModel.class);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class BaseClientConfig {

		@Bean
		BaseClient baseClient() {
			return Mockito.mock(BaseClient.class);
		}

	}

}
