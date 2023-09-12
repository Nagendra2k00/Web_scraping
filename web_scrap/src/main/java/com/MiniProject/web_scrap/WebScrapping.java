package com.MiniProject.web_scrap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;


//                   				 HTMLUnit
//The HtmlUnit library is a popular library for web scraping. It provides a number of classes and 
//methods for working with web pages, including the HtmlPage class.

//use record
class Players {
	
	public Players(String name, String id, Integer rating, Integer qrating) {
		// TODO Auto-generated constructor stub
		System.out.println("[Name = " + name + " , id = " + id + " , rating = " + rating + " , qrating = " + qrating + "]");
		
	}

}

public class WebScrapping {
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		//client :- think of it as web browser it will request web server to make HTTP requests
		@SuppressWarnings("resource")
		WebClient client = new WebClient();  
		
		
		//will disable JavaScript in the WebClient object. This means that the WebClient object will not execute 
		//any JavaScript code that is embedded in the web pages that it loads
		//the web browser we created doesn't know the java script
		client.getOptions().setJavaScriptEnabled(false);  
		
		
		//will disable CSS in the WebClient object. This means that the WebClient object will not render any CSS styles when it loads web pages.
		client.getOptions().setCssEnabled(false);
		
		
		//will use the WebClient object to load the web page at the specified URL. The web page will be returned as an HtmlPage object.
		//The HtmlPage object is a representation of a web page in the HtmlUnit library
		//The getPage() method of the WebClient object will make an HTTP request to the specified URL and return the response as an HtmlPage object.
		HtmlPage searchPage = client.getPage("https://new.uschess.org/player-search");
		
		
		//will get the first form element on the web page. The getByXPath("") method will return a 
		//list of all of the elements on the web page that match the specified XPath expression
		//The get(0) method will return the first element in the list. In this case, the first element in the list is a form element.
		HtmlForm form = (HtmlForm) searchPage.getByXPath("//form").get(0);
		
		
		// will get the input element with the specified name from the form element. 
		//The getInputByName("") method will return an HtmlInput object if there is an input element with the specified name in the form element. 
		//If there is no input element with the specified name in the form element, the method will return null.
		HtmlInput displayNameField = form.getInputByName("display_name");
		
		
		//will get the submit button element from the form element. 
		//The getInputByName("") method will return an HtmlInput object if there is a submit button element with the specified name in the form element. 
		//If there is no submit button element with the specified name in the form element, the method will return null.
		HtmlInput submitButton = form.getInputByName("op");
		
		
		//will set the value of the displayNameField input element to string. 
		//The type() method of the HtmlInput class is used to set the value of the input element.
		displayNameField.type("Magnus");
		
		
		//will submit the form element and return the results page. The click() method of the HtmlInput class is used to submit the form element.
		HtmlPage resultsPage = submitButton.click();
		
		
		//will parse the results page and return a list of players. The parseResults() method is a custom method that is used to parse the results page.
		//The parseResults() method will extract the player data from the results page and return a list of Players objects. 
		//A Players object is a data structure that represents a player.
		Set<Players> players = parseResults(resultsPage); 
		
		
		// will iterate over the list of players and print each player to the console. 
		//The for loop will iterate over the list of players and assign each player to the player variable. 
		//The System.out.println(player) statement will print the player variable to the console.
//		for (Object player : players) {
//			System.out.println(player);
//		}
	}

	
	//Custom method 
	private static Set<Players> parseResults(HtmlPage resultsPage) {
		
		//will get the first table element on the results page. 
		//The getByXPath("") method will return a list of all of the elements on the results page that match the specified XPath expression. 
		//The empty string is being used as the XPath expression because we want to get all of the tables on the results page.
		HtmlTable table = (HtmlTable) resultsPage.getByXPath("//table").get(0);
		
		
		//will extract the player data from the table and return a list of Players objects.
		//table.getBodies().get(0) :- The code first gets the first body of the table. The table can have multiple bodies, so we need to get the first body.
		//table.getBodies().get(0).getRows() :- we get all of the rows in the body.\
		//from .stream() till collect we iterate over the rows and extract the player data from each row.
		Set<Players> players = table.getBodies().get(0).getRows().stream()
				.map(r -> {
		String rating = r.getCell(2).getTextContent();
		String qrating = r.getCell(3).getTextContent();
		return new Players(
		r.getCell(0).getTextContent(),
		r.getCell(1).getTextContent(),
		rating.length() == 0 ? null : Integer.parseInt(rating),
		qrating.length() == 0 ? null : Integer.parseInt(qrating)
		);
		})
		//.collect(Collectors.toList()):-  method will collect the Players objects into a list.
		.collect(Collectors.toSet());
		return players;
	}

}
	
