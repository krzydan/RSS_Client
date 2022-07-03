package rss_Client;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static org.apache.commons.lang3.StringEscapeUtils.*;
import org.jsoup.*;

import models.Account;


public class Main {

	
	
	protected static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/RSS_Server").build();
    }

	public static void main(String[] args) {
		
		Display display = new Display ();
		Shell shell = new Shell (display);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		shell.setLayout (gl);
		ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);

        WebTarget target = client.target(getBaseURI());

        

		Label loginLabel = new Label(shell, SWT.NONE);
		loginLabel.setText("Login");
		Text loginText = new Text (shell, SWT.BORDER);
		
		Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setText("Hasło");
		Text passwordText = new Text (shell, SWT.BORDER);
		passwordText.setEchoChar('*');
		
		Button registerButton = new Button (shell, SWT.CHECK);
		registerButton.setText("Nie mam konta");
		
		
		
		Button loginButton = new Button (shell,SWT.CENTER);
		loginButton.setText("  Zaloguj  ");
		
		Label emailLabel = new Label(shell, SWT.NONE);
		emailLabel.setText("E-mail");
		Text emailText = new Text (shell, SWT.BORDER);
		emailLabel.setVisible(false);
		emailText.setVisible(false);
		
		registerButton.addListener (SWT.Selection, e -> {
			if (registerButton.getSelection()) {
				emailLabel.setVisible(true);
				emailText.setVisible(true);
				loginButton.setText("Zarejestruj");
				
			} else {
				emailLabel.setVisible(false);
				emailText.setVisible(false);
				loginButton.setText("Zaloguj");
			}
		});
		
		loginButton.addListener (SWT.Selection, e -> {
			if(loginText.getText()==""||passwordText.getText()=="")
			{
				if(loginText.getText()==""){ 
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING );
					messageBox.setMessage("Podaj login");
					messageBox.open();
				}
				
				if(passwordText.getText()==""){
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING );
					messageBox.setMessage("Podaj hasło");
					messageBox.open();
				}
			}
			
			else if(!registerButton.getSelection())
			{
				
					try{
						 String plainAnswer = target.path("rest").path("user").queryParam("login", loginText.getText()).queryParam("password", hash(passwordText.getText())).request().accept(MediaType.TEXT_PLAIN).get(String.class);
						 if(plainAnswer.equals("login"))
					     {
							 	String login = loginText.getText();
					    	 	shell.close();
						        display.dispose();
						        MainWindow mw = new MainWindow();
						        mw.initGui(login);
					     }else
					     {
					    		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Błędne hasło lub login. Spróbuj ponownie.");
								messageBox.open();
					     }
					 }
					 catch(javax.ws.rs.ProcessingException pe)
					 {
						 if(pe.getMessage().contains("Connection refused: connect"))
						 {
							 MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Błąd połączenia. Spróbuj ponownie potem.");
								messageBox.open();
						 }
						 
					 }
			     
			}
			else
			{
				if(emailText.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
				{
					Object user = null;
					try{
					
						String responseString = target.path("rest").path("user").path("register").queryParam("login", loginText.getText()).queryParam("password", hash(passwordText.getText())).queryParam("email", emailText.getText()).request().accept(MediaType.TEXT_PLAIN).get(String.class);
					
					
						if(responseString.equals("Success")) {
							String login = loginText.getText();
				    	 	shell.close();
					        display.dispose();
					        MainWindow mw = new MainWindow();
					        mw.initGui(login);
						}
						else if(responseString.equals("Mail"))
						{
							MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
							messageBox.setMessage("Istnieje już użytkownik o podanym mailu.");
							messageBox.open();
						}
						else if(responseString.equals("Nick"))
						{
							MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
							messageBox.setMessage("Istnieje już użytkownik o podanym nicku.");
							messageBox.open();
						}
					}
					catch(javax.ws.rs.ProcessingException pe)
					{
						if(pe.getMessage().contains("Connection refused: connect"))
						{
							MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
							messageBox.setMessage("Błąd połączenia. Spróbuj ponownie potem.");
							messageBox.open();
						}
					}
				}
				else
				{
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING );
					messageBox.setMessage("Błędny adres e-mail.");
					messageBox.open();
				}
			}
	        

		});
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	
	public static String hash(String password) { //hashing the password user created
	    try {
	        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
	        String salt = "some_random_salt";
	        String passWithSalt = password + salt;
	        byte[] passBytes = passWithSalt.getBytes();
	        byte[] passHash = sha256.digest(passBytes);             
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< passHash.length ;i++) {
	            sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));         
	        }
	        String generatedPassword = sb.toString();
	        return generatedPassword;
	    } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }       
	    return null;
	}
	
	
}