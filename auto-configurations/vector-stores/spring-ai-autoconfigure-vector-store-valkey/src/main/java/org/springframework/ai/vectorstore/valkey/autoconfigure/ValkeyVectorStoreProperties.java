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

import glide.api.models.commands.FT.FTCreateOptions.DistanceMetric;

import org.springframework.ai.vectorstore.properties.CommonVectorStoreProperties;
import org.springframework.ai.vectorstore.valkey.ValkeyVectorStore;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Valkey Vector Store.
 *
 * @author Yongtae Kim
 */
@ConfigurationProperties(ValkeyVectorStoreProperties.CONFIG_PREFIX)
public class ValkeyVectorStoreProperties extends CommonVectorStoreProperties {

	public static final String CONFIG_PREFIX = "spring.ai.vectorstore.valkey";

	private String host = "localhost";

	private int port = 6379;

	private String clientName = ValkeyVectorStore.DEFAULT_CLIENT_NAME;

	private String indexName = ValkeyVectorStore.DEFAULT_INDEX_NAME;

	private String prefix = ValkeyVectorStore.DEFAULT_PREFIX;

	private String contentFieldName = ValkeyVectorStore.DEFAULT_CONTENT_FIELD_NAME;

	private String embeddingFieldName = ValkeyVectorStore.DEFAULT_EMBEDDING_FIELD_NAME;

	private ValkeyVectorStore.Algorithm vectorAlgorithm = ValkeyVectorStore.DEFAULT_VECTOR_ALGORITHM;

	private DistanceMetric distanceMetric = ValkeyVectorStore.DEFAULT_DISTANCE_METRIC;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getIndexName() {
		return this.indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getContentFieldName() {
		return this.contentFieldName;
	}

	public void setContentFieldName(String contentFieldName) {
		this.contentFieldName = contentFieldName;
	}

	public String getEmbeddingFieldName() {
		return this.embeddingFieldName;
	}

	public void setEmbeddingFieldName(String embeddingFieldName) {
		this.embeddingFieldName = embeddingFieldName;
	}

	public ValkeyVectorStore.Algorithm getVectorAlgorithm() {
		return this.vectorAlgorithm;
	}

	public void setVectorAlgorithm(ValkeyVectorStore.Algorithm vectorAlgorithm) {
		this.vectorAlgorithm = vectorAlgorithm;
	}

	public DistanceMetric getDistanceMetric() {
		return this.distanceMetric;
	}

	public void setDistanceMetric(DistanceMetric distanceMetric) {
		this.distanceMetric = distanceMetric;
	}

}
