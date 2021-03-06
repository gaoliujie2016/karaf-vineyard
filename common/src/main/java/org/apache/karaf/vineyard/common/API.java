/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.vineyard.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** This is a API view, containing attributes common to any concrete API in Vineyard. */
public class API {

    /** Unique API ID */
    private String id;

    /** Name of the API */
    private String name;

    /** Base context of this API */
    private String context;

    /** Open text describing the API */
    private String description;

    /** Additional data functional and technical related to the API */
    private Map<String, String> meta = new HashMap<>();

    /** Collection of resources linked to this API. */
    private Collection<RestResource> restResources = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> metadata) {
        this.meta = metadata;
    }

    public Collection<RestResource> getRestResources() {
        return restResources;
    }

    public void setRestResources(Collection<RestResource> restResources) {
        this.restResources = restResources;
    }
}
