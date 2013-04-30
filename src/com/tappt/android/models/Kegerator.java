package com.tappt.android.models;

/**
 * @author John
 * A kegerator model.
 */
public class Kegerator {
    /**
     * Gets or sets the id. 
     */
    public int ID;
    
    /**
    * Gets or sets the communication hash.
    */
    public String CommunicationKey;

    /**
    * Gets or sets the keg name.
    */
    public String KegeratorName;

    /**
    * Gets or sets a value indicating whether kegerator public.
    */
    public Boolean KegeratorPublic;

    /**
    * Gets the current keg.
    */
    public Keg CurrentKeg(){ 
    	return null;//this.Kegs[0];
    }

    /**
    * Gets or sets the temperature.
    */
    public float Temperature;

    /**
    * Gets or sets the amount of beer that was poured.
    */
    public float OuncesLeftInKeg;

    /**
    * Gets or sets the kegs.
    */
    public Iterable<Keg> Kegs;
	
	@Override
	public String toString() {
		return "";
	}
}
