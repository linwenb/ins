package hk.ust.ins;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void callManager(View view) {
    	if(view.getTag()!= null)
    	{
    		int id = Integer.parseInt(view.getTag().toString());
    	
	    	Intent intent = new Intent(this, ManagerActivity.class);
	    	Bundle bundle = new Bundle();
	    	bundle.putInt("id", id);
	    	intent.putExtras(bundle);
	    	startActivity(intent);
	    	System.gc();
    	}
    }
    
}
