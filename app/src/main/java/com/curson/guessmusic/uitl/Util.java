package com.curson.guessmusic.uitl;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.curson.guessmusic.R;
import com.curson.guessmusic.data.Constants;
import com.curson.guessmusic.model.IAlertDialogButtonListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    private static AlertDialog mAlertDialog;

    /**
     * 显示自定义对话框
     *
     * @param context
     * @param message
     * @param listener
     */
    public static void showDialog(final Context context, String message, final IAlertDialogButtonListener listener) {
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

                //播放音效
                MyPlayer.playTone(context, Constants.INDEX_STONE_ENTER);
            }
        });

        //否
        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
                MyPlayer.playTone(context, Constants.INDEX_STONE_CANCEL);
            }
        });
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        //显示对话框
        mAlertDialog.show();
    }

    /**
     * 保存游戏数据
     *
     * @param context
     * @param stageIndex
     * @param coins
     */
    public static void saveData(Context context, int stageIndex, int coins) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constants.FILE_NAME_SAVE_DATA, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(stageIndex);
            dos.writeInt(coins);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取数据
     *
     * @param context
     * @return
     */
    public static int[] readData(Context context) {
        FileInputStream fis = null;
        int[] datas = {-1, Constants.TOTAL_COINS};
        try {
            fis = context.openFileInput(Constants.FILE_NAME_SAVE_DATA);
            DataInputStream dis = new DataInputStream(fis);
            datas[Constants.INDEX_LOAD_DATA_STAGE] = dis.readInt();
            datas[Constants.INDEX_LOAD_DATA_COINS] = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
