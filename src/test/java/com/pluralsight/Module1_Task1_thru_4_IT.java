package com.pluralsight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import static org.junit.Assert.*;

import java.io.IOException;

public class Module1_Task1_thru_4_IT {
	private String CART_FORM_NAME = "cart_form";
	private String indexUrl;
	private WebClient webClient;
  HtmlPage firstPage = null;
  HtmlPage cartPage = null;
  HtmlForm listForm = null;
  HtmlForm cartForm = null;

	  @Before
	  public void setUp() throws IOException {
		// Turning off HTMLUnit Logging
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	      
	    indexUrl = "http://localhost:8080"; //System.getProperty("integration.base.url");
	    webClient = new WebClient();
			// Open the admin page
	    firstPage = webClient.getPage(indexUrl + "/books/list");

      // Get form
			try {
					listForm = firstPage.getFormByName(CART_FORM_NAME);
			} catch (ElementNotFoundException e) {}

      HtmlInput quantityInput = (HtmlInput)listForm.getInputByName("quantity");
      quantityInput.setAttribute("value", "1");
      HtmlSubmitInput submitButton = (HtmlSubmitInput)listForm.getInputByValue("Add to Cart");
      cartPage = submitButton.click();

      // Get form
			try {
					cartForm = cartPage.getFormByName(CART_FORM_NAME);
			} catch (ElementNotFoundException e) {}
	  }

	  @After
	  public void tearDown() {
	    webClient.close(); // closeAllWindows
	  }

    @Test
    public void _task1() {
      String errorMsg = "Form with name=\"" + CART_FORM_NAME + "\" does not exist.";
      assertNotNull(errorMsg, cartForm);

      String action = cartForm.getActionAttribute();
      String desiredAction = "/cart/update";
      errorMsg = "Form action is not \"" + desiredAction + "\".";
      assertEquals(errorMsg, desiredAction, action);
    }

    @Test
	  public void _task2() {
			assertNotNull("Form is null.", cartForm);
			//Get id input field
			try {
				HtmlInput inputIndex = cartForm.getInputByName("index");

				// Check if hidden
				String typeAttribute = inputIndex.getTypeAttribute();
				assertEquals("The index input needs type=\"hidden\".", "hidden", typeAttribute);

				// Check value is an int
				try {
					Integer.parseInt(inputIndex.getValueAttribute());
				} catch (NumberFormatException e) {
					assertTrue("The index input does not have an int for value.", false);
				}
			} catch (ElementNotFoundException e) {
				assertTrue("The input field with name \"index\" does not exist.", false);
			}
    }

    @Test
	  public void _task3() {
			assertNotNull("Form is null.", cartForm);
			//Get id input field
			try {
				HtmlInput inputQuantity = cartForm.getInputByName("quantity");

				// Check if hidden
				String typeAttribute = inputQuantity.getTypeAttribute();
				assertEquals("The quantity input needs type=\"number\".", "number", typeAttribute);

				// Check value is an int
				try {
					Integer.parseInt(inputQuantity.getValueAttribute());
				} catch (NumberFormatException e) {
					assertTrue("The quantity input does not have an int for value.", false);
				}
			} catch (ElementNotFoundException e) {
				assertTrue("The input field with name \"quantity\" does not exist.", false);
			}
    }

    @Test
	  public void _task4() {
      assertNotNull("Form is null.", cartForm);
      try {
        HtmlSubmitInput updateButton = (HtmlSubmitInput)cartForm.getInputByValue("Update");
      } catch (ElementNotFoundException e) {
				assertTrue("The submit input with value \"Update\" does not exist.", false);
			}

      try {
        HtmlSubmitInput deleteButton = (HtmlSubmitInput)cartForm.getInputByValue("Delete");
        String buttonXML = deleteButton.asXml();
        assertTrue("The formaction for Submit Input for Delete is incorrect." ,
                   buttonXML.contains("formaction=\"/cart/delete\""));
      } catch (ElementNotFoundException e) {
				assertTrue("The submit input with value \"Delete\" does not exist.", false);
			}

    }
}
