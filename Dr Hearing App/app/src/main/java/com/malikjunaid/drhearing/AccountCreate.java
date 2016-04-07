package com.malikjunaid.drhearing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountCreate extends Activity {

	private SharedPreferences user_info;
	Context ctx = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.create_account);
		user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		final TextView tv_account = (TextView) findViewById(R.id.tv_account);

		// if(user_info.contains("account")){//already created an account. So
		// maybe we need to check the db

		// }else{//get new account from AccountManager. Ask them age and gender.
		// Push to db.
		Button b = (Button) findViewById(R.id.btn_create_account);
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {// should collect the data they
											// inputed and probably send it to
											// the db and then start the next
											// activity
				String account = tv_account.getText().toString();
			//	String userName = (TextView).getText().toString();
				Spinner sp_gender = (Spinner) findViewById(R.id.spinner_gender);
				String gender = sp_gender.getSelectedItem().toString();
				Spinner sp_age = (Spinner) findViewById(R.id.spinner_age);
				int age = Integer.parseInt(sp_age.getSelectedItem().toString());
				setAccountPref(account, gender, age);

				Intent intent = new Intent(ctx, StartTest.class);
				ctx.startActivity(intent);
			}
		});

		List<String> ages = new ArrayList<String>();
		for (int count = 0; count < 100; count++)
			ages.add(count + "");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner_age = (Spinner) findViewById(R.id.spinner_age);
		spinner_age.setAdapter(adapter);

		AccountManager am = AccountManager.get(this); // "this" references the
		// current Context
		final Account[] accounts = am.getAccountsByType("com.google");
		int len = accounts.length;
		if (len > 1) {// user needs to choose an account
			tv_account.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					showAccountAlert(accounts);
				}
			});
			// showAccountAlert(accounts);
		} else {
			// tv_account = (TextView) findViewById(R.id.tv_account);
			// tv_account.setText(accounts[0].name);
			// setAccountPref(accounts[0].name);
		}
		tv_account.setText(accounts[0].name);
		for (int i = 0; i < len; i++)
			Log.d("ACCOUNTS", accounts[i].toString());
		// }
	}

	protected void showAccountAlert(Account[] accounts) {
		int len = accounts.length;
		final CharSequence[] accs = new CharSequence[len];
		for (int i = 0; i < len; i++)
			accs[i] = accounts[i].name;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(accs, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					TextView tv_account = (TextView) findViewById(R.id.tv_account);
					tv_account.setText(accs[which].toString());
					break;
				default:
					tv_account = (TextView) findViewById(R.id.tv_account);
					tv_account.setText(accs[which].toString());
					break;
				}
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setTitle("Choose an account");
		dialog.show();
	}

	protected void setAccountPref(String account_name, String gender, int age) {
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putString("account", account_name);
		//prefEditor.putString("user-name", user_name);
		prefEditor.putString("gender", gender);
		prefEditor.putInt("age", age);

		prefEditor.commit();

		try {
			FileOutputStream fos = ctx.openFileOutput("temp_data" + ".txt",
					Context.MODE_PRIVATE);
			Writer out = new OutputStreamWriter(fos);
			out.write(user_info.getString("user-name", "default"));
			out.write(user_info.getString("gender", "error") + "\n");
			out.write(user_info.getInt("age", -1) + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d("MAIN",
				"user_info account = "
						+ user_info.getString("account", "error"));
	//	Log.d("MAIN",
		//		"user_name = " + user_info.getString("user-name", "error"));
		Log.d("MAIN",
				"user_info gender = " + user_info.getString("gender", "error"));

		Log.d("MAIN", "user_info age = " + user_info.getInt("age", -1));
	}
}

