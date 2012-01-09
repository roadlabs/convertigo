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

package com.twinsoft.convertigo.engine.admin.services.configuration;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.twinsoft.convertigo.engine.Engine;
import com.twinsoft.convertigo.engine.EnginePropertiesManager;
import com.twinsoft.convertigo.engine.EnginePropertiesManager.PropertyName;
import com.twinsoft.convertigo.engine.admin.services.XmlService;
import com.twinsoft.convertigo.engine.admin.services.at.ServiceDefinition;
import com.twinsoft.convertigo.engine.admin.services.at.ServiceDefinition.Role;
import com.twinsoft.convertigo.engine.util.XMLUtils;

@ServiceDefinition(
		name = "Update",
		roles = { Role.WEB_ADMIN },
		parameters = {},
		returnValue = ""
	)
public class Update extends XmlService {

	protected void getServiceResult(HttpServletRequest request, Document document) throws Exception {
		Element rootElement = document.getDocumentElement();

		Document post = null;

		post = XMLUtils.parseDOM(request.getInputStream());

		NodeList nl = post.getElementsByTagName("property");

		for (int i = 0; i < nl.getLength(); i++) {
			String propKey = ((Element) nl.item(i)).getAttribute("key");
			PropertyName property = PropertyName.valueOf(propKey);
			if (property.isVisible()) {
				String propValue = ((Element) nl.item(i)).getAttribute("value");
				EnginePropertiesManager.setProperty(property, propValue);
				Engine.logAdmin.info("The engine property '" + propKey + "' has been updated to '" + propValue + "'");
			}
		}

		EnginePropertiesManager.saveProperties();

		Element update = document.createElement("update");
		update.setAttribute("status", "ok");
		rootElement.appendChild(update);
	}
}