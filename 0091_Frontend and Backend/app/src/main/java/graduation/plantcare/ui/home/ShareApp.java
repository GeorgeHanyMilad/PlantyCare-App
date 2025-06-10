package graduation.plantcare.ui.home;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;

public class ShareApp extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.share_app);
    }

    public void backButton(View view) {
        finish();
    }

    public void whatsapp(View view) {
        shareText("whatsapp");
    }

    void shareText(String platform) {
        String text = "Check out this app: https://appetize.io/app/b_msyipkbcvk26wxqelljokb244q";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        switch (platform) {
            case "whatsapp":
                shareIntent.setPackage("com.whatsapp");
                break;
            case "facebook":
                shareIntent.setPackage("com.facebook.katana");
                break;
            case "copy":
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("App Link", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ShareApp.this,"Link Copied!" , Toast.LENGTH_SHORT).show();
                return;
            case "telegram":
                shareIntent.setPackage("org.telegram.messenger");
                break;
            case "gmail":
                String subject = "Check this app!";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
                return;
            case "messenger":
                shareIntent.setPackage("com.facebook.orca");
                break;
        }

        try {
            startActivity(shareIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void facebook(View view) {
        shareText("facebook");
    }

    public void copyLink(View view) {
        shareText("copy");
    }

    public void telegram(View view) {
        shareText("telegram");
    }

    public void gmail(View view) {
        shareText("gmail");
    }

    public void messenger(View view) {
        shareText("messenger");
    }
}
