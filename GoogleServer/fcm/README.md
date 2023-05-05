# FCM集成

[官方文档地址](https://firebase.google.com/docs/cloud-messaging?hl=zh-cn)



fcm（Firebase Cloud Messaging）是google提供给安卓开发者的消息推送服务平台。



## FCM 架构概览

FCM 依赖于以下一组组件来构建、传输和接收消息：

1. 用于编写或构建消息请求的工具。通知编辑器提供了一种基于 GUI 的用于创建通知请求的方式。 要实现对所有消息类型的完全自动化和支持，您必须在支持 Firebase Admin SDK 或 FCM 服务器协议的受信任服务器环境中构建消息请求。 此环境可以是 Cloud Functions for Firebase、App Engine 或您自己的应用服务器。

   ![本页中介绍的三个架构层的示意图。](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/diagram-FCM.png)

2. FCM 后端（以及其他函数），它接受消息请求，通过主题对消息执行扇出，并生成消息 ID 等消息元数据。

3. 平台级传输层，用于将消息路由到目标设备、处理消息传送，并在适当情况下应用针对具体平台的配置。此传输层包括：

   - Android 传输层 (ATL)，适用于运行 Google Play 服务的 Android 设备

   - 适用于 Apple 设备的 Apple 推送通知服务 (APNs)

   - Web 应用的网络推送协议

     **注意**：平台级传输层在核心 FCM 产品之外。路由到平台级传输层的 FCM 消息可能受特定于该平台的条款（而不是 FCM 的服务条款）的约束。通过 ATL 的 Android 消息路由受 [Google API 服务条款](https://www.google.com/url?q=https%3A%2F%2Fdevelopers.google.com%2Fterms%2F&%3Bsa=D&%3Bust=1558536676246000&%3Busg=AFQjCNFrlRMLv51d1S9NkWxD0IoYSqJ2Ng&hl=zh-cn)的约束。

4. 用户设备上的 FCM SDK，根据应用的前台/后台状态和任何相关应用逻辑显示通知或处理消息。





## 消息类型

您可以使用 FCM 向客户端发送两种类型的消息：

- 通知消息，有时被称为“显示消息”。此类消息由 FCM SDK 自动处理。
- 数据消息，由客户端应用处理。

通知消息包含一组用户可见的预定义键。与其相对，数据消息只包含用户定义的自定义键值对。通知消息可以包含可选的数据载荷。两种消息类型的载荷上限均为 4000 个字节，但从 Firebase 控制台发送消息时会强制执行 1024 个字符的限制。

| 消息类型 | 如何发送                                                     | 说明                                                         |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 通知消息 | 当客户端应用在后台运行时，FCM SDK 会代表客户端应用向最终用户设备显示消息。如果应用在收到通知时正在前台运行，应用的代码会决定行为。通知消息包含一组预定义的用户可见的键和一个由自定义键值对组成的可选的数据载荷。 | 在可信环境（例如 [Cloud Functions](https://firebase.google.com/docs/functions?hl=zh-cn) 或应用服务器）中，使用 [Admin SDK](https://firebase.google.com/docs/cloud-messaging/admin?hl=zh-cn) 或者 [FCM 服务器协议](https://firebase.google.com/docs/cloud-messaging/server?hl=zh-cn#choose)：设置 `notification` 键。可能包含可选的数据载荷。 一律可折叠。请参阅一些[显示通知示例](https://firebase.google.com/docs/cloud-messaging/send-message?hl=zh-cn#example-notification-message-with-platform-specific-delivery-options)并发送请求载荷。使用 [Notifications Composer](https://console.firebase.google.com/project/_/notification?hl=zh-cn)：输入消息文本、标题等，然后发送。通过提供自定义数据添加可选的数据载荷。 |
| 数据消息 | 客户端应用负责处理数据消息。数据消息仅包含自定义键值对，没有保留键名（请参阅下文）。 | 在可信环境（例如 [Cloud Functions](https://firebase.google.com/docs/functions?hl=zh-cn) 或应用服务器中），使用 [Admin SDK](https://firebase.google.com/docs/cloud-messaging/admin?hl=zh-cn) 或者 [FCM 服务器协议](https://firebase.google.com/docs/cloud-messaging/server?hl=zh-cn#choose)：仅设置 `data` 键。 |

当您的应用在后台运行时，如果您希望 FCM SDK 自动处理通知的显示，请使用通知消息。如果您希望使用自己的客户端应用代码处理消息，请使用数据消息。



FCM 可以发送包含可选的数据载荷的通知消息。在此类情况下，FCM 负责显示通知载荷，而客户端应用负责处理数据载荷。



### 通知消息

应用在后台运行时，通知消息将被传递至通知面板。应用在前台运行时，消息由回调函数处理。





### 数据消息

使用自定义键值对设置适当的键，以将数据载荷发送至客户端应用。

> 请确保未在自定义键值对中使用任何保留字词。保留字词包括“from”“notification”“message_type”或以“google”或“gcm”开头的任何字词。





## 安卓集成

FCM 客户端需要在搭载 Android 4.4 或更高版本且安装了 Google Play 商店应用的设备上运行，或者在搭载 Android 4.4 版本且支持 Google API 的模拟器中运行。请注意，除了使用 Google Play 商店，您还可以通过其他方式部署您的 Android 应用。



### 使用 Firebase 控制台添加 Firebase

如要将 Firebase 添加到您的应用，您需要在 [Firebase 控制台](https://console.firebase.google.com/?hl=zh-cn)和打开的 Android 项目中执行若干任务（例如，从控制台下载 Firebase 配置文件，然后将配置文件移动到 Android 项目中）。

#### **第 1 步**：创建 Firebase 项目

您需要先创建一个要关联到 Android 应用的 Firebase 项目，然后才能将 Firebase 添加到您的 Android 应用。

![image-20230505114226160](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114226160.png)

![image-20230505114241530](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114241530.png)

![image-20230505114317890](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114317890.png)

等待一会。



![image-20230505114404318](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114404318.png)

#### **第 2 步**：在 Firebase 中注册您的应用

如需在 Android 应用中使用 Firebase，您需要向 Firebase 项目注册您的应用。注册应用的过程通常称为将应用“添加”到项目中。

1. 前往 [Firebase 控制台](https://console.firebase.google.com/?hl=zh-cn)。

2. 在项目概览页面的中心位置，点击 **Android** 图标 (plat_android) 或**添加应用**，启动设置工作流。

   ![image-20230505114653273](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114653273.png)

   ![image-20230505114730181](https://xh-blog.oss-cn-guangzhou.aliyuncs.com/image-20230505114730181.png)

3. 在 **Android 软件包名称**字段中输入应用的软件包名称。

4. （可选）输入其他应用信息：**应用别名**和**调试签名证书 SHA-1**。

5. 点击**注册应用**。

#### **第 3 步**：添加 Firebase 配置文件

1.下载 Firebase Android 配置文件 (`google-services.json`)，然后将其添加到您的应用：

1. 点击**下载 google-services.json** 以获取 Firebase Android 配置文件。
2. 将配置文件移到应用的**模块（应用级）**根目录中。

2.在您的**根级（项目级）**Gradle 文件 (`<project>/build.gradle`) 中，将 Google 服务插件添加为 buildscript 依赖项：

为了确保 Firebase SDK 可以访问 `google-services.json` 配置文件中的值，您需要具有 [Google 服务 Gradle 插件](https://developers.google.com/android/guides/google-services-plugin?hl=zh-cn) (`google-services`)。

```
buildscript {

    repositories {
      // Make sure that you have the following two repositories
      google()  // Google's Maven repository
      mavenCentral()  // Maven Central repository
    }

    dependencies {
      ...

      // Add the dependency for the Google services Gradle plugin
      classpath 'com.google.gms:google-services:4.3.15'
    }
}

allprojects {
  ...

  repositories {
    // Make sure that you have the following two repositories
    google()  // Google's Maven repository
    mavenCentral()  // Maven Central repository
  }
}
```

在您的**模块（应用级）**Gradle 文件（通常是 `<project>/<app-module>/build.gradle`）中，添加 Google 服务插件：

```
plugins {
    id 'com.android.application'

    // Add the Google services Gradle plugin
    id 'com.google.gms.google-services'
    ...
}
```

#### **第 4 步**：将 Firebase SDK 添加到您的应用

在您的**模块（应用级）Gradle 文件**（通常是 `<project>/<app-module>/build.gradle`）中，添加您需要在应用中使用的 [Firebase 产品](https://firebase.google.com/docs/android/setup?hl=zh-cn#available-libraries)的依赖项。我们建议使用 [Firebase Android BoM](https://firebase.google.com/docs/android/learn-more?hl=zh-cn#bom) 来实现库版本控制。

```
dependencies {
  // ...

  // Import the Firebase BoM
  implementation platform('com.google.firebase:firebase-bom:31.5.0')

  // When using the BoM, you don't specify versions in Firebase library dependencies

  // Add the dependency for the Firebase SDK for Google Analytics
  implementation 'com.google.firebase:firebase-analytics-ktx'

  // TODO: Add the dependencies for any other Firebase products you want to use
  // See https://firebase.google.com/docs/android/setup#available-libraries
  // For example, add the dependencies for Firebase Authentication and Cloud Firestore
  implementation 'com.google.firebase:firebase-auth-ktx'
  implementation 'com.google.firebase:firebase-firestore-ktx'
}
```

添加了需要使用的产品的依赖项之后，将 Android 项目与 Gradle 文件同步。







### 集成fcm

#### 添加依赖

```
    api 'com.google.firebase:firebase-messaging-ktx'
    api 'com.google.firebase:firebase-analytics'
```



#### 修改您的应用清单

将以下内容添加至应用的清单中：

- 一项扩展 `FirebaseMessagingService` 的服务。除了接收通知外，如果您还希望在后台应用中进行消息处理，则必须添加此服务。例如，您需要在前台应用中接收通知、接收数据载荷以及发送上行消息等，就必须扩展此服务。

```
<service
    android:name=".java.MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

- （可选）应用组件中用于设置默认通知图标和颜色的元数据元素。如果传入的消息未明确设置图标和颜色，Android 就会使用这些值。

```
<!-- Set custom default icon. This is used when no icon is set for incoming notification messages.
     See README(https://goo.gl/l4GJaQ) for more. -->
<meta-data
    android:name="com.google.firebase.messaging.default_notification_icon"
    android:resource="@drawable/ic_stat_ic_notification" />
<!-- Set color used with incoming notification messages. This is used when no color is set for the incoming
     notification message. See README(https://goo.gl/6BKBk7) for more. -->
<meta-data
    android:name="com.google.firebase.messaging.default_notification_color"
    android:resource="@color/colorAccent" />
```

- （可选）从 Android 8.0（API 级别 26）和更高版本开始，我们支持并推荐使用[通知渠道](https://developer.android.com/guide/topics/ui/notifiers/notifications.html?hl=zh-cn#ManageChannels)。FCM 提供具有基本设置的默认通知渠道。如果您希望[创建](https://developer.android.com/guide/topics/ui/notifiers/notifications.html?hl=zh-cn#CreateChannel)和使用自己的默认渠道，请将 `default_notification_channel_id` 设置为您的通知渠道对象的 ID（如下所示）；只要传入的消息未明确设置通知渠道，FCM 就会使用此值。如需了解详情，请参阅[管理通知渠道](https://developer.android.com/guide/topics/ui/notifiers/notifications.html?hl=zh-cn#ManageChannels)。

```
<meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="@string/default_notification_channel_id" />
```



#### 在 Android 13 及更高版本上请求运行时通知权限

Android 13 中引入了用于显示通知的新运行时权限。该项引入会影响在 Android 13 或更高版本上使用 FCM 通知的所有应用。

默认情况下，FCM SDK（23.0.6 或更高版本）中包含清单中定义的 [`POST_NOTIFICATIONS`](https://developer.android.com/reference/android/Manifest.permission?hl=zh-cn#POST_NOTIFICATIONS) 权限。不过，您的应用还需要通过常量 `android.permission.POST_NOTIFICATIONS` 请求此权限的运行时版本。在用户授予此权限之前，您的应用将无法显示通知。

如需请求该项新运行时权限，请执行以下操作：

```
// Declare the launcher at the top of your Activity/Fragment:
private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) {
        // FCM SDK (and your app) can post notifications.
    } else {
        // TODO: Inform user that that your app will not show notifications.
    }
}

private fun askNotificationPermission() {
    // This is only necessary for API level >= 33 (TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
```

通常，您应向用户显示一个界面，说明如果用户授予应用发布通知的权限，会启用哪些功能。此界面应向用户提供同意或拒绝的选项，例如**确定**和**不用了**按钮。如果用户选择**确定**，则直接请求该权限。如果用户选择**不用了**，则允许用户在不接收通知的情况下继续操作。





#### 使用 Android 12L（API 级别 32）或更低版本的应用上的通知权限



当您的应用首次创建通知渠道时，只要应用处于前台，Android 便会自动请求用户授予该权限。不过，关于创建渠道和请求权限的时机，需要注意下面一些重要事项：

- 如果您的应用是在后台运行时创建的第一个通知渠道（FCM SDK 在收到 FCM 通知时便会在后台创建通知渠道），Android 不会允许该通知显示出来，并且直到用户下次打开应用时才会提示他们授予通知权限。这意味着，在用户打开应用并授予该权限之前收到的所有通知都将丢失。
- 我们强烈建议您将应用更新为使用 Android 13 及更高版本，以便能够利用平台的 API 来请求权限。如果您无法进行此更新，您的应用应该在您向其发送任何通知之前创建通知渠道，以便触发通知权限对话框，并确保不会丢失通知。



#### 可选：移除 `POST_NOTIFICATIONS` 权限

默认情况下，FCM SDK 包含 `POST_NOTIFICATIONS` 权限。如果您的应用不使用通知消息（无论是通过 FCM 通知、通过其他 SDK 还是由您的应用直接发布），并且您不想让应用包含该权限，则可以使用[清单合并](https://developer.android.com/studio/build/manage-manifests?hl=zh-cn)的 `remove` 标记移除该权限。请注意，移除此权限会阻止系统显示所有通知，而不仅仅是 FCM 通知。将以下内容添加到应用的清单文件中：

```
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" tools:node="remove"/>
```





