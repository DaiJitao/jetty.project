//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.annotations;

import java.util.List;

import org.eclipse.jetty.annotations.AnnotationParser.ClassInfo;
import org.eclipse.jetty.annotations.AnnotationParser.FieldInfo;
import org.eclipse.jetty.annotations.AnnotationParser.MethodInfo;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.DiscoveredAnnotation;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebListenerAnnotationHandler extends AbstractDiscoverableAnnotationHandler
{
    private static final Logger LOG = Log.getLogger(WebListenerAnnotationHandler.class);

    public WebListenerAnnotationHandler (WebAppContext context)
    {
       super(context);
    }
    
    public WebListenerAnnotationHandler (WebAppContext context, List<DiscoveredAnnotation> list)
    {
       super(context, list);
    }
    
    /** 
     * @see org.eclipse.jetty.annotations.AnnotationParser.DiscoverableAnnotationHandler#handle(ClassAnnotationInfo)
     */
    public void handle(ClassInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;
        
        WebListenerAnnotation wlAnnotation = new WebListenerAnnotation(_context, info.getClassName(), info.getContainingResource());
        addAnnotation(wlAnnotation);
    }

    public void handle(FieldInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;
        LOG.warn ("@WebListener is not applicable to fields: "+info.getClassInfo().getClassName()+"."+info.getFieldName());
    }

    public void handle(MethodInfo info, String annotationName)
    {
        if (annotationName == null || !"javax.servlet.annotation.WebListener".equals(annotationName))
            return;
        LOG.warn ("@WebListener is not applicable to methods: "+info.getClassInfo().getClassName()+"."+info.getMethodName()+" "+info.getSignature());
    }
}
