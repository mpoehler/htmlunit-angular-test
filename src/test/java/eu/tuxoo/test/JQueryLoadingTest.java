/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.tuxoo.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

public class JQueryLoadingTest {

    private static final String BROWSER_VERSION = BrowserType.CHROME;

    @Test
    public void testLoadingJQuery1_7_0() throws Exception {
      doTestLoadingJQuery("1.7.0", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_7_1() throws Exception {
      doTestLoadingJQuery("1.7.1", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_7_2() throws Exception {
      doTestLoadingJQuery("1.7.2", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_8_0() throws Exception {
      doTestLoadingJQuery("1.8.0", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_8_1() throws Exception {
      doTestLoadingJQuery("1.8.1", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_8_2() throws Exception {
      doTestLoadingJQuery("1.8.2", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_8_3() throws Exception {
      doTestLoadingJQuery("1.8.3", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_9_0() throws Exception {
      doTestLoadingJQuery("1.9.0", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_9_1() throws Exception {
      doTestLoadingJQuery("1.9.1", BROWSER_VERSION);
    }

    @Test
    public void testLoadingJQuery1_10_2() throws Exception {
      doTestLoadingJQuery("1.10.2", BROWSER_VERSION);
    }

    
    public void doTestLoadingJQuery(String jQueryVersion, String browserVersion) throws Exception {
        String html
                = "<!DOCTYPE HTML><html><head><title>foo</title>" + getJqueryScript(jQueryVersion) + "<script>\n"
                + "alert(window.jQuery !== undefined);\n"
                + "</script></head><body>\n"
                + "</body></html>";

        String initialUrl = "http://localhost:8080/myDummyApp/index.html";
        CollectingAlertHandler alertHandler = new CollectingAlertHandler();

        HtmlUnitDriver driver = createDriver(alertHandler, browserVersion, new StringWebResponse(html, new URL(initialUrl)));

        // load the mock html page
        driver.get(initialUrl);
        assertThat("jQuery did not load succesfully.",
                alertHandler.getCollectedAlerts().get(0), containsString("true"));
    }

    private HtmlUnitDriver createDriver(final CollectingAlertHandler alertHandler, final String browserVersion, final StringWebResponse... mockResponses) {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setBrowserName(BrowserType.HTMLUNIT);
        dc.setVersion(browserVersion);
        return new HtmlUnitDriver(dc) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                client.setAlertHandler(alertHandler);
                client.setWebConnection(new DelegatingWebConnection(new HttpWebConnection(client), mockResponses));
                client.getOptions().setJavaScriptEnabled(true);
                return client;
            }
        };
    }

    private String getJqueryScript(String version) {
        return "<script src=\"" + getJQueryUrl(version) + "\"></script>";
    }

    private String getJQueryUrl(String version) {
        return "http://code.jquery.com/jquery-" + version + ".js";
    }

    /**
     * Serve canned responses for certain URLs, and perform real fetches for others.
     */
    private static class DelegatingWebConnection implements WebConnection {

        private final WebConnection delegate;
        private final Map<URL, WebResponse> mockResponses;

        private DelegatingWebConnection(WebConnection delegate, StringWebResponse... mockResponses) {
            this.delegate = delegate;
            this.mockResponses = new HashMap<URL, WebResponse>();
            for (StringWebResponse mockWebResponse : mockResponses) {
                this.mockResponses.put(mockWebResponse.getWebRequest().getUrl(), mockWebResponse);
            }
        }

        public WebResponse getResponse(WebRequest request) throws IOException {
            URL key = request.getUrl();
            if (mockResponses.containsKey(key)) {
                return mockResponses.get(key);
            } else {
                return delegate.getResponse(request);
            }
        }
    }
}