package eu.tuxoo.integrationtest;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AngularApp1Test {

    @Test
    public void testDynContent() {

        // init webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setTimeout(10000);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);

        try {
            HtmlPage page = webClient.getPage("http://localhost:8080/angularapp1.html");
            webClient.waitForBackgroundJavaScript(10000);

            DomElement domElement = page.getElementById("sometext");
            HtmlTextInput htmlTextInput = (HtmlTextInput) domElement;
            htmlTextInput.setValueAttribute("You!");

            assertEquals("Hello You!", page.getElementById("out").asText());
        } catch (IOException ioe){
            System.err.println(ioe.toString());
            ioe.printStackTrace();
        } finally {
            webClient.closeAllWindows();
            for (final String alert : collectedAlerts) {
                System.err.println("ALERT: " + alert);
            }
        }

    }

}