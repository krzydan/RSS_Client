package rss_Klient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;



public class MainWindow extends Main{
	private static JSONArray urlArray; 

	protected Boolean manage;
	private static List <String> titles;
	private ChannelManagementView cmv;
	private static List<News> newsList;
	private String login;
	private Shell shell;
	public void initGui(String username)
	{
		login = username;
		newsList = new ArrayList<News>();
		Display display = new Display().getDefault();
		shell = new Shell (display);
		GridLayout gl = new GridLayout();
		shell.setLayout (gl);
		shell.setLayout(new FillLayout());
		ClientConfig config = new ClientConfig();
		
		ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());
        Menu menu = new Menu (shell, SWT.BAR);
    	shell.setMenuBar (menu);
    	MenuItem channelItem = new MenuItem (menu, SWT.CASCADE);
    	channelItem.setText ("&Kanały");
    	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
    	channelItem.setMenu (submenu);
    	manage = false;
    	MenuItem item = new MenuItem (submenu, SWT.PUSH);
    	item.addListener (SWT.Selection, e -> 
    	{

    		try {
    			if(ChannelManagementView.getInstance()==null)
        		{
        			cmv = new ChannelManagementView();
        			cmv.initGui(urlArray,login);
        			loadNewsChannels(target);
        			loadNews(bar);
        		}
        		else
        		{
        			cmv.setActive();
        		}
    		}catch( org.eclipse.swt.SWTException eswt)
    		{
    			System.out.println(eswt.getMessage());
    			cmv = new ChannelManagementView();
    			cmv.initGui(urlArray,login);
    			loadNewsChannels(target);
    			loadNews(bar);
    		}
        		
        		manage = true;


    	});
    	item.setText ("Zarządzaj kanałami \tCtrl+M");
    	item.setAccelerator (SWT.MOD1 + 'M');
    
        loadNewsChannels(target);

		//readRSS("http://rss.cnn.com/rss/edition.rss");
		//readRSS("https://www.cdaction.pl/rss_newsy.xml");
		//readRSS("https://www.tvn24.pl/najnowsze.xml");
		
		
	
		loadNews(bar);


		shell.open ();
		shell.setMaximized(true);
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	
		
		//
		//readRSS("https://www.cdaction.pl/rss_newsy.xml");
		//readRSS("https://www.eurogamer.pl/?format=rss");
		
		//readRSS("https://fakty.interia.pl/feed");
		//kategorie
		//readRSS("https://oko.press/feed");		
		//readRSS("http://www.rfi.fr/general/rss");
		

		//zly odczyt
		//readRSS("http://feeds.feedburner.com/niebezpiecznik?format=xml");
				
