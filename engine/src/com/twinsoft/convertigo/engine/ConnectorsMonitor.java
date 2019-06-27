/*
 * Copyright (c) 2001-2019 Convertigo SA.
 * 
 * This program  is free software; you  can redistribute it and/or
 * Modify  it  under the  terms of the  GNU  Affero General Public
 * License  as published by  the Free Software Foundation;  either
 * version  3  of  the  License,  or  (at your option)  any  later
 * version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;  without even the implied warranty of
 * MERCHANTABILITY  or  FITNESS  FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */

package com.twinsoft.convertigo.engine;

import java.awt.*;

import com.twinsoft.api.ISessionManagerMonitor;

public class ConnectorsMonitor extends ISessionManagerMonitor {
	
	private static final long serialVersionUID = 9018017104263266158L;

	private GridLayout layout;
	    
    /** Creates new form ConnectorsMonitoring */
    public ConnectorsMonitor() {
        initComponents();
		setSize(640, 480);
		setVisible(true);
		layout = (GridLayout) panelConnectorVisualObjects.getLayout();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        panelConnectorVisualObjects = new java.awt.Panel();

        setBackground(new java.awt.Color(255, 255, 255));
        setTitle("Twinsoft Convertigo connectors monitor");
        panelConnectorVisualObjects.setLayout(new java.awt.GridLayout(1, 1, 2, 2));

        panelConnectorVisualObjects.setBackground(new java.awt.Color(255, 255, 204));
        add(panelConnectorVisualObjects, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Panel panelConnectorVisualObjects;
    // End of variables declaration//GEN-END:variables

	private int nConnectorVisualObjects = 0;

	public void addComponent(Object sessionID, Component connectorVisualObject) {
		nConnectorVisualObjects++;

		layout.setRows(nConnectorVisualObjects / 3 + (nConnectorVisualObjects % 3 == 0 ? 0 : 1));
		layout.setColumns(nConnectorVisualObjects % 3 + 1);
		
        Panel panelConnectorVisualObject = new Panel();
		panelConnectorVisualObject.setLayout(new BorderLayout());
        
		Label labelSessionID = new Label("Session ID: " + sessionID);
		labelSessionID.setAlignment(java.awt.Label.CENTER);
		labelSessionID.setBackground(new java.awt.Color(0, 153, 255));
		labelSessionID.setFont(new java.awt.Font("Tahoma", 1, 12));
		labelSessionID.setForeground(new java.awt.Color(255, 255, 255));
		panelConnectorVisualObject.add(labelSessionID, BorderLayout.NORTH);

		panelConnectorVisualObject.add(connectorVisualObject, BorderLayout.CENTER);
        
		panelConnectorVisualObjects.add(panelConnectorVisualObject);
		panelConnectorVisualObjects.validate();
		repaint();
	}

	public void removeComponent(Component connectorVisualObject) {
		nConnectorVisualObjects--;

		layout.setRows(nConnectorVisualObjects / 3 + (nConnectorVisualObjects % 3 == 0 ? 0 : 1));
		layout.setColumns(nConnectorVisualObjects % 3 + 1);
		
		panelConnectorVisualObjects.remove(connectorVisualObject.getParent());
		panelConnectorVisualObjects.validate();
		repaint();
	}
}
