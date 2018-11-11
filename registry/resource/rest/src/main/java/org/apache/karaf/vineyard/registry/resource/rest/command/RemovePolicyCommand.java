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
package org.apache.karaf.vineyard.registry.resource.rest.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.vineyard.common.ResourceRegistryService;

@Service
@Command(
        scope = "vineyard",
        name = "resource-rest-policy-remove",
        description = "Remove a Policy from a Rest Resource from the registry")
public class RemovePolicyCommand implements Action {

    @Reference private ResourceRegistryService resourceRegistryService;

    @Argument(
            index = 0,
            name = "resource",
            description = "Rest Resource id",
            required = true,
            multiValued = false)
    String idResource;

    @Option(name = "--policy", description = "Policy id", required = true, multiValued = false)
    String idPolicy;

    @Override
    public Object execute() throws Exception {
        resourceRegistryService.removePolicy(idResource, idPolicy);
        System.out.println(
                "The Policy " + idPolicy + " has been removed from the resource " + idResource);
        return null;
    }
}