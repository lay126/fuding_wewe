/*
 * Copyright (c) 2014 Samsung Electronics Co., Ltd. All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that 
 * the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright notice, 
 *       this list of conditions and the following disclaimer in the documentation and/or 
 *       other materials provided with the distribution. 
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its contributors may be used to endorse or 
 *       promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package wewe.fuding.activity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

import javax.security.cert.X509Certificate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAAuthenticationToken;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class HelloAccessoryProviderService extends SAAgent {
    public static final int SERVICE_CONNECTION_RESULT_OK = 0;
    public static final int HELLOACCESSORY_CHANNEL_ID = 104;
    public static final String TAG = "HelloAccessoryProviderService";
    public Boolean isAuthentication = false;
    public Context mContext = null;

    private final IBinder mBinder = new LocalBinder();
//    private int authCount = 1;

    HashMap<Integer, HelloAccessoryProviderConnection> mConnectionsMap = null;

    public HelloAccessoryProviderService() {
        super(TAG, HelloAccessoryProviderConnection.class);
    }
    
    
    

    @Override
    public void onCreate() {
        super.onCreate();

        SA mAccessory = new SA();
        try {
            mAccessory.initialize(this);
        } catch (SsdkUnsupportedException e) {
            // Error Handling
        } catch (Exception e1) {
            e1.printStackTrace();
            /*
             * Your application can not use Samsung Accessory SDK. You application should work smoothly
             * without using this SDK, or you may want to notify user and close your application gracefully (release
             * resources, stop Service threads, close UI thread, etc.)
             */
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    protected void onAuthenticationResponse(SAPeerAgent uPeerAgent, SAAuthenticationToken authToken, int error) {
        if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_CERTIFICATE_X509) {
            mContext = getApplicationContext();
            byte[] myAppKey = getApplicationCertificate(mContext);

            if (authToken.getKey() != null) {
                boolean matched = true;
                if (authToken.getKey().length != myAppKey.length) {
                    matched = false;
                } else {
                    for (int i = 0; i < authToken.getKey().length; i++) {
                        if (authToken.getKey()[i] != myAppKey[i]) {
                            matched = false;
                        }
                    }
                }
                if (matched) {
                    acceptServiceConnectionRequest(uPeerAgent);
                }
            }
        } else if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_NONE)
            Log.e(TAG, "onAuthenticationResponse : CERT_TYPE(NONE)");
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket thisConnection, int result) {
        if (result == CONNECTION_SUCCESS) {
            if (thisConnection != null) {
                HelloAccessoryProviderConnection myConnection = (HelloAccessoryProviderConnection) thisConnection;

                if (mConnectionsMap == null) {
                    mConnectionsMap = new HashMap<Integer, HelloAccessoryProviderConnection>();
                }
                myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);
                mConnectionsMap.put(myConnection.mConnectionId, myConnection);
            }
        }
        else if (result == CONNECTION_ALREADY_EXIST) {
            Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
        }
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        /*
         * The authenticatePeerAgent(peerAgent) API may not be working properly depending on the firmware
         * version of accessory device. Recommend to upgrade accessory device firmware if possible.
         */

        // if(authCount%2 == 1)
        // isAuthentication = false;
        // else
        // isAuthentication = true;
        // authCount++;

        isAuthentication = false;

        if (isAuthentication) {
            Toast.makeText(getBaseContext(), "Authentication On!", Toast.LENGTH_SHORT).show();
            authenticatePeerAgent(peerAgent);
        }
        else {
            Toast.makeText(getBaseContext(), "Authentication Off!", Toast.LENGTH_SHORT).show();
            acceptServiceConnectionRequest(peerAgent);
        }
    }

    private static byte[] getApplicationCertificate(Context context) {
        if (context == null) {
            return null;
        }
        byte[] cert = null;
        String packageName = context.getPackageName();
        if (context != null) {
            try {
                PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (pkgInfo == null) {
                    return null;
                }
                Signature[] sigs = pkgInfo.signatures;
                if (sigs == null) {
                } else {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream stream = new ByteArrayInputStream(sigs[0].toByteArray());
                    X509Certificate x509cert = X509Certificate.getInstance(stream);
                    cert = x509cert.getPublicKey().getEncoded();
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (javax.security.cert.CertificateException e) {
                e.printStackTrace();
            }
        }
        return cert;
    }

    public class LocalBinder extends Binder {
        public HelloAccessoryProviderService getService() {
            return HelloAccessoryProviderService.this;
        }
    }

    public class HelloAccessoryProviderConnection extends SASocket {
        private int mConnectionId;

        public HelloAccessoryProviderConnection() {
            super(HelloAccessoryProviderConnection.class.getName());
        }

        @Override
        public void onError(int channelId, String errorString, int error) {
        }
        public void sendMsgToWatch2(String msg) {
			Log.d(TAG, "In sendMsgToWatch");
			final String message = msg;
			final HelloAccessoryProviderConnection uHandler = mConnectionsMap
					.get(Integer.parseInt(String.valueOf(mConnectionId)));
			if (uHandler == null) {
				Log.e(TAG,
						"Error, can not get HelloAccessoryProviderConnection handler");
				return;
			}
			Log.d(TAG, message);
			new Thread(new Runnable() {
				public void run() {
					try {
						uHandler.send(HELLOACCESSORY_CHANNEL_ID,
								message.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
        
        // 기어의 메시지 요청을 받거나 브로트캐스트를 수신하는 리시버 
        @Override
        public void onReceive(int channelId, byte[] data) {
            Time time = new Time();
            time.set(System.currentTimeMillis());
            String timeStr = " " + String.valueOf(time.minute) + ":" + String.valueOf(time.second);
            String strToUpdateUI = new String(data);
            String foodstr = "ramen:재료=신라면1봉지,물500cc:레시피=1.라면을 끓는물에 5분간 끓인다.";
            
            final String message = strToUpdateUI.concat(foodstr+timeStr);
            
            GearDataReceiver gearDataReceiver = new GearDataReceiver();
            IntentFilter intentFilter = new IntentFilter("myData");
			registerReceiver(gearDataReceiver, intentFilter);
			
            final HelloAccessoryProviderConnection uHandler = mConnectionsMap.get(Integer.parseInt(String.valueOf(mConnectionId)));
            if (uHandler == null) {
                return;
            }
            /*
            new Thread(new Runnable() {
                public void run() {
                    try {
                        uHandler.send(HELLOACCESSORY_CHANNEL_ID, message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            */
        }
        
        // 기어에 data를 전송하기 위한 브로드캐스트리시버
        // 메인 액티비티로 부터 오는 브로드캐스트를 수신
        private class GearDataReceiver extends BroadcastReceiver {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			if (intent.getAction().equals("myData")) {
    				String data = intent.getStringExtra("data");
    				notifyGear(data);
    			}
    		}
    	}
        
        //기어에 data를 전송하는 함수
        public void notifyGear(String notification) {

    		for (HelloAccessoryProviderConnection provider : mConnectionsMap
    				.values()) {
    			provider.sendMsgToWatch2(notification);
    		}
    	}

        @Override
        protected void onServiceConnectionLost(int errorCode) {
            if (mConnectionsMap != null) {
                mConnectionsMap.remove(mConnectionId);
            }
        }
    }
    
}
