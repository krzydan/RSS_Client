package rss_Client;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;


public class ChannelManagementView extends Main{

	    private static ChannelManagementView instance;
		private Table table;
		private static Shell shell;
		private Button deleteButton;
		private Button categoryButton;
		public List<String> added= new ArrayList<String>();
		public List<String> deleted = new ArrayList<String>();
		public List<String> channelTmpDeleted = new ArrayList<String>();
		boolean add;

		public void initGui(JSONArray urlArray, String login) {
			Display display = Display.getDefault();
			shell = new Shell(display);
			GridLayout gl=new GridLayout(2, true);
			gl.horizontalSpacing=50;
			shell.setLayout(gl);
			GridData gdlabelChannels=new GridData();
			gdlabelChannels.horizontalSpan=2;
			gdlabelChannels.horizontalAlignment=SWT.CENTER;
			gdlabelChannels.grabExcessHorizontalSpace=true;
			Label labelTitle=new Label(shell, SWT.NONE);
			labelTitle.setText("Zarządzanie kanałami RSS:");
			labelTitle.setLayoutData(gdlabelChannels);
			table=new Table(shell, SWT.BORDER+SWT.FULL_SELECTION);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			TableColumn tc=new TableColumn(table, SWT.NONE+SWT.BORDER);
			tc.setText("Id");
			tc.setWidth(50);
			TableColumn tc2=new TableColumn(table, SWT.BORDER);
			tc2.setText("URL kanału");
			tc2.setWidth(200);
			
			
			
			GridData  gdTable = new GridData(SWT.FILL, SWT.FILL, true, false);
			gdTable.horizontalSpan=1;
			gdTable.verticalSpan=8;
			gdTable.grabExcessVerticalSpace=true;
			table.setLayoutData( gdTable);
			
			Composite composite = new Composite(shell,SWT.FILL);
			GridLayout compositeGridLayout = new GridLayout(1,true);
			composite.setLayout(compositeGridLayout);
			Button addButton = new Button(composite,SWT.NONE);
			addButton.setText("Nowy kanał");
			
			addButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					AddForm af= new AddForm();
					af.init("channel",added);
					loadDataToTable(urlArray);
				}
			});
			
			deleteButton= new Button(composite, SWT.NONE);
			deleteButton.setText("Usuń kanał");
			
			deleteButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					deleteButton.setEnabled(false);
					categoryButton.setEnabled(false);
					String string = "";
					
			        deleted.add(table.getItem(table.getSelectionIndex()).getText(1));
			        table.remove(table.getSelectionIndex());
				}
			});
			
			categoryButton = new Button(composite, SWT.NONE);
			categoryButton.setText("Kategorie kanału");
			
			categoryButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e) {

					ClientConfig config = new ClientConfig();

			        Client client = ClientBuilder.newClient(config);

			        WebTarget target = client.target(getBaseURI());
			        try{
			        	
			        	String response = target.path("rest").path("user").path("channelsCategories").queryParam("login", login).queryParam("url", table.getItem(table.getSelectionIndex()).getText(1)).request().accept(MediaType.APPLICATION_JSON).get(String.class);
			        	JSONObject json = new JSONObject(response);
			 			JSONArray categoriesArray = json.getJSONArray("categories");
			 			 
			 			CategoryManagementView catmv= new CategoryManagementView();
			 			catmv.initGui(categoriesArray,login,table.getItem(table.getSelectionIndex()).getText(1));
			 			
			 		
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
			});
			
			Button confirmButton = new Button (composite, SWT.NONE);
			confirmButton.setText("Potwierdź zmiany");
			confirmButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					String addedString = "";
					String deletedString = "";
					for(int i =0;i<added.size();i++)
					{
						addedString+=added.get(i)+";";
					}
					for(int i =0;i<deleted.size();i++)
					{
						deletedString+=deleted.get(i)+";";
					}
					
			
					ClientConfig config = new ClientConfig();

			        Client client = ClientBuilder.newClient(config);

			        WebTarget target = client.target(getBaseURI());
			        try{
						 String plainAnswer = target.path("rest").path("user").path("channelsChanges").queryParam("login", login).queryParam("added", addedString).queryParam("deleted", deletedString).request().accept(MediaType.TEXT_PLAIN).get(String.class);
						 if(!plainAnswer.equals("Success"))
					     {
					    		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Błędny zapis. Spróbuj ponownie.");
								messageBox.open();
					     }
					 }
					 catch(javax.ws.rs.ProcessingException pe)
					 {
						 if(pe.getMessage().contains("Connection refused: connect"))
						 {
							 MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Błąd połączenia z serwerem. Spróbuj ponownie potem.");
								messageBox.open();
						 }
						 
					 }
					shell.close();
				}
			});
			
			table.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					deleteButton.setEnabled(true);
					categoryButton.setEnabled(true);
					
				}
			});

			
			shell.open ();
			loadDataToTable(urlArray);
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) 
					{
						display.sleep ();
					}
			}

		}


		public void loadDataToTable(JSONArray data){
			table.clearAll();
			channelTmpDeleted.removeAll(channelTmpDeleted);
			
			int[] idx = { 0 };
			data.forEach(url ->{
				
				deleteButton.setEnabled(false);
				categoryButton.setEnabled(false);
			
				TableItem ti=new TableItem(table, SWT.NONE);

				table.getItem(idx[0]).setText(0,""+idx[0]);
				table.getItem(idx[0]).setText(1,url.toString());
				idx[0]++;
				  });
			
			added.forEach(url ->{
				add = true;
				deleted.forEach(deletedCh->
				{
					if(url.toString().equals(deletedCh.toString()))
					{
						channelTmpDeleted.add(deletedCh);
						add=false;
					}
				});
				if(add)
				{
					TableItem ti=new TableItem(table, SWT.NONE);

					table.getItem(idx[0]).setText(0,""+idx[0]);
					table.getItem(idx[0]).setText(1,url.toString());
					idx[0]++;
				}
				
			 });
				
			if(channelTmpDeleted.size()>0)
			{
				channelTmpDeleted.forEach(deletedCat->
				{
					added.remove(deletedCat);
					deleted.remove(deletedCat);
				});
				
			}
		}
		
		public void setActive()
		{
			shell.forceActive();
		}
		
	
	    
	    //private constructor to avoid client applications to use constructor
	    ChannelManagementView(){
	    	instance = this;
	    }

	    public static ChannelManagementView getInstance(){
	    	
	    	return instance;
	        
	    }
			
}

		



