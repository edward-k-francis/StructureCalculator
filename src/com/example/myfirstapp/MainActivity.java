package com.example.myfirstapp;

import java.text.NumberFormat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity 
{

	private ListView[] columns = new ListView[5];
	private CheckBox crusherCheck;
	private ToggleButton unit;
	private EditText totalText;
	private Button deleteButton;
	private ToggleButton verifyButton;
	private Weight crusherWeight = new Weight(10);
	
	private NumberFormat format = NumberFormat.getInstance();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) 
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
        
        format.setMaximumFractionDigits(3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        if (id == R.id.action_settings) 
        {
        	clearAll();
            return true;
        }
        
        if(id == R.id.action_exit)
        {
        	System.exit(1);
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public View onCreateView(View parent, String name, Context context,	AttributeSet attrs) 
    {
    	View view = super.onCreateView(parent, name, context, attrs);
    	
    	columns[0] = (ListView) findViewById(R.id.listView1);
    	columns[1] = (ListView) findViewById(R.id.listView2);
    	columns[2] = (ListView) findViewById(R.id.listView3);
    	columns[3] = (ListView) findViewById(R.id.listView4);
    	columns[4] = (ListView) findViewById(R.id.listView5);
    	
    	crusherCheck = (CheckBox) findViewById(R.id.crusher_checkBox);
    	unit = (ToggleButton) findViewById(R.id.toggleButton1);
    	totalText = (EditText) findViewById(R.id.totalValue);
    	deleteButton = (Button) findViewById(R.id.deleteButton);
    	verifyButton = (ToggleButton) findViewById(R.id.verifyToggle);
    	
    	return view;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment 
    {

        public PlaceholderFragment() 
        { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    /**
     * Add 
     * @param view
     */
    public void add45(View view)
    {
    	if(!verifyMode())
    	{
    		Weight weight = new Weight(45);
    		addValue(weight);
    		updateColumns(weight);
    	}
    	else
    	{
    		verify(45);
    	}
    }
    
    public void add35(View view)
    {
    	if(!verifyMode())
    	{
    		Weight weight = new Weight(35);
    		addValue(weight);
    		updateColumns(weight);
    	}
    	else
    	{
    		verify(35);
    	}
    }
    
    public void add25(View view)
    {
    	if(!verifyMode())
    	{
    		Weight weight = new Weight(25);
    		addValue(weight);
    		updateColumns(weight);
    	}
    	else
    	{
    		verify(25);
    	}
    }
    
    public void add10(View view)
    {
    	if(!verifyMode())
    	{
    		Weight weight = new Weight(10);
    		addValue(weight);
    		updateColumns(weight);
    	}
    	else
    	{
    		verify(10);
    	}
    }
    
    public void add5(View view)
    {
    	if(!verifyMode())
    	{
    		Weight weight = new Weight(5);
    		addValue(weight);
    		updateColumns(weight);
    	}
    	else
    	{
    		verify(5);
    	}
    }
    
    public void addCrusher(View view)
    {
    	if(crusherCheck.isChecked())
    	{
    		addValue(crusherWeight);
    	}
    	else
    	{
    		deleteValue(crusherWeight);
    	}
    }
    
    public void convert(View view)	
    {
    	double total = 0.0;
    	
    	if(crusherCheck.isChecked())
    	{
    		total+=crusherWeight.addToTotal(total, unit.isChecked());
    	}
    	
		for(int c = 0; c < columns.length; c++)
		{
    		ArrayAdapter<Weight> adapter = (ArrayAdapter<Weight>) columns[c].getAdapter();
    		
    		if(adapter == null)
    		{
    			continue;
    		}
    		
    		for(int i = 0; i < adapter.getCount(); i++)
    		{
    			Weight weight = adapter.getItem(i);
    			total=weight.addToTotal(total, unit.isChecked());
    		}
		}
    		
        totalText.setText(Double.toString(total));
    }
    
    public void setVerify(View view)
    {
    	deleteButton.setEnabled(!verifyMode());
    }
    
    public void delete(View view)
    {
    	ListView list = getListViewForDelete();
    	
    	if(list == null)
    	{
    		if(crusherCheck.isChecked())
    		{
    			crusherCheck.setChecked(false);
    			deleteValue(crusherWeight);
    		}
    		return;
    	}
    	
    	ArrayAdapter<Weight> values = (ArrayAdapter<Weight>) list.getAdapter();
    	
    	Weight lastValue = values.getItem(values.getCount()-1);
    	
    	deleteValue(lastValue);
    	
    	ArrayAdapter<Weight> newValues = new ArrayAdapter<Weight>(this, android.R.layout.test_list_item);
    	
    	for(int i = 0; i < values.getCount()-1; i++)
    	{
    		newValues.add(values.getItem(i));
    	}
    	
    	list.setAdapter(newValues);
    }
    
    private boolean verifyMode()
    {
    	return verifyButton.isChecked();
    }
    
    private void addValue(Weight weight)
    {
    	double total = 0;
        total = getTotal();
        
        if(!crusherCheck.isChecked())
        {
        	crusherCheck.setChecked(true);
        	total = crusherWeight.addToTotal(total, unit.isChecked());
        }
        
        total = weight.addToTotal(total, unit.isChecked());
        totalText.setText(format.format(total));
    }


	private double getTotal() 
	{
		String message = totalText.getText().toString();
        
        if(message != null && !message.equals(""))
    	{
        	return Double.parseDouble(message);
    	}
        
		return 0.0;
	}
    
    private void deleteValue(Weight weight)
    {
        double total = getTotal();
        total = weight.reduceFromTotal(total, unit.isChecked());
        totalText.setText(format.format(total));
    }
    
    private void updateColumns(Weight value)
    {
    	ListView list = getListView();
    	
    	if(list == null)
    	{
    		return;
    	}
    	
    	ArrayAdapter<Weight> values = (ArrayAdapter<Weight>) list.getAdapter();
    	values.add(value);
    	list.setAdapter(values);
    }
    
    private void clearAll()
    {
    	for(int i = 0; i < columns.length; i++)
    	{
    		columns[i].setAdapter(new ArrayAdapter<Weight>(this, android.R.layout.test_list_item));
    	}
		
    	verifyButton.setChecked(false);
		crusherCheck.setChecked(false);
		totalText.setText("");
    }

    private ListView getListView() 
	{
		for(int i = 0; i < columns.length; i++)
		{
			if(columns[i].getAdapter() == null)
			{
				columns[i].setAdapter(new ArrayAdapter<Weight>(this, android.R.layout.test_list_item));
			}
			
			int value = columns[i].getAdapter().getCount();
			if(value < 10)
			{
				return columns[i];
			}
		}

		return null;
	}
    
    private ListView getListViewForDelete()
    {
    	for(int i = columns.length-1; i >= 0; i--)
    	{
    		if(columns[i].getAdapter() == null)
			{
				columns[i].setAdapter(new ArrayAdapter<Weight>(this, android.R.layout.test_list_item));
			}
    		
    		int value = columns[i].getAdapter().getCount();
    		int value2 = i != columns.length-1 ? columns[i+1].getAdapter().getCount() : 0;
    		if(value > 0 && value <= 10 && value2 == 0)
    		{
    			return columns[i];
    		}
    		
    	}

		return null;
    }
    
    private void verify(int value)
    {
    	ListView verifyList = getVerifyListView();
    	
    	if(verifyList == null)
    	{
    		return;
    	}
    	
    	int position = getVerifyPosition(verifyList);
    	
    	Weight weight = (Weight) verifyList.getAdapter().getItem(position);
    	weight.verify(value);
    	
    	View view = verifyList.getChildAt(position);
    	
    	if(view == null)
    	{
    		return;
    	}
    	
    	if(weight.isCorrect())
    	{
    		view.setBackgroundColor(Color.GREEN);
    	}
    	else
    	{
    		view.setBackgroundColor(Color.RED);
    	}
    }
    
	private ListView getVerifyListView() 
	{
		for(int i = 0; i < columns.length; i++)
		{
			if(getVerifyPosition(columns[i]) > -1)
			{
				return columns[i];
			}
		}
		
		return null;
	}
	
	private int getVerifyPosition(ListView view)
	{
		Weight weight = null;
		ArrayAdapter<Weight> values = (ArrayAdapter<Weight>) view.getAdapter();
		
		for(int i = values.getCount()-1; i >= 0; i--)
		{
			Weight setWeight = values.getItem(i);
			
			if(!setWeight.isVerified())
			{
				return i;
			}
		}
		
		return -1;
	}
    
    private class Weight
    {
    	private double value;
    	private boolean verified;
    	private boolean correct;
    	
    	public Weight(double value)
    	{
    		this.value = value;
    		this.verified = false;
    	}
    	
    	public void verify(double value)
    	{
    		this.correct = this.value == value;
    		this.verified = true;
    	}
    	
    	protected boolean isCorrect()
    	{
    		return this.correct;
    	}
    	
    	protected boolean isVerified()
    	{
    		return this.verified;
    	}
    	
    	protected double addToTotal(double total, boolean inKiloGrams)
    	{
    		if(inKiloGrams)
    		{
    			return total+=valueInKgs();
    		}
    		
    		return total+=this.value;
    	}
    	
    	protected double reduceFromTotal(double total, boolean inKiloGrams)
    	{
    		if(inKiloGrams)
    		{
    			return total-=valueInKgs();
    		}
    		
    		return total-=this.value;
    	}
    	
    	public String toString()
    	{
    		return Double.toString(value);
    	}
    	
    	private double valueInKgs()
    	{
    		return value*0.453;
    	}
    }
}
