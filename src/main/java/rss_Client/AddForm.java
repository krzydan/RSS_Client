package rss_Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;

public class AddForm extends MainWindow
{
	public void init(String mode,List<String> array)
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		GridLayout gl=new GridLayout(2, true);
		gl.horizontalSpacing=50;
		shell.setLayout(gl);
		Text newRecord =  new Text(shell, SWT.BORDER);
		Label label = new Label(shell, SWT.NONE);
		switch(mode)
		{
			case "channel":
			{
				label.setText("Wpisz adres URL dla feeda RSS"); break;
				
			}
			case "category":
			{
				label.setText("Podaj kategorię dla tego kanału"); break;
			}
		}
		
		Button confirmButton = new Button(shell, SWT.NONE);
		confirmButton.setText("Zapisz");
		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(mode.equals("channel"))
				{
					if(newRecord.getText().matches("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"))
					{
						if(checkIfCorrectFeed(newRecord.getText()))
						{
							array.add(newRecord.getText());
							shell.close();
						}
						else
						{
							MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
							messageBox.setMessage("Podany adres nie jest adresem feeda RSS. Proszę spróbować ponownie.");
							messageBox.open();
						}
						
					}
					else
					{
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Błędny adres. Proszę spróbować ponownie.");
						messageBox.open();
					}
				}
				else if(!newRecord.getText().equals(""))
				{
					array.add(newRecord.getText());
					shell.close();
				}
				else
				{
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
					messageBox.setMessage("Proszę podać nazwę kategorii.");
					messageBox.open();
				}
				
			}
		});
		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setText("Anuluj");
		
		cancelButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		shell.setSize(550, 150);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) 
				{
					display.sleep ();
				}
		}
	}
	
	private boolean checkIfCorrectFeed(String urlString) // funkcja sprawdza czy podano prawidłowy URL kanału
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
			ArrayList<String >titles = new ArrayList<String>();
			String[] newsArray = sourceCode.split("<item>");
			
		
			in.close();
			
			if(newsArray[0].contains("<channel>"))
			{
				return true;
			}
				
		
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong reading the contents");
			e.printStackTrace();
		} 
		
		return false;
	}
	
}