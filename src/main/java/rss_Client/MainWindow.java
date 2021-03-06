package rss_Client;

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
    	channelItem.setText ("&Kana??y");
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
    	item.setText ("Zarz??dzaj kana??ami \tCtrl+M");
    	item.setAccelerator (SWT.MOD1 + 'M');
    
        loadNewsChannels(target);
	
		loadNews(bar);


		shell.open ();
		shell.setMaximized(true);
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();

				
	}
	
	private void loadNewsChannels(WebTarget target) {//loading channels to an array
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
					messageBox.setMessage("B????d po????czenia. Spr??buj ponownie potem.");
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
			for (int i = 1; i <newsArray.length;i++)  // looping from 1 because i=0 are informations about the channel
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
	
	
	public static String getTag(String text, String tag)//the funtion returns text between tags
	{
			
			String tempTag="";
			if (tag=="img")
			{
				tempTag="img";
				tag="description"; //link to the picture is contained in description tag
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

			result =  text.substring(firstPos);
			//one category may show up multiple times

			result = result.replaceFirst("<"+tag+">", "");
			if(tag=="category") result = result.replace("<"+tag+">", ";");
			int lastPos = result.lastIndexOf("</"+tag+">");
			result = result.substring(0, lastPos);
			
			if (tag =="title"||tag=="description"||tag=="category"||tag == "media:group")
				{result= result.replace("]]>", "").replace("<![CDATA[", "");
				result = getImgURL(tempTag,tag,result);
				result = unescapeHtml4(result);
				result = Jsoup.parse(result).text();
				}
			return result;

	}
	public static String getImgURL(String tempTag, String tag, String result) // function gest URL of the first image from the post
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
			link.setText("<a href=\""+newsList.get(i).getLink()+"\">Czytaj wi??cej... </a>");
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
