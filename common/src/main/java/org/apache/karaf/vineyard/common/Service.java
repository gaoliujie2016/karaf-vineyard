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

import java.util.List;
import java.util.Map;

/**
 * Describe a service registered in Karaf Vineyard.
 */
public class Service {

    /** Unique service ID */
    public String id;

    /** Human readable name/alias for the service */
    public String name;

    /** Description of the service */
    public String description;

    /** Description of this service on different environments */
    public List<ServiceOnEnvironment> environments;

}
