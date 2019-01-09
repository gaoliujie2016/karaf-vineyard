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
package org.apache.karaf.vineyard.gateway;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.karaf.vineyard.common.API;
import org.apache.karaf.vineyard.common.GatewayService;
import org.apache.karaf.vineyard.common.Policy;
import org.apache.karaf.vineyard.common.RestResource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.util.TreeMap;

@Component(service = GatewayService.class, immediate = true)
public class GatewayServiceImpl implements GatewayService {

    private CamelContext camelContext = new DefaultCamelContext();

    @Activate
    public void activate() throws Exception {
        camelContext.start();
    }

    @Deactivate
    public void deactivate() throws Exception {
        camelContext.stop();
    }

    @Override
    public void publish(API api) throws Exception {
        for (RestResource restResource : api.getRestResources()) {
            publish(api, restResource);
        }
    }

    @Override
    public void delete(API api) throws Exception {
        for (RestResource restResource : api.getRestResources()) {
            remove(api, restResource);
        }
    }

    @Override
    public void publish(API api, RestResource restResource) throws Exception {
        String routeId = getRouteId(api, restResource);
        if (camelContext.getRoute(routeId) != null) {
            throw new IllegalArgumentException("API resource already published");
        }

        final RouteDefinition definition = new RouteDefinition();
        definition.from(
                "jetty:http://0.0.0.0:9090/vineyard" + api.getContext() + restResource.getPath());
        definition.log("Processing " + api.getContext() + restResource.getPath());
        definition.routeId(routeId);

        TreeMap<Integer, Policy> sortedPolicies = new TreeMap<>(restResource.getPolicies());
        for (Policy policy : sortedPolicies.values()) {
            Processor processor =
                    (Processor) Class.forName(policy.getClassName()).getConstructor().newInstance();
            definition.log("Adding policy " + policy.getClassName());
            definition.process(processor);
        }

        if (restResource.getResponse() != null) {
            definition.log("mocking response");
            definition.transform().constant(restResource.getResponse());
        } else {
            definition.log("proxying to endpoint " + restResource.getEndpoint());
            definition.to(restResource.getEndpoint());
        }

        RouteBuilder builder =
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        configureRoute(definition);
                    }
                };
        camelContext.addRoutes(builder);
    }

    @Override
    public void remove(API api, RestResource restResource) throws Exception {
        String routeId = getRouteId(api, restResource);
        if (camelContext.getRoute(routeId) == null) {
            throw new IllegalArgumentException("API resource not published");
        }
        camelContext.removeRoute(routeId);
    }

    @Override
    public void resume(API api, RestResource restResource) throws Exception {
        camelContext.resumeRoute(getRouteId(api, restResource));
    }

    @Override
    public void suspend(API api, RestResource restResource) throws Exception {
        camelContext.suspendRoute(getRouteId(api, restResource));
    }

    private String getRouteId(API api, RestResource resource) {
        return api.getId() + "-" + resource.getId();
    }
}