package rss_Klient;


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


public class CategoryManagementView extends Main{

	    private static CategoryManagementView instance;
		private Table table;
		private static Shell shell;
		private Button deleteButton;
		public List<String> added= new ArrayList<String>();
		public List<String> deleted = new ArrayList<String>();
		private List<String> categoryTmpDelete = new ArrayList<String>();
		
		public void initGui(JSONArray categoriesArray, String login,String url) {
			Display display = Display.getDefault();
			shell = new Shell(display);
			shell.setText("Kategorie kanału " + url);
			GridLayout gl=new GridLayout(2, true);
			gl.horizontalSpacing=50;
			shell.setLayout(gl);
			GridData gdlabelChannels=new GridData();
			gdlabelChannels.horizontalSpan=2;
			gdlabelChannels.horizontalAlignment=SWT.CENTER;
			gdlabelChannels.grabExcessHorizontalSpace=true;
			Label labelTitle=new Label(shell, SWT.NONE);
			labelTitle.setText("Zarządzanie kategoriami:");
			labelTitle.setLayoutData(gdlabelChannels);
			table=new Table(shell, SWT.BORDER+SWT.FULL_SELECTION);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			TableColumn tc=new TableColumn(table, SWT.NONE+SWT.BORDER);
			tc.setText("Id");
			tc.setWidth(50);
			TableColumn tc2=new TableColumn(table, SWT.BORDER);
			tc2.setText("kategoria");
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
			addButton.setText("Nowa kategoria");
			
			addButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					AddForm af= new AddForm();
					af.init("category",added);
					loadDataToTable(categoriesArray);
				}
			});
			
			deleteButton= new Button(composite, SWT.NONE);
			deleteButton.setText("Usuń kategorię");
			
			deleteButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					deleteButton.setEnabled(false);
					String string = "";
					
			        TableItem[] selection = table.getSelection();
			        //System.out.println(table.g);
			        deleted.add(table.getItem(table.getSelectionIndex()).getText(1));
			        loadDataToTable(categoriesArray);
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
						 String plainAnswer = target.path("rest").path("user").path("categoriesChanges").queryParam("url", url).queryParam("login", login).queryParam("added", addedString).queryParam("deleted", deletedString).request().accept(MediaType.TEXT_PLAIN).get(String.class);
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
								messageBox.setMessage("Błąd połączenia. Spróbuj ponownie potem.");
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
					
				}
			});
			/*Menu menu=new Menu(table);
			{
			MenuItem mi1=new MenuItem(menu, SWT.NONE);
			mi1.setText("DODAJ");
			mi1.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					AdresKlientaAddEditForm paef=new AdresKlientaAddEditForm(FormDataMode.ADD,null);
					paef.initGui();
					loadDataToTable();
				}
				
			});
			}

			MenuItem mi2=new MenuItem(menu, SWT.NONE);
			mi2.setText("EDYTUJ");
			mi2.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					AdresKlientaAddEditForm paef=new AdresKlientaAddEditForm(FormDataMode.EDIT,(AdresKlienta)table.getSelection()[0].getData(DataForm.DATA_OBJECT));
					paef.initGui();

					
					loadDataToTable();
				
				}
			});
			
			{
			MenuItem mi1=new MenuItem(menu, SWT.NONE);
			mi1.setText("USUN");
			mi1.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					MessageBox mb=new MessageBox(shell,SWT.YES+SWT.NO);
					mb.setMessage("Czy na pewno chcesz usun�� obiekt?");
					if(mb.open()==SWT.YES){
						new Controller().removeObject((AdresKlienta)table.getSelection()[0].getData(DataForm.DATA_OBJECT));
						loadDataToTable();
					}
				}
				
			});
			}

			table.setMenu(menu);
*/
			
			shell.open ();
			//shell.setMaximized(true);
			loadDataToTable(categoriesArray);
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) 
					{
						display.sleep ();
					}
			}

		}


		public void loadDataToTable(JSONArray data){
			table.clearAll();
			categoryTmpDelete.removeAll(categoryTmpDelete);

			int[] idx = { 0 };
			data.forEach(category ->{
				
				deleteButton.setEnabled(false);
			
				TableItem ti=new TableItem(table, SWT.NONE);

				table.getItem(idx[0]).setText(0,""+idx[0]);
				table.getItem(idx[0]).setText(1,category.toString());
				idx[0]++;
				  });
			
			added.forEach(category ->{
			
					TableItem ti=new TableItem(table, SWT.NONE);

					table.getItem(idx[0]).setText(0,""+idx[0]);
					table.getItem(idx[0]).setText(1,category.toString());
					idx[0]++;
				
				deleted.forEach(deletedCat->
				{
					if(category.toString().equals(deletedCat.toString()))
					{
						idx[0]--;
						categoryTmpDelete.add(deletedCat);
						table.remove(idx[0]);
					}
				});
				
			 });
			if(categoryTmpDelete.size()>0)
			{
				categoryTmpDelete.forEach(deletedCat->
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
	    CategoryManagementView(){
	    	instance = this;
	    }

	    public static CategoryManagementView getInstance(){
	    	
	    	return instance;
	        
	    }
			
}

		



