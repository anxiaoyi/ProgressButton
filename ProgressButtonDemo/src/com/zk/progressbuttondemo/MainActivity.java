package com.zk.progressbuttondemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.zk.ProgressButton;

public class MainActivity extends Activity {

	final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onBlueProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, null, null, null, 30);
	}

	public void onPurpleProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, null, null, null, 60);
	}

	public void onGreenProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, null, null, null, 90);
	}

	public void onYellowProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, null, null, null, 300);
	}

	public void onRedProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, null, null, null);
	}

	public void onRedWithTextProgressBtnCicked(View v) {
		simulateDownload((ProgressButton) v, "Begin Download",
				"Downloading...", "Download Finish");
	}

	public void onRedWithGreenProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, "Begin Download",
				"Downloading...", "Download Finish");
	}

	public void onRedWithCornerProgressBtnClicked(View v) {
		simulateDownload((ProgressButton) v, "Begin Download",
				"Downloading...", "Download Finish");
	}

	private void simulateDownload(final ProgressButton pb,
			final String beginText, final String downloadingText,
			final String finishText) {
		simulateDownload(pb, beginText, downloadingText, finishText, 30);
	}

	private void simulateDownload(final ProgressButton pb,
			final String beginText, final String downloadingText,
			final String finishText, final int sleepTimeInMills) {
		if (Math.abs(pb.getProgressRation() - 1.0F) < 0.000001F) {
			pb.setProgressRation(.0F);
		}
		new Thread(new Runnable() {
			int progress = (int) (pb.getProgressRation() * 100);

			public void run() {

				if (Math.abs(pb.getProgressRation() - 0.0F) < 0.000001F) {
					setText(pb, beginText);
				}

				while (progress < 100) {
					setText(pb, downloadingText);
					try {
						Thread.sleep(sleepTimeInMills);
						progress++;
					} catch (InterruptedException e) {
						break;
					}
					mHandler.post(new Runnable() {
						public void run() {
							pb.setProgressRation(progress * 0.01F);
						}
					});
				}

				setText(pb, finishText);
			}
		}).start();
	}

	private void setText(final ProgressButton pb, final String text) {
		if (!TextUtils.isEmpty(text)) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					pb.setText(text);
				}
			});
		}
	}
}
