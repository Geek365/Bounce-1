package com.gishump.bounce.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.gishump.bounce.Bounce;
import com.gishump.bounce.DialogResult;
import com.gishump.bounce.RequestHandler;

public class AndroidLauncher extends AndroidApplication implements RequestHandler {

	private Activity context;
	private IabHelper mHelper;
	private int levelsPaidFor = -1; // -1 Value Not Yet Retrieved
	private int levelsAvailable=54;
	private boolean purchasesAvailable;
	private String nextAvailableSKU="";
	private GridView[] levelGrids;
    private PagerAdapter levelPageAdapter;
    private ViewPager levelPager;

	IabHelper.QueryInventoryFinishedListener
			mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory)
		{
			if (result.isFailure()) {
				purchasesAvailable=false;
			}
			else {
				if (inventory.hasPurchase("Pack 3")) { purchasesAvailable = false; }
				else {
					purchasesAvailable = false;
					//nextAvailableSKU = (inventory.hasPurchase("Pack 2")) ? "Pack 3" : "Pack 2";
					//nextAvailableSKU = (inventory.hasPurchase("Pack 1")) ? nextAvailableSKU : "Pack 1";
				}
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock=true;
		config.useAccelerometer=false;
		config.useCompass=false;
		config.useImmersiveMode=true;
		mHelper = new IabHelper(this, "MyKey");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    levelsPaidFor = 30;
                    purchasesAvailable = false;
                } // 30 Levels Included With Game By Default
                else {
                    ArrayList additionalSkuList = new ArrayList();
                    additionalSkuList.add("Pack 1");
                    additionalSkuList.add("Pack 2");
                    additionalSkuList.add("Pack 3");
                    mHelper.queryInventoryAsync(true, additionalSkuList,
                            mQueryFinishedListener);
                }
            }
        });
		initialize(new Bounce(this), config);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "New");
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void levelCompleteDialog(final DialogResult result, final int attempts){
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(AndroidLauncher.this, android.R.style.Theme_Material_Light_Dialog))
						.setTitle("Level Completed")
						.setMessage("Attempts: " + String.valueOf(attempts))
						.setCancelable(false)
						.setNegativeButton("Replay Level", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.no();
								dialog.cancel();
							}
						})
						.setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.yes(0);
								dialog.cancel();
							}
						})
						.create();
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				dialog.show();
				dialog.getWindow().getDecorView().setSystemUiVisibility(
						context.getWindow().getDecorView().getSystemUiVisibility());
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			}
		});
	}

	@Override
	public void noPurchasesAvailable(final DialogResult result) {
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(AndroidLauncher.this, android.R.style.Theme_Material_Light_Dialog))
                        .setTitle("No More Levels Available")
                        .setMessage("Congratulations, you have finished all of the available levels in the game.\n" +
                                "There currently aren't any more levels available for purchase.\n" +
                                "You may check for new levels in the future by selecting 'Get New Levels' in the menu.")
                        .setCancelable(false)
                        .setNegativeButton("Exit Game", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.no();
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Replay An Old Level", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.yes(0);
                                dialog.cancel();
                            }
                        }).create();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                dialog.show();
                dialog.getWindow().getDecorView().setSystemUiVisibility(
                        context.getWindow().getDecorView().getSystemUiVisibility());
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });
	}

	@Override
	public void levelSelector(final DialogResult result) {
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
                if (levelsAvailable%16==0) { levelGrids = new GridView[levelsAvailable/16]; }
                else { levelGrids = new GridView[(levelsAvailable/16)+1]; }
                //System.out.println(levelGrids.length);
                for (int i=0; i < levelGrids.length; i++) {
                    levelGrids[i] = new GridView(AndroidLauncher.this);
                    levelGrids[i].setNumColumns(4);
                    levelGrids[i].setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
                }
                levelPager = new ViewPager(AndroidLauncher.this);
                levelPager.setAdapter(new LevelGridPager(levelGrids));
				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AndroidLauncher.this, android.R.style.Theme_Material_Light_Dialog));
				builder.setCancelable(false);
				final FrameLayout parent = new FrameLayout(new ContextThemeWrapper(AndroidLauncher.this, android.R.style.Theme_Material_Light_Dialog));
				parent.addView(levelPager);
				builder.setView(parent);
				builder.setTitle("Select Level");
				final Dialog dialog = builder.create();
                for (int i=0; i<levelGrids.length; i++) {
                    levelGrids[i].setAdapter(new LevelAdapter(AndroidLauncher.this, i, levelsAvailable, new View.OnClickListener() {
                        @Override
                        @SuppressWarnings("")
                        public void onClick(View v) {
                            result.yes((int) v.getTag());
                            dialog.cancel();
                        }
                    }));
                }
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				dialog.show();
				dialog.getWindow().getDecorView().setSystemUiVisibility(
						context.getWindow().getDecorView().getSystemUiVisibility());
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			}
		});
	}

	@Override
	public void paymentPrompt(final DialogResult result){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(AndroidLauncher.this, android.R.style.Theme_Material_Light_Dialog))
						.setTitle("New Levels Available")
						.setMessage("Congratulations, you have finished all of your available levels.\n" +
										"There are 30 new levels available for purchase."
						)
						.setCancelable(false)
						.setNegativeButton("Replay An Old Level", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.no();
								dialog.cancel();
							}
						})
						.setPositiveButton("Purchase Levels", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.yes(0);
								dialog.cancel();
							}
						}).create();
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				dialog.show();
				dialog.getWindow().getDecorView().setSystemUiVisibility(
						context.getWindow().getDecorView().getSystemUiVisibility());
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			}
		});
	}

	@Override
	public int showMenu() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Activity a1 = AndroidLauncher.this;
				a1.openOptionsMenu();
			}

		});
		return 0;
	}

	@Override
	public void userPayment(){ }

	@Override
	public boolean arePurchasesAvailable () {
		return purchasesAvailable;
	}

	@Override
	public int getLevelsPaidFor () {
		return levelsPaidFor;
	}
}