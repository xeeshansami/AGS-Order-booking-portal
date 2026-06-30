# Firebase Push & Google Maps — setup steps

The code side is done. These remaining steps are console/cloud configuration that
can only be done in the Firebase and Google Cloud dashboards.

## 1. Firebase push notifications

The new `google-services.json` (project **freelance-e1ba3**, package
`com.agsadil.agssalesandroidclientorderdocter`) is now in `app/`. The app already:
- fetches & caches the FCM token on startup,
- subscribes every device to the topic **`general`**,
- shows notifications (foreground + data messages) and requests the Android 13+
  notification permission.

### Option A — Firebase Console campaign (easiest)
Firebase Console → **Messaging** → **New campaign** → Notifications →
write title/body → **Target = Topic → `general`** → review → publish.
Every installed device is subscribed to `general`, so all users receive it.

### Option B — Send via API to the topic
Use the **HTTP v1** API (the old legacy server-key API is shut down). You need a
service-account OAuth token.

```
POST https://fcm.googleapis.com/v1/projects/freelance-e1ba3/messages:send
Authorization: Bearer <oauth2_access_token>
Content-Type: application/json

{
  "message": {
    "topic": "general",
    "notification": { "title": "Order update", "body": "Your order shipped" }
  }
}
```

Get the bearer token from a service account JSON
(Firebase Console → Project settings → Service accounts → Generate new private key)
using the Google Auth library, scope `https://www.googleapis.com/auth/firebase.messaging`.

To target ONE device instead of everyone, send to its token (the app stores it via
`SharedPreferenceManager.getFcmToken()` — POST it to your backend on login if you
want server-side per-user sends).

## 2. Google Maps — why tiles don't show

The Maps API key (`AIzaSyCG3S4m8EJeJK_PVb1EDYV42JN0AImCJEU`) is wired correctly in
the app. A blank/grey map means the key isn't authorized for Maps on this project.

In **Google Cloud Console** for project **freelance-e1ba3**:

1. **APIs & Services → Library →** enable **"Maps SDK for Android"**.
   (This is the #1 cause of a blank map.)
2. **APIs & Services → Credentials →** open that API key →
   **Application restrictions → Android apps →** add an entry:
   - Package name: `com.agsadil.agssalesandroidclientorderdocter`
   - SHA-1 of the cert you build/sign with. Add **all** that apply:
     - Debug:  run `keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android`
     - Release/upload key (CORRECT one Play expects — `keystore/agsadil.jks`, alias `agsadil`): `95:05:B8:56:FD:CE:60:04:2E:C9:74:00:60:C4:DC:DE:3B:C3:88:EB`
     - The SHA-1 already registered in google-services.json: `76:42:A6:52:8C:15:AB:6F:89:00:79:45:0C:13:DF:8D:D2:4A:F5:47`
     - If using **Play App Signing**, also add the app-signing SHA-1 from
       Play Console → Setup → App signing.
3. **API restrictions** on the key: allow at least "Maps SDK for Android".
4. Make sure **billing is enabled** on the project (Maps requires it).

Tip for quick testing: temporarily set the key to "no application restrictions"
to confirm the map renders, then lock it back down with the SHA-1s above.

## 3. "Google Maps not installed" — fixed in code

This was the Android 11+ package-visibility rule (with `targetSdk 34`,
`resolveActivity()` couldn't see the maps app). A `<queries>` block was added to
the manifest and the Order Booking button now falls back to any installed maps app.
No console action needed.
