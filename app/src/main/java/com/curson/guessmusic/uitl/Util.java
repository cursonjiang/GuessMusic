package com.curson.guessmusic.uitl;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.curson.guessmusic.R;
import com.curson.guessmusic.model.IAlertDialogButtonListener;

public class Util {

    private static AlertDialog mAlertDialog;

    /**
     * 显示自定义对话框
     *
     * @param context
     * @param message
     * @param listener
     */
    public static void showDialog(Context context, String message, final IAlertDialogButtonListener listener) {
        View dialogView;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_view, null);

        ImageButton btnOkView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);
        TextView txtMessageView = (TextView) dialogView.findViewById(R.id.text_dialog_message);

        txtMessageView.setText(message);

        //是
        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
                if (listener != null) {
                    listener.onClick();
                }
            }
        });

        //否
        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
            }
        });
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        //显示对话框
        mAlertDialog.show();
    }
}
