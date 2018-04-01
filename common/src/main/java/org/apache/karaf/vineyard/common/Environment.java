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

import java.util.Map;

/**
 * Describe an environment where services are attached to.
 */
public class Environment {

    /** The environment unique ID */
    private String id;

    /** Name of the environment */
    private String name;

    /** Description of the environment */
    private String description;

    /** Scope of the environment (on cloud, on prem, ...) */
    private String scope;

    /** The environment maintainers with roles */
    private Map<String, Role> maintainers;

    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Map<String, Role> getMaintainers() {
        return maintainers;
    }

    public void setMaintainers(Map<String, Role> maintainers) {
        this.maintainers = maintainers;
    }
}
