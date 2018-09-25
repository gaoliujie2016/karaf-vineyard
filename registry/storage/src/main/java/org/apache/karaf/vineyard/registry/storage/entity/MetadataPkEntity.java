/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.karaf.vineyard.registry.storage.entity;

import java.io.Serializable;

/**
 * A regular JPA entity, using JPA annotations.
 */
public class MetadataPkEntity implements Serializable {

    private String key;

    private String api;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MetadataPkEntity)) return false;
        MetadataPkEntity pk = (MetadataPkEntity) obj;
        return pk.key.equals(this.key) && pk.api.equals(this.api);
    }

}