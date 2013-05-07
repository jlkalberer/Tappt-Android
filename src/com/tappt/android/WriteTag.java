package com.tappt.android;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class WriteTag extends Activity implements CreateNdefMessageCallback {

	NfcAdapter adapter;
	PendingIntent pendingIntent;
	IntentFilter writeTagFilters[];
	boolean writeMode;
	Tag mytag;
	Context ctx;
	String[][] techList;
	String token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_tag);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		token = intent.getStringExtra("AuthenticationID");
		
		adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		writeTagFilters = new IntentFilter[] { tagDetected };
		
		techList = new String[][] { new String[] { NfcA.class.getName(),
	            NfcB.class.getName(), NfcF.class.getName(),
	            NfcV.class.getName(), IsoDep.class.getName(),
	            MifareClassic.class.getName(),
	            MifareUltralight.class.getName(), Ndef.class.getName() } };
		
		//adapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {tagDetected}, null);
	}

	@Override
	protected void onResume(){
		super.onResume();
		adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
	}
	@Override
	protected void onPause() {
	    super.onPause();
	    adapter.disableForegroundDispatch(this);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onNewIntent(Intent intent){
	      if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
	          Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	          writeTag(WriteTag.this, detectedTag, "FOOBAR");
	      }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_tag, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean writeTag(Context context, Tag tag, String data) {     
	    // Record to launch Play Store if app is not installed
	    NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());
	 
	    // Record with actual data we care about
	    NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
	                                            new String("application/" + context.getPackageName()).getBytes(Charset.forName("US-ASCII")),
	                                            null, data.getBytes());
	 
	    // Complete NDEF message with both records
	    NdefMessage message = new NdefMessage(new NdefRecord[] {relayRecord, appRecord});
	 
	    try {
	        // If the tag is already formatted, just write the message to it
	        Ndef ndef = Ndef.get(tag);
	        if(ndef != null) {
	            ndef.connect();
	 
	            // Make sure the tag is writable
	            if(!ndef.isWritable()) {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcReadOnlyErrorTitle, R.string.nfcReadOnlyError);
	                return false;
	            }
	 
	            // Check if there's enough space on the tag for the message
	            int size = message.toByteArray().length;
	            if(ndef.getMaxSize() < size) {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcBadSpaceErrorTitle, R.string.nfcBadSpaceError);
	                return false;
	            }
	 
	            try {
	                // Write the data to the tag
	                ndef.writeNdefMessage(message);
	 
	                //DialogUtils.displayInfoDialog(context, R.string.nfcWrittenTitle, R.string.nfcWritten);
	                return true;
	            } catch (TagLostException tle) {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcTagLostErrorTitle, R.string.nfcTagLostError);
	                return false;
	            } catch (IOException ioe) {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
	                return false;
	            } catch (FormatException fe) {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
	                return false;
	            }
	        // If the tag is not formatted, format it with the message
	        } else {
	            NdefFormatable format = NdefFormatable.get(tag);
	            if(format != null) {
	                try {
	                    format.connect();
	                    format.format(message);
	 
	                    //DialogUtils.displayInfoDialog(context, R.string.nfcWrittenTitle, R.string.nfcWritten);
	                    return true;
	                } catch (TagLostException tle) {
	                    //DialogUtils.displayErrorDialog(context, R.string.nfcTagLostErrorTitle, R.string.nfcTagLostError);
	                    return false;
	                } catch (IOException ioe) {
	                    //DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
	                    return false;
	                } catch (FormatException fe) {
	                    //DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
	                    return false;
	                }
	            } else {
	                //DialogUtils.displayErrorDialog(context, R.string.nfcNoNdefErrorTitle, R.string.nfcNoNdefError);
	                return false;
	            }
	        }
	    } catch(Exception e) {
	        //DialogUtils.displayErrorDialog(context, R.string.nfcUnknownErrorTitle, R.string.nfcUnknownError);
	    }
	 
	    return false;
	}
}
