/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.karaf.vineyard.itests;

import org.apache.karaf.itests.KarafTestSupport;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.ws.rs.HttpMethod;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class VineyardRegistryRestTest extends KarafTestSupport {

    private static final RolePrincipal[] ADMIN_ROLES = {
            new RolePrincipal("admin"),
            new RolePrincipal("manager")
    };

    @Test
    public void testVineyardRegistryFeatureInstall() throws Exception {
        // adding vineyard features repository
        addFeaturesRepository("mvn:org.apache.karaf.vineyard/apache-karaf-vineyard/1.0.0-SNAPSHOT/xml");

        String featureList = executeCommand("feature:list");
        System.out.println(featureList);

        executeCommand("feature:install vineyard-registry", ADMIN_ROLES);
        installAndAssertFeature("vineyard-registry");

        String bundleList = executeCommand("bundle:list");
        System.out.println(bundleList);
        Assert.assertTrue(bundleList.contains("Apache Karaf :: Vineyard :: Common"));
        Assert.assertTrue(bundleList.contains("Apache Karaf :: Vineyard :: Registry :: API"));
        Assert.assertTrue(bundleList.contains("Apache Karaf :: Vineyard :: Registry :: Commands"));
        Assert.assertTrue(bundleList.contains("Apache Karaf :: Vineyard :: Registry :: REST"));
        Assert.assertTrue(bundleList.contains("Apache Karaf :: Vineyard :: Registry :: Storage"));

        String jdbcList = executeCommand("jdbc:ds-list");
        System.out.println(jdbcList);
        Assert.assertTrue(jdbcList.contains("jdbc:derby:data/vineyard/derby │ OK"));

        try {
            String URL = "http://localhost:8181/cxf/vineyard/registry/dataformat";
            URL urlDataformat = new URL(URL);

            // Call add dataformat service
            String soapAddDataformat = "{\n" +
                    "  \"name\": \"soap-xml\",\n" +
                    "  \"schema\": \"xml\",\n" +
                    "  \"sample\": \"soap-xml-sample\"\n" +
                    "}";
            System.out.println("Call POST http://localhost:8181/cxf/vineyard/registry/dataformat");
            HttpURLConnection connection = (HttpURLConnection) urlDataformat.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            os.write(soapAddDataformat.getBytes());
            os.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Assert.assertTrue(true);
            } else {
                System.out.println("Error when sending POST method : HTTP_CODE = " + connection.getResponseCode());
                Assert.assertTrue(false);
            }

            // Call add dataformat service
            String jsonAddDataformat = "{\n" +
                    "  \"name\": \"json\",\n" +
                    "  \"schema\": \"json\",\n" +
                    "  \"sample\": \"json-sample\"\n" +
                    "}";
            System.out.println("Call POST http://localhost:8181/cxf/vineyard/registry/dataformat");
            connection = (HttpURLConnection) urlDataformat.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            os = connection.getOutputStream();
            os.write(jsonAddDataformat.getBytes());
            os.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Assert.assertTrue(true);
            } else {
                System.out.println("Error when sending POST method : HTTP_CODE = " + connection.getResponseCode());
                Assert.assertTrue(false);
            }

            // Call list dataformat
            System.out.println("Call GET http://localhost:8181/cxf/vineyard/registry/dataformat");
            connection = (HttpURLConnection) urlDataformat.openConnection();
            connection.setRequestMethod(HttpMethod.GET);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
                if (sb.length() == 0) {
                    System.out.println("Dataformat list is empty");
                    Assert.assertTrue(false);
                } else {
                    System.out.println(sb.toString());
                    Assert.assertTrue(true);
                }
            } else {
                System.out.println("Error when sending GET method : HTTP_CODE = " + connection.getResponseCode());
                Assert.assertTrue(false);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(false);
        }

        try {
            String URL = "http://localhost:8181/cxf/vineyard/registry/api";
            URL urlDataformat = new URL(URL);

            // Call add rest-api
            String jsonAddRestApi = "{\n" +
                    "  \"name\": \"authenticate service\",\n" +
                    "  \"context\": \"api/authenticate\"\n" +
                    "  \"description\": \"use to authenticate user with token\",\n" +
                    "  \"version\": \"1.0.0\"" +
                    "}";
            System.out.println("Call POST http://localhost:8181/cxf/vineyard/registry/api");
            HttpURLConnection connection = (HttpURLConnection) urlDataformat.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            os.write(jsonAddRestApi.getBytes());
            os.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Assert.assertTrue(true);
            } else {
                System.out.println("Error when sending POST method : HTTP_CODE = " + connection.getResponseCode());
                Assert.assertTrue(false);
            }

            // Call list rest-api
            System.out.println("Call GET http://localhost:8181/cxf/vineyard/registry/api");
            connection = (HttpURLConnection) urlDataformat.openConnection();
            connection.setRequestMethod(HttpMethod.GET);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
                if (sb.length() == 0) {
                    System.out.println("api list is empty");
                    Assert.assertTrue(false);
                } else {
                    System.out.println(sb.toString());
                    Assert.assertTrue(true);
                }
            } else {
                System.out.println("Error when sending GET method : HTTP_CODE = " + connection.getResponseCode());
                Assert.assertTrue(false);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(false);
        }
    }

    @After
    public void tearDown() {
        executeCommand("feature:uninstall vineyard-registry", ADMIN_ROLES);
    }
}
