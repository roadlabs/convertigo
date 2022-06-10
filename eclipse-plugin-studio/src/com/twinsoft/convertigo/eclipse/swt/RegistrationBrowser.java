package com.twinsoft.convertigo.eclipse.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.browser.callback.OpenPopupCallback;
import com.teamdev.jxbrowser.browser.event.BrowserClosed;
import com.teamdev.jxbrowser.js.JsAccessible;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.view.swt.BrowserView;
import com.twinsoft.convertigo.engine.Engine;

public class RegistrationBrowser extends Composite {
	public interface OnPSC {
		void onPSC(String psc);
	}
	
	public interface OnReady {
		void onReady(boolean ok);
	}
	
	C8oBrowser browser;
	String username;
	String secret;
	OnPSC onPSC;
	OnReady onReady;

	public class StudioAPI {
		
		@JsAccessible
		public String getUsername() {
			return username;
		}
		
		@JsAccessible
		public String getSecret() {
			return secret;
		}
		
		@JsAccessible
		public void setPSC(String psc) {
			try {
				onPSC.onPSC(psc);
			} catch (Throwable t) {
			}
		}
		
		@JsAccessible
		public void setReady(boolean ok) {
			try {
				onReady.onReady(ok);
			} catch (Throwable t) {
			}
		}
	}
	
	public RegistrationBrowser(Composite parent, int style) {
		super(parent, style);
		Composite composite = this;
		StackLayout stack = new StackLayout();
		composite.setLayout(stack);
		
		stack.topControl = browser = new C8oBrowser(this, SWT.NONE);
		
		browser.setUseExternalBrowser(true);
		browser.getBrowser().set(InjectJsCallback.class, params -> {
			String url = params.frame().browser().url();
			if (url != null) {
				try {
					JsObject window = params.frame().executeJavaScript("window"); 
					window.putProperty("studio", new StudioAPI());
				} catch (Exception e) {
					Engine.logStudio.info("onScriptContextCreate failed for '" + url + "': " + e.getMessage());
				}
			}
			return com.teamdev.jxbrowser.browser.callback.InjectJsCallback.Response.proceed();
		});
		browser.getBrowser().set(OpenPopupCallback.class , params -> {
			getDisplay().asyncExec(() -> {
				Browser br = params.popupBrowser();
				BrowserView bv = BrowserView.newInstance(composite, br);
				stack.topControl = bv;
				composite.layout(true);
				br.on(BrowserClosed.class, event ->
					getDisplay().asyncExec(() -> {
						stack.topControl = browser;
						composite.layout(true);
					})
				);
			});
			return com.teamdev.jxbrowser.browser.callback.OpenPopupCallback.Response.proceed();
		});
	}

	public RegistrationBrowser setUsername(String username) {
		this.username = username;
		return this;
	}

	public RegistrationBrowser setSecret(String secret) {
		this.secret = secret;
		return this;
	}
	
	public RegistrationBrowser onPSC(OnPSC onPSC) {
		this.onPSC = onPSC;
		return this;
	}
	
	public RegistrationBrowser onReady(OnReady onReady) {
		this.onReady = onReady;
		return this;
	}
	
	public void goRegister() {
		browser.setUrl("https://test-convertigo.convertigo.net/convertigo/projects/convertigo_signup/DisplayObjects/mobile/login/true/" + SwtUtils.isDark());
	}
	
	public void goTrial() {
//		browser.setUrl("http://localhost:41562/path-to-xfirst");
//		browser.setUrl("https://www.convertigo.com/startprivatecloud");
		browser.setUrl("https://test-convertigo.convertigo.net/convertigo/projects/convertigo_signup/DisplayObjects/mobile/startcloudfromstudio/:signup/:dark");
	}
}
