package com.zeroone_creative.basicapplication.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.system.AppConfig;
import com.zeroone_creative.basicapplication.model.system.UserAccount;
import com.zeroone_creative.basicapplication.view.activity.CommentActivity_;

import java.util.Collections;
import java.util.List;

//ノーティフィケーションを受けとるReceiver
public class RemindLaterReceiver extends BroadcastReceiver implements LogInCallback {
	//Intent key
	private Context mContext;
	private PendingIntent sender;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		Log.d("Remind", "onReceive");
		Parse.initialize(context, AppConfig.APPLICATION_ID, AppConfig.CLIENT_KEY);
		UserAccount account = new UserAccount(context);
		ParseUser.logInInBackground(account.username, account.password, this);
		ParseObject.registerSubclass(ImageParseObject.class);
	}

	@Override
	public void done(ParseUser parseUser, ParseException e) {
		if (e == null) {
			ParseQuery<ImageParseObject> query = ParseQuery.getQuery("Image");
			query.whereEqualTo("userId", parseUser.getObjectId());
			query.whereEqualTo("objectId", "xskR6U6EJt");
			query.findInBackground(new FindCallback<ImageParseObject>() {
				public void done(List<ImageParseObject> imageList, ParseException e) {
					if (e == null) {
						if(imageList.size() < 1) return;
						Collections.shuffle(imageList);
						Intent sendIntent = CommentActivity_.intent(mContext).imageId(imageList.get(0).getObjectId()).get();
						sender = PendingIntent.getActivity(mContext, 0, sendIntent, 0);
						//通知オブジェクトの生成
						Notification noti = new NotificationCompat.Builder(mContext)
								.setTicker(mContext.getString(R.string.remind_later_title))
								.setContentTitle(mContext.getString(R.string.remind_later_title))
								.setContentText(mContext.getString(R.string.remind_later_message))
								.setSmallIcon(R.mipmap.ic_launcher)
								.setLargeIcon(ImageUtil.decodeImageBase64(imageList.get(0).getBody()))
								.setVibrate(new long[]{0, 200, 100, 200, 100, 200})
								.setAutoCancel(true)
								.setContentIntent(sender)
								.build();
						NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
						manager.notify(0, noti);

					} else {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
