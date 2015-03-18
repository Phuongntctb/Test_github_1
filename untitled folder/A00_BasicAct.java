package jp.entermotion.prea;

import jp.entermotion.prea.common.Preferences;
import jp.entermotion.prea.common.ShowLog;
import jp.entermotion.prea.common.Utils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * A00_BasicAct : basic of an activity
 * 
 * @version 1.0
 * @since 2015-01-19
 * @author vfa.Android
 * 
 */
public class A00_BasicAct extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setColorStatusBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.a01_top, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			if (Utils.FLAG_TESTING && ShowLog.IS_FLAG_DEBUG) {
				showPopupRefreshApp(this,
						Preferences.getString(this, Preferences.DEVICE_ID_KEY));
			}
			return true;

		case R.id.a01_actbar_gift:
			startActivity(new Intent(this, A03_MyGifts.class)
					.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			return true;

		case R.id.a01_actbar_settings:
			goHomeTop(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// *************************************************************************
	// ***** COMMON FUNCTION
	// *************************************************************************
	/**
	 * goHomeTop
	 * 
	 * @param context
	 */
	public static void goHomeTop(Activity context) {
		final Intent mIntent = new Intent(context, Z01_Menu.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(mIntent);
	}

	/**
	 * goTopScren
	 * 
	 * @param context
	 */
	public static void goTopScreen(Context context) {
		final Intent mItent = new Intent(context, A01_TopAct.class);
		mItent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mItent);
	}

	/**
	 * customActionBar
	 * 
	 * @param icon
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void customActionBar(int icon, int title, Context context) {
		ActionBar mActionBar = getActionBar();

		if (icon == R.drawable.icon_back) {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setIcon(R.drawable.icon_back);
		} else {
			mActionBar.setIcon(getResources().getDrawable(icon));
			if (Utils.FLAG_TESTING
					&& ShowLog.IS_FLAG_DEBUG
					&& context.getClass().getName()
							.equals(A01_TopAct.class.getName())) {
				mActionBar.setHomeButtonEnabled(true);
			}
		}
		mActionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.status_bar)));
		mActionBar.setDisplayShowCustomEnabled(true);

		LayoutParams mLayout = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, Gravity.CENTER);
		ViewGroup mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
				R.layout.a01_top_actionbar_custom, null);
		if (title != R.string.msg_blank) {
			TextView txtTitle = (TextView) mActionBarLayout
					.findViewById(R.id.textviewactivityname);
			txtTitle.setText(title);
			txtTitle.setVisibility(View.VISIBLE);
		}
		mActionBar.setTitle("");
		mActionBar.setCustomView(mActionBarLayout, mLayout);
	}

	/**
	 * setColorStatusBar
	 * 
	 */
	@SuppressLint({ "NewApi" })
	public void setColorStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window mWindow = getWindow();
			mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			mWindow.setStatusBarColor(getResources().getColor(
					R.color.status_bar));
		}
	}

	/**
	 * setTitle
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		TextView mTitleTv = (TextView) findViewById(R.id.header_title);
		mTitleTv.setText(title);
	}

	/**
	 * setDescription
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		TextView titleTv = (TextView) findViewById(R.id.header_description_txt);
		titleTv.setText(description);
	}

	/**
	 * isLogin
	 * 
	 * @return
	 */
	public static boolean isLogin(Context mContext) {
		return Preferences.getBoolean(mContext, Preferences.LOGIN_FLAG);
	}

	/**
	 * goToWebVIew
	 * 
	 * @param url
	 * @param title
	 */
	public void goToWebVIew(String url, String title) {
		Intent wvIntent = new Intent(this, WebViewAct.class);
		wvIntent.putExtra(Utils.INTENT_WEB_VIEW_URL_KEY, url);
		wvIntent.putExtra(Utils.INTENT_WEB_VIEW_TITLE_KEY, title);
		wvIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(wvIntent);
	}

	// *************************************************************************
	// ***** SHOW POPUP
	// *************************************************************************
	/**
	 * ShowPopupConfirmLoginDefault
	 * 
	 * @param mContext
	 * @param msgConfirm
	 */
	public void showPopupConfirmLoginDefault(final Context mContext,
			String msgConfirm) {
		AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Message
		mAlertDialog.setMessage(mContext
				.getString(R.string.a02_gift_dl_confirm_msg));

		// Setting Positive "Yes" Button
		mAlertDialog.setPositiveButton(
				mContext.getString(R.string.a02_gift_dl_confirm_register),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Preferences.setInteger(getBaseContext(),
								Preferences.A08_FROM_TOP_FLAG, 1);
						Preferences.setInteger(getBaseContext(),
								Preferences.LOGIN_FROM_DETAIL_FLAG, 1);
						startActivity(new Intent(mContext,
								A08_RegisterAct.class));
					}
				});

		// Setting Negative "NO" Button
		mAlertDialog.setNegativeButton(
				mContext.getString(R.string.a02_gift_dl_confirm_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		AlertDialog mDialog = mAlertDialog.show();

		TextView mMessageText = (TextView) mDialog
				.findViewById(android.R.id.message);
		mMessageText.setGravity(Gravity.CENTER);
	}

	/**
	 * showPopup
	 * 
	 * @param msgConfirm
	 */
	public void showPopupConfirmLogin(final Context mContext, String msgConfirm) {
		final Dialog mDialog = new Dialog(this, R.style.viewRecommend);
		mDialog.setContentView(R.layout.a02_c_dialog_confirm);

		// ***** 写真を撮る
		TextView mTxtMsg = (TextView) mDialog.findViewById(R.id.txtMsg);
		mTxtMsg.setText(mContext.getString(R.string.a02_gift_dl_confirm_msg));

		// *****
		TextView mBtnClose = (TextView) mDialog.findViewById(R.id.btn_close);
		mBtnClose.setText(mContext
				.getString(R.string.a02_gift_dl_confirm_cancel));
		mBtnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		TextView mTvOK = (TextView) mDialog.findViewById(R.id.btn_ok);
		mTvOK.setText(mContext.getString(R.string.a02_gift_dl_confirm_register));
		mTvOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Preferences.setInteger(getBaseContext(),
						Preferences.A08_FROM_TOP_FLAG, 1);
				Preferences.setInteger(getBaseContext(),
						Preferences.LOGIN_FROM_DETAIL_FLAG, 1);
				startActivity(new Intent(mContext, A08_RegisterAct.class));
				mDialog.dismiss();
			}
		});
		mDialog.show();
		mDialog.setCancelable(true);
	}

	/**
	 * ShowPopupConfirmCouponPressent
	 * 
	 * @param mContext
	 * @param url
	 */
	public void showPopupConfirmCouponPressent(final Context mContext,
			final String url) {
		AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

		mAlertDialog.setMessage(mContext
				.getString(R.string.a04_dialog_confirm_content));

		// Setting Positive "Yes" Button
		mAlertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ShowLog.showLogDebug("Papico_Coupondetail",
								"Papico_Coupondetail + url web :" + url);
						goToWebVIew(url, null);
					}
				});

		// Setting Negative "NO" Button
		mAlertDialog.setNegativeButton(
				mContext.getString(R.string.a04_dialog_confirm_cancel_button),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		AlertDialog mDialog = mAlertDialog.show();

		TextView mMessageText = (TextView) mDialog
				.findViewById(android.R.id.message);
		mMessageText.setGravity(Gravity.CENTER);
	}

	/**
	 * showDialogResultDefault
	 * 
	 * @param context
	 * @param result
	 * @param user
	 */
	public void showDialogResultDefault(final Context context,
			final String result, String user) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
		if (result.equals("true")) {
			Preferences.setBoolean(getBaseContext(), Preferences.LOGIN_FLAG,
					true);
			Preferences.setString(getBaseContext(), Preferences.LOGIN_USER,
					user);
			Preferences.setBoolean(getBaseContext(),
					Preferences.LOGIN_FLAG_FIRST, true);
			if (Preferences.getInt(getBaseContext(),
					Preferences.LOGIN_FROM_DETAIL_FLAG) == 0)
				goTopScreen(context);
			finish();
		} else {
			mBuilder.setMessage(result);
			mBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
						}
					});
			AlertDialog mDialog = mBuilder.show();
			TextView mMessageText = (TextView) mDialog
					.findViewById(android.R.id.message);
			mMessageText.setGravity(Gravity.CENTER);
		}
	}

	/**
	 * showPopup
	 * 
	 * @param msgConfirm
	 */
	public void showPopupSNSSendMessage(final Context mContext,
			String msgConfirm) {
		final Dialog mDialog = new Dialog(this, R.style.viewRecommend);
		mDialog.setContentView(R.layout.a02_c_dialog_confirm);

		// ***** 写真を撮る
		TextView mTxtMsg = (TextView) mDialog.findViewById(R.id.txtMsg);
		mTxtMsg.setText(mContext.getString(R.string.a02_gift_dl_confirm_msg));

		// *****
		TextView mBtnClose = (TextView) mDialog.findViewById(R.id.btn_close);
		mBtnClose.setText(mContext
				.getString(R.string.a02_gift_dl_confirm_cancel));
		mBtnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		TextView mTvOK = (TextView) mDialog.findViewById(R.id.btn_ok);
		mTvOK.setText(mContext.getString(R.string.a02_gift_dl_confirm_register));
		mTvOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Preferences.setInteger(getBaseContext(),
						Preferences.A08_FROM_TOP_FLAG, 1);
				Preferences.setInteger(getBaseContext(),
						Preferences.LOGIN_FROM_DETAIL_FLAG, 1);
				startActivity(new Intent(mContext, A08_RegisterAct.class));
				mDialog.dismiss();
			}
		});
		mDialog.show();
		mDialog.setCancelable(true);
	}

	/**
	 * Show Pop Up Refresh App for testing
	 * 
	 * @param mContext
	 * @param url
	 */
	public void showPopupRefreshApp(final Context mContext,
			final String app_serial) {
		AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

		mAlertDialog.setMessage("app_serial"
				+ mContext.getString(R.string.popup_refresh_app));

		// Setting Positive "Yes" Button
		mAlertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						Utils.resetApp(mContext);
						goTopScreen(mContext);
					}
				});

		// Setting Negative "NO" Button
		mAlertDialog.setNegativeButton(
				mContext.getString(R.string.a04_dialog_confirm_cancel_button),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		AlertDialog mDialog = mAlertDialog.show();

		TextView mMessageText = (TextView) mDialog
				.findViewById(android.R.id.message);
		mMessageText.setGravity(Gravity.CENTER);
	}
}