		for (int i =0; i <newsList.size();i++)
		{
			//System.out.println(newsList.get(i).getTitle());
			//System.out.println(newsList.get(i).getDescription());
			//System.out.println(newsList.get(i).getCategory());
			//System.out.println(newsList.get(i).getChannel());
		}
	}
	
	private void loadNewsChannels(WebTarget target) { //wczytanie kanalow do tablicy
		try{
			 String response = target.path("rest").path("user").path("channels").queryParam("login", login).request().accept(MediaType.APPLICATION_JSON).get(String.class);

			 
			 JSONObject json = new JSONObject(response);
			urlArray = json.getJSONArray("url");
			urlArray.forEach(url ->{
				readRSS(url.toString());
				  });

			
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

	public static void readRSS(String urlString)
	{	
		try {
			String sourceCode ="";
			URL rssURL = new URL(urlString);
			URLConnection urlCon = rssURL.openConnection();
			urlCon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line;
			while((line=in.readLine())!=null)
			{
				sourceCode+=line;
			}
			titles = new ArrayList<String>();
			String[] newsArray = sourceCode.split("<item>");
			String channel = getTag(newsArray[0],"title");
			for (int i = 1; i <newsArray.length;i++)  // indeks od 1, ponieważ 0 to informacje o kanale
			{

				News news = new News();
				news.setTitle(getTag(newsArray[i], "title"));
				news.setLink(getTag(newsArray[i],"link"));
				news.setDescription(getTag(newsArray[i],"description"));
				DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH); 
				news.setPubDate(format.parse(getTag(newsArray[i],"pubDate")));
				news.setCategory(getTag(newsArray[i],"category"));
				news.setChannel(channel);
				news.setImage(getTag(newsArray[i],"img"));
				newsList.add(news);
			}
			in.close();
		
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong reading the contents");
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static String getTag(String text, String tag)//funkcja zwraca tekst pomiedzy znacznikami 
	{
			
			String tempTag="";
			if (tag=="img")
			{
				tempTag="img";
				tag="description"; //ponieważ link do zdjecia znajduje sie w znaczniku description
				if(text.contains("media:group")) 
					{
							tag = "media:group";
							tempTag="media:content";
					}
			}
			String result="";
			int firstPos = text.indexOf("<"+tag+">");
			int lastPosTemp = text.lastIndexOf("</"+tag+">");
			if(firstPos==-1||lastPosTemp==-1)
			{
				return result;
			}
			if (StringUtils.countMatches(text, "<"+tag+">")>1)
			{
				//System.out.println(StringUtils.countMatches(text, "<"+tag+">"));
			}
			result =  text.substring(firstPos);
			//kategoria może wystapic kilka razy

			result = result.replaceFirst("<"+tag+">", "");
			if(tag=="category") result = result.replace("<"+tag+">", ";");
			int lastPos = result.lastIndexOf("</"+tag+">");
			result = result.substring(0, lastPos);
			//System.out.println(result);
			
			if (tag =="title"||tag=="description"||tag=="category"||tag == "media:group")
				{result= result.replace("]]>", "").replace("<![CDATA[", "");
				result = getImgURL(tempTag,tag,result);
				result = unescapeHtml4(result);
				result = Jsoup.parse(result).text();
				}
			return result;

	}
	public static String getImgURL(String tempTag, String tag, String result) // funkcja pozyskuje adres URL pierwszego obrazka dołączonego do wiadomości
	{
		try 
		{
			if(tempTag =="img")
			{
				int index = result.indexOf("src=");
				int howMany = result.substring(index).split(" ").length;
				for(int i = 0; i<howMany; i++)
				{
					if(result.substring(index).split(" ")[i].contains("src="))
					{
						result = result.substring(index).split(" ")[i];
						break;
					};
				}
				result=result.replace("src=", "").replaceAll("\"", "");
				//System.out.println(result);
			}
			else if(tempTag == "media:content")
			{
				int index = result.indexOf("url=");
				int howMany = result.substring(index).split(" ").length;
				for(int i = 0; i<howMany; i++)
				{
					if(result.substring(index).split(" ")[i].contains("url="))
					{
						result = result.substring(index).split(" ")[i];
						break;
					};
				}
				result=result.replace("url=", "").replaceAll("\"", "");
			}
			return result;
		}catch(java.lang.StringIndexOutOfBoundsException e)
		{
			return  "";
		}
		
	}
	
	public void loadNews(ExpandBar bar)
	{
		
		ExpandItem[] items = bar.getItems();
		for(ExpandItem item:items)
		{
			item.dispose();
		}
		
		for (int i = 0; i< newsList.size();i++)
		{
			Composite composite = new Composite (bar, SWT.NONE);
			GridLayout layout = new GridLayout ();
			layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
			layout.verticalSpacing = 10;
			composite.setLayout(layout);
			Label date = new Label(composite, SWT.NONE);
			Date dateDate = newsList.get(i).getPubDate();
			DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH); 
			date.setText(format.format(dateDate));
			Label text = new Label(composite, SWT.WRAP);
			text.setText(newsList.get(i).getDescription());
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			if(newsList.get(i).getImage().length()>0)
			   {
			     final Browser browser = new Browser(composite, SWT.NONE);
			     browser.setUrl(newsList.get(i).getImage());
			     browser.setLayoutData(new GridData(GridData.FILL_BOTH));
			   }
			final Link link = new Link (composite, SWT.NONE);
			link.setText("<a href=\""+newsList.get(i).getLink()+"\">Czytaj więcej... </a>");
			link.addSelectionListener(new SelectionAdapter()  {
				 
			    @Override
			    public void widgetSelected(SelectionEvent e) {
			    	String s = link.getText().substring(link.getText().indexOf("http") );
			    	System.out.println(s);
			    	s = s.substring(0, s.indexOf("\">"));
			    	System.out.println(s);
			    	Program.launch(s);
			    }
			     
			});
	
		
			ExpandItem item0 = new ExpandItem (bar, SWT.NONE, 0);
			item0.setText(newsList.get(i).getTitle());
			item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			item0.setControl(composite);
		}
		
	}
}
