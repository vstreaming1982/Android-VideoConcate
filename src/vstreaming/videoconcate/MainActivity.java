package vstreaming.videoconcate;

import java.io.File;
import java.io.IOException;

import vstreaming.videoconcate.ffmpeg.FfmpegJob;
import vstreaming.videoconcate.ffmpeg.Utils;
import za.jamie.androidffmpegcmdline.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "MainActivity";
	

	private Button mStartButton;
	
	private String mFfmpegInstallPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViews();
		
	
		installFfmpeg();
		
		mStartButton.setOnClickListener(this);
	}
	
	private void findViews() {
		
		mStartButton = (Button) findViewById(R.id.button1);
	}
	
	private void installFfmpeg() {
		File ffmpegFile = new File(getCacheDir(), "ffmpeg");
		mFfmpegInstallPath = ffmpegFile.toString();
		Log.d(TAG, "ffmpeg install path: " + mFfmpegInstallPath);
		
		if (!ffmpegFile.exists()) {
			try {
				ffmpegFile.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, "Failed to create new file!", e);
			}
			Utils.installBinaryFromRaw(this, R.raw.ffmpeg, ffmpegFile);
		}
		
		ffmpegFile.setExecutable(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath);
		loadJob(job);		
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please wait.", 
				true, false);
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				job.create();//.run();
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				progressDialog.dismiss();
				Toast.makeText(MainActivity.this, "Ffmpeg job complete.", Toast.LENGTH_SHORT).show();
			}
			
		}.execute();
	}
	
	private void loadJob(FfmpegJob job) {
		job.inputPath1 = "/mnt/sdcard/asd.mp4";
		job.inputPath2 = "/mnt/sdcard/asd.mp4";
		job.outputPath = "/mnt/sdcard/2.mp4";
	}

}
