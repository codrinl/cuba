/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.09.2009 20:34:38
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.Application;

import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CubaApplicationContext extends WebApplicationContext {
    protected CubaApplicationContext() {
    }

    static public WebApplicationContext getApplicationContext(HttpSession session) {
        CubaApplicationContext cx = (CubaApplicationContext) session
                .getAttribute(WebApplicationContext.class.getName());
        if (cx == null) {
            cx = new CubaApplicationContext();
            session.setAttribute(WebApplicationContext.class.getName(), cx);
        }
        if (cx.session == null) {
            cx.session = session;
        }
        return cx;
    }

    @Override
    protected CommunicationManager createCommunicationManager(Application application,
            AbstractApplicationServlet applicationServlet) {
        return new CubaCommunicationManager(application);
    }
}
