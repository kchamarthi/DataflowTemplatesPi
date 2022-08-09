/*
 * Copyright (C) 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.api.services.dataplex.v1.model;

/**
 * Batch compute resources associated with the task.
 *
 * <p>This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Dataplex API. For a detailed explanation see:
 * <a
 * href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources
    extends com.google.api.client.json.GenericJson {

  /** Optional. Total number of job executors. The value may be {@code null}. */
  @com.google.api.client.util.Key private java.lang.Integer executorsCount;

  /**
   * Optional. Max configurable executors. If max_executors_count > executors_count, then auto-
   * scaling is enabled. The value may be {@code null}.
   */
  @com.google.api.client.util.Key private java.lang.Integer maxExecutorsCount;

  /**
   * Optional. Total number of job executors.
   *
   * @return value or {@code null} for none
   */
  public java.lang.Integer getExecutorsCount() {
    return executorsCount;
  }

  /**
   * Optional. Total number of job executors.
   *
   * @param executorsCount executorsCount or {@code null} for none
   */
  public GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources setExecutorsCount(
      java.lang.Integer executorsCount) {
    this.executorsCount = executorsCount;
    return this;
  }

  /**
   * Optional. Max configurable executors. If max_executors_count > executors_count, then auto-
   * scaling is enabled.
   *
   * @return value or {@code null} for none
   */
  public java.lang.Integer getMaxExecutorsCount() {
    return maxExecutorsCount;
  }

  /**
   * Optional. Max configurable executors. If max_executors_count > executors_count, then auto-
   * scaling is enabled.
   *
   * @param maxExecutorsCount maxExecutorsCount or {@code null} for none
   */
  public GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources setMaxExecutorsCount(
      java.lang.Integer maxExecutorsCount) {
    this.maxExecutorsCount = maxExecutorsCount;
    return this;
  }

  @Override
  public GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources set(
      String fieldName, Object value) {
    return (GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources)
        super.set(fieldName, value);
  }

  @Override
  public GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources clone() {
    return (GoogleCloudDataplexV1TaskInfrastructureSpecBatchComputeResources) super.clone();
  }
}
