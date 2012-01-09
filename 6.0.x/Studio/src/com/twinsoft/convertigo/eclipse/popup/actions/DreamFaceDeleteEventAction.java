/*
 * Copyright (c) 2001-2011 Convertigo SA.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 *
 * $URL$
 * $Author$
 * $Revision$
 * $Date$
 */

package com.twinsoft.convertigo.eclipse.popup.actions;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import com.twinsoft.convertigo.beans.core.Connector;
import com.twinsoft.convertigo.beans.core.RequestableObject;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.MashupDataViewConfiguration;
import com.twinsoft.convertigo.eclipse.MashupEventConfiguration;
import com.twinsoft.convertigo.eclipse.MashupInformation;
import com.twinsoft.convertigo.eclipse.dialogs.MashupEventDialog;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;

public class DreamFaceDeleteEventAction extends DreamFaceAbstractAction {

	public DreamFaceDeleteEventAction() {
		super();
	}

	public void run() {
		Display display = Display.getDefault();
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);		
		
		Shell shell = getParentShell();
		shell.setCursor(waitCursor);
		
        try {
    		ProjectExplorerView explorerView = getProjectExplorerView();
    		if (explorerView != null) {
    			RequestableObject requestable = (RequestableObject)explorerView.getFirstSelectedDatabaseObject();
    			
    			Connector connector = requestable.getConnector();
    			
    			String connectorName = (connector != null) ? connector.getName():"";
    			String projectName = requestable.getProject().getName();
    			String requestableName = requestable.getName();
    			
    			String key = projectName + "." + connectorName + "." + requestableName;
    			MashupInformation mashupInformation = getMashupInformation(projectName);
    			MashupDataViewConfiguration mdc = mashupInformation.getDataViewConfiguration(key);
    			if (mdc == null) {
    				mdc = new MashupDataViewConfiguration();
    			}
    			MashupEventConfiguration mec = mashupInformation.getEventConfiguration(key);
    			if (mec == null) {
    				mec = new MashupEventConfiguration();
    			}
    			
    			MashupEventDialog mashupDialog = new MashupEventDialog(shell, MashupEventDialog.TYPE_DELETE, mdc, mec);
	        	mashupDialog.open();
	    		if (mashupDialog.getReturnCode() != Window.CANCEL) {
	    			
	    			String dataview = mashupDialog.dataview;
	    			if ((dataview != null) && (!dataview.equals(""))) {
	    				if (securedLogin()) {
		    				try {
			    				// Add dataviews to Mashup Composer
				    			String dfResponse = deleteResponseEvent(requestable, dataview);
				    			if (!dfResponse.equals("")) {
				    	        	MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.APPLICATION_MODAL);
				    	        	messageBox.setMessage("For requestable response: " + dfResponse);
				    	        	messageBox.open();
				    			}
				    			
				    			// Store dataview names
				    			if (existClass(dataview))
				    				mec.removeDataView(dataview);
			    		        mashupInformation.setEventConfiguration(key, mec);
			    		        storeMashupInformation(mashupInformation, projectName);
		    				}
		    				catch (DreamFaceInvalidTemplateException ite) {
			    				mec.removeDataView(dataview);
			    		        mashupInformation.setEventConfiguration(key, mec);
			    		        storeMashupInformation(mashupInformation, projectName);

			    	        	deleteResponseTemplate(dataview);
			    	        	throw ite;
		    				}
		    				finally {
		    					securedLogout();
		    				}
	    				}
	    			}
	    		}
    		}
        }
        catch (Throwable e) {
        	ConvertigoPlugin.logException(e, "Unable to update event(s) to DreamFace!");
        }
        finally {
			shell.setCursor(null);
			waitCursor.dispose();
        }
	}
	
	private String deleteResponseEvent(RequestableObject requestable, String dataview) throws HttpException, IOException, ParserConfigurationException, SAXException, DreamFaceInvalidTemplateException {
		Hashtable<String, String> fields = getResponseFields(DELETE_EVENT_TYPE, requestable, dataview);
        return updateDataView(dataview,fields);
	}
	
	private String updateDataView(String dataview, Hashtable<String, String> fields) throws HttpException, IOException, DreamFaceInvalidTemplateException {
        if (existClass(dataview))
        	return updateClass(fields);
        else {
        	throw new DreamFaceInvalidTemplateException("Dataview named \""+ dataview +"\" does not exist any more in DreamFace repository.");
        }
	}
}
