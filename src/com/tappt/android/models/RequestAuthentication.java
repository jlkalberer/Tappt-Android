/**
 * 
 */
package com.tappt.android.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * The request authentication model for getting access to various resources.
 */
public class RequestAuthentication {
	
	public RequestAuthentication() {
		this.Kegerators = new ArrayList<Integer>();
	}
	
	/**
    * Gets or sets the user name for the user who wants authorization.
    */
    public String UserName;

    /**
    * Gets or sets the password.
    */
    public String Password;

    /**
    * Gets or sets the authorize type.
    */
    public AuthorizeType AuthorizeType;

    /**
    * Gets or sets the expires date.
    */
    public Date ExpiresDate;

    /**
    * Gets or sets the kegerator IDs.
    */
    public ArrayList<Integer> Kegerators;
}
