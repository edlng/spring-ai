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
import glide.api.models.configuration.NodeAddress;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.SpringAIVectorStoreTypes;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationConvention;
import org.springframework.ai.vectorstore.valkey.ValkeyVectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * {@link AutoConfiguration Auto-configuration} for Valkey Vector Store.
 *
 * @author Yongtae Kim
 */
@AutoConfiguration
@ConditionalOnClass({ GlideClient.class, ValkeyVectorStore.class, EmbeddingModel.class })
@EnableConfigurationProperties(ValkeyVectorStoreProperties.class)
@ConditionalOnProperty(name = SpringAIVectorStoreTypes.TYPE, havingValue = SpringAIVectorStoreTypes.VALKEY,
		matchIfMissing = true)
public class ValkeyVectorStoreAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	BatchingStrategy batchingStrategy() {
		return new TokenCountBatchingStrategy();
	}

	@Bean
	@ConditionalOnMissingBean
	GlideClientConfiguration glideClientConfiguration(ValkeyVectorStoreProperties properties) {
		return GlideClientConfiguration.builder()
			.clientName(properties.getClientName())
			.address(NodeAddress.builder().host(properties.getHost()).port(properties.getPort()).build())
			.build();
	}

	@Bean
	@ConditionalOnMissingBean(BaseClient.class)
	GlideClient glideClient(GlideClientConfiguration glideClientConfiguration) throws Exception {
		return GlideClient.createClient(glideClientConfiguration).get();
	}

	@Bean
	@ConditionalOnMissingBean
	ValkeyVectorStore vectorStore(EmbeddingModel embeddingModel, ValkeyVectorStoreProperties properties,
			BaseClient client, ObjectProvider<ObservationRegistry> observationRegistry,
			ObjectProvider<VectorStoreObservationConvention> convention, BatchingStrategy batchingStrategy) {

		return ValkeyVectorStore.builder(client, embeddingModel)
			.initializeSchema(properties.isInitializeSchema())
			.observationRegistry(observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP))
			.customObservationConvention(convention.getIfAvailable())
			.batchingStrategy(batchingStrategy)
			.indexName(properties.getIndexName())
			.prefix(properties.getPrefix())
			.contentFieldName(properties.getContentFieldName())
			.embeddingFieldName(properties.getEmbeddingFieldName())
			.vectorAlgorithm(properties.getVectorAlgorithm())
			.distanceMetric(properties.getDistanceMetric())
			.build();
	}

}
