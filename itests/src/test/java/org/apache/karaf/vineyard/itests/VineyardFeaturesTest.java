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
import java.net.HttpURLConnection;
import java.net.URL;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class VineyardFeaturesTest extends VineyardTestSupport {

    private static final RolePrincipal[] ADMIN_ROLES = {
            new RolePrincipal("admin"),
            new RolePrincipal("manager")
    };

    @Test
    public void testVineyardRegistryFeatureInstall() throws InterruptedException {
        installVineyard();
        Thread.sleep(DEFAULT_TIMEOUT);
        System.out.println(executeCommand("feature:install vineyard-registry", ADMIN_ROLES));

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
            String URL = "http://localhost:8181/cxf/vineyard/registry/service";
            URL urlGetListServices = new URL(URL);

            // Call list service
            System.out.println("Call GET http://localhost:8181/cxf/vineyard/registry/service");
            HttpURLConnection connection = (HttpURLConnection) urlGetListServices.openConnection();
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
                    System.out.println("Services list is empty");
                } else {
                    System.out.println(sb.toString());
                }
            } else {
                System.out.println("Error when sending GET method : HTTP_CODE = " + connection.getResponseCode());
            }
        } catch (Exception e) {
            // nothing to do
        }
    }


    @After
    public void tearDown() {
        try {
            unInstallVineyard();
        } catch (Exception ex) {
            //Ignore
        }
    }

}
