package iglabs.warehouse.converter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App 
{
    public static void main( String[] args )
    {
    	try {
	    	UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName()
	        );
    	}
    	catch (Exception ex) {
    	}
    	
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			DefaultAppContext appContext = DefaultAppContext.getInstance();
    			appContext.bootstrap();
    			
    			MainWindow mainWindow = new MainWindow(appContext);
    			mainWindow.setVisible(true);
    		}
    	});
    }
}
