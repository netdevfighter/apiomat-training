/*
 * Copyright (c) 2011 - 2018, Apinauten GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.apiomat.nativemodule.salesmodule2;
import com.apiomat.nativemodule.*;
import com.apiomat.nativemodule.basics.User;

import com.apiomat.nativemodule.mysqlaomdbusershopapp2.Employees;
import com.apiomat.nativemodule.salesmodule2.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.apiomat.nativemodule.salesmodule2.SalesModule2.APP_CONFIG_PROXY;

/**
* Generated class for hooks on your Lead data model
*/

public class LeadHooksNonTransient<T extends com.apiomat.nativemodule.salesmodule2.Lead> implements com.apiomat.nativemodule.IModelHooksNonTransient<com.apiomat.nativemodule.salesmodule2.Lead>
{
    protected com.apiomat.nativemodule.salesmodule2.Lead model;

    @Override
    public void setCallingModel( com.apiomat.nativemodule.salesmodule2.Lead model )
    {
        this.model = model;
    }


    /*
     * Following Methods can be used if your data model is NOT set to TRANSIENT
     */

    @Override
    public void beforePost( com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {
        Object defaultScore = APP_CONFIG_PROXY.getConfigValue(SalesModule2.DEFAULT_SCORE, r.getApplicationName(), r.getSystem());
        Long castedScore = Long.valueOf( String.valueOf( defaultScore ) );
        this.model.log(Level.INFO, "Creating lead....");
        obj.setLastVisit(new Date());
        obj.setScore(castedScore);

    }


    @Override
    public void afterPost( com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {
        List<Salesman> salesmans = this.model.findByNames(Salesman.class, "userName == \"" + r.getUserEmail() + "\"", r);
        if (salesmans.size() == 1) {
            Salesman salesman = salesmans.get(0);
            salesman.postListOfLeads(obj);
        }


        ContractProtocol cp = this.model.createObject(ContractProtocol.class, r);

        List<Employees> employees = this.model.findByNames(Employees.class, "", r);
        if (employees != null && employees.size() > 0)
            cp.setNotes(employees.get(0).getPhone());

        cp.save();
        obj.postContactAttempts(cp);
    }

    @Override
    public void beforeGet( String id, com.apiomat.nativemodule.Request r )
    {
    }

    
    @Override
    public void afterGet( com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {
    }

    @Override
    public String beforeGetAll( String query, com.apiomat.nativemodule.Request r )
    {
        /* NOTE that returning null or "" would ignore any query and always return any object of this class and backend */
        return query;
    }

    @Override
    public java.util.List<com.apiomat.nativemodule.salesmodule2.Lead> afterGetAll( java.util.List<com.apiomat.nativemodule.salesmodule2.Lead> objects, String query, com.apiomat.nativemodule.Request r )
    {
        /*
         * If you want to change the returned list of elements, you have to create a new list
         * and add the elements to return to it. Because getting elements from the "objects"
         * list will return a copy, changing values in this list does not have any effect.
         *
         * If NULL is returned, unnecessary conversions are omitted and result is taken from database.
         */
        return null;
    }
    @Override
    public boolean beforePut( com.apiomat.nativemodule.salesmodule2.Lead objFromDB, com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {
        if (!r.getIsAccountRequest() && obj.getScore() != null && objFromDB.getScore() != obj.getScore()) {
            this.model.throwException("score modification ia not allowed");
        }

          String defaultApiKey =  String.valueOf(APP_CONFIG_PROXY.getConfigValue(SalesModule2.DEFAULT_GMAP_API_KEY, r.getApplicationName(), r.getSystem()));

        try {
            final URL url = new URL("https://maps.googleapis.com/maps/api/staticmap?canter=51.34,12.37&zoom=14&size=400x400&key=" + defaultApiKey);
            obj.postAreaPicture(url.openStream(), "gmaps", "png");
        } catch (MalformedURLException e) {
            this.model.throwException(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            this.model.throwException(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void afterPut( com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {

    }

    @Override
    public boolean beforeDelete( com.apiomat.nativemodule.salesmodule2.Lead obj, com.apiomat.nativemodule.Request r )
    {
        return false;
    }


    @Override
    public boolean beforePostData( final com.apiomat.nativemodule.salesmodule2.Lead obj, final String attributeName, final com.apiomat.nativemodule.DataWrapper dataWrapper, final com.apiomat.nativemodule.Request r )
    {
        return false;
    }

    @Override
    public void afterPostData( final com.apiomat.nativemodule.salesmodule2.Lead obj, final String attributeName, final com.apiomat.nativemodule.DataWrapper dataWrapper, final com.apiomat.nativemodule.Request r )
    {
    }

    @Override
    public String beforeGetData( final String dataId, final String attributeName, final com.apiomat.nativemodule.TranscodingConfiguration transcodingConfig, final com.apiomat.nativemodule.Request r )
    {
        return null;
    }

    @Override
    public void afterGetData( final String dataId, final String attributeName, final com.apiomat.nativemodule.DataWrapper dataWrapper, final com.apiomat.nativemodule.TranscodingConfiguration transcodingConfig, final com.apiomat.nativemodule.Request r )
    {
    }

    @Override
    public boolean beforeDeleteData( final com.apiomat.nativemodule.salesmodule2.Lead obj, final String attributeName, final com.apiomat.nativemodule.DataWrapper dataWrapper, final com.apiomat.nativemodule.Request r )
    {
        return false;
    }

    @Override
    public void afterDeleteData( final com.apiomat.nativemodule.salesmodule2.Lead obj, final String attributeName, final com.apiomat.nativemodule.DataWrapper dataWrapper, final com.apiomat.nativemodule.Request r )
    {
    }

    @Override
    public boolean beforePostRef( com.apiomat.nativemodule.salesmodule2.Lead obj, Object referencedObject, String referenceName, com.apiomat.nativemodule.Request r )
    {
        return false;
    }

    @Override
    public void afterPostRef( com.apiomat.nativemodule.salesmodule2.Lead obj, Object referencedObject, String referenceName, com.apiomat.nativemodule.Request r )
    {
    }

    @Override
    public String beforeGetAllReferences( String query, String referenceName, com.apiomat.nativemodule.Request r )
    {
        /* NOTE that returning null or "" would ignore any query and always return any referenced object */
        return query;
    }

    @Override
    public <Z extends com.apiomat.nativemodule.AbstractClientDataModel> java.util.List<Z> afterGetAllReferences( java.util.List<Z> objects, String query,
        String referenceName, com.apiomat.nativemodule.Request r )
    {
            return null; // return objects here if you changed sth; returning null prevents unnecessary conversions
    }

    @Override
    public boolean beforeDeleteRef( com.apiomat.nativemodule.salesmodule2.Lead obj, Object referencedObject, String referenceName, com.apiomat.nativemodule.Request r )
    {
        return false;
    }

    @Override
    public void afterDeleteRef( com.apiomat.nativemodule.salesmodule2.Lead obj, Object referencedObject, String referenceName, com.apiomat.nativemodule.Request r )
    {
    }

}
