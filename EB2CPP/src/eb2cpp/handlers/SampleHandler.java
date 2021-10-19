package eb2cpp.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rodinp.core.RodinDBException;
import org.eclipse.jface.dialogs.MessageDialog;

public class SampleHandler extends AbstractHandler {

	public MainHandler EB2CPP;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			EB2CPP = new MainHandler();
		}
		catch (RodinDBException e)
		{
			e.printStackTrace();
		}
		
		if (!EB2CPP.projectExists()) {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			MessageDialog.openInformation(
					window.getShell(),
					"EB2CPP",
					"There isn't a project with the name specified");
		}
		else {
			try {
				EB2CPP.translateProject();
			}
			catch (RodinDBException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}
