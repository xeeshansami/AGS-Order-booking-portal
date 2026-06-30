# AGS Order Booking Portal — Play Store Release Guide

This project is now configured for a Play Store release. Because the build needs
the Android SDK, the final signed bundle must be produced on a machine with
Android Studio / the SDK installed (it can't be built in the Cowork sandbox).

## 1. What was changed for release readiness

**Manifest & security (`app/src/main/AndroidManifest.xml`)**
- Removed the duplicate `ACCESS_NETWORK_STATE` permission (the only permission
  removal — approved).
- `android:allowBackup="false"` + added `data_extraction_rules.xml` so customer
  data isn't copied off-device via the backup framework.

**Network security (`res/xml/network_security_config.xml`)**
- Cleartext (HTTP) is now denied by default; only HTTPS is allowed.
- Fixed the malformed domain entry (`https://mobile.agssukkur.com` → host-only
  `mobile.agssukkur.com`). The API base URL is already HTTPS, so nothing breaks.

**`app/build.gradle`**
- `targetSdkVersion` 33 → **34** (Play requires API 34 for updates).
- Release build no longer forces cleartext (`usesCleartextTraffic: "false"`).
- Added a release `signingConfig` that reads credentials from Gradle properties
  / environment variables (no secrets in the repo).
- Removed duplicate dependency declarations (firebase-core/auth/database, glide)
  and flattened the nested `dependencies { }` block. No libraries were dropped.

**`.gitignore`** — added rules so keystores and `credentials.txt` are never
committed.

> Left unchanged on purpose (your call): `READ_MEDIA_AUDIO`, `READ_MEDIA_VIDEO`,
> `ACCESS_WIFI_STATE`, the legacy `com.android.support` libraries, and minify
> (R8) which stays **off**.

## 2. Configure signing

**The correct upload key is `keystore/agsadil.jks`** (alias `agsadil`), whose
fingerprint `95:05:B8:56:FD:CE:60:04:2E:C9:74:00:60:C4:DC:DE:3B:C3:88:EB` matches
what Play expects. Do NOT use the other keystores — signing with the wrong one
causes the "App Bundle is signed with the wrong key" rejection.

Signing credentials are read from a gitignored **`keystore.properties`** at the
repo root (already created). It contains:

```
AGS_STORE_FILE=keystore/agsadil.jks
AGS_STORE_PASSWORD=agsadil
AGS_KEY_ALIAS=agsadil
AGS_KEY_PASSWORD=agsadil
```

The build picks this up automatically and signs the release with the right key.
(For extra safety you can instead move these into your global
`~/.gradle/gradle.properties` or set them as environment variables.)

If the keystore/`credentials.txt` are already tracked by git, untrack them:

```
git rm --cached keystore/*.jks keystore/credentials.txt agskeystore
```

## 3. Bump the version for each release

In `app/build.gradle` `defaultConfig`, increase `versionCode` (currently **13**)
by 1 for every upload, and update `versionName` (currently **15.1.0**) as needed.

## 4. Build the bundle

```
# Android App Bundle (preferred by Play):
./gradlew clean bundleRelease
# output: app/build/outputs/bundle/release/app-release.aab

# Or a signed APK:
./gradlew clean assembleRelease
# output: app/build/outputs/apk/release/app-release.apk
```

## 5. Before you upload — checklist

- [ ] Signing properties set; build produces a **signed** `.aab`.
- [ ] `versionCode` incremented above the last published value.
- [ ] App tested on a device: login, customer update + map picker, PDF, FCM.
- [ ] Play Console **Data safety** form completed (location + any media access).
- [ ] Privacy policy URL added (required because the app uses location).
- [ ] Maps work in release — confirm **"Maps SDK for Android"** is enabled for
      the API key and that the release SHA-1 is added to the key restrictions.

## 6. Worth addressing soon (not blocking the build)

- **`SmsManager.sendTextMessage()` in `VarificationActivity`** runs without a
  declared `SEND_SMS` permission, so it will fail at runtime. `SEND_SMS` is a
  Play-restricted permission. Either remove that SMS code or switch to an
  SMS-gateway/OTP API. Tell me which and I'll implement it.
- Consider scoping `WRITE_EXTERNAL_STORAGE`/`READ_EXTERNAL_STORAGE` with
  `android:maxSdkVersion="32"` since they're ignored on Android 13+.
- The legacy `com.android.support` libraries mixed with AndroidX work via
  Jetifier but are worth migrating later. Say the word and I'll do it carefully.
