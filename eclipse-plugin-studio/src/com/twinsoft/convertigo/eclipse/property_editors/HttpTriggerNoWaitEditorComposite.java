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

package com.twinsoft.convertigo.eclipse.property_editors;

import com.twinsoft.convertigo.engine.parsers.triggers.NoWaitTrigger;
import com.twinsoft.convertigo.engine.parsers.triggers.TriggerXMLizer;

public class HttpTriggerNoWaitEditorComposite extends HttpTriggerWaitTimeEditorComposite { 
	
	public HttpTriggerNoWaitEditorComposite(HttpTriggerEditorComposite parent) {
		super(parent);
	}

	@Override
	public TriggerXMLizer getTriggerXMLizer(){
		return new TriggerXMLizer(new NoWaitTrigger(parent.getTimeout(), isDoDirty()));
	}

	@Override
	public String getHelp() {
		return "This synchronizer does nothing.";
	}
}
