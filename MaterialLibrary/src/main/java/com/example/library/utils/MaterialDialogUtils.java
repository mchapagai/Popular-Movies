package com.example.library.utils;

import android.support.v7.app.AppCompatActivity;

import com.example.library.R;
import com.example.library.dialog.MovieDialogBuilder;
import com.example.library.dialog.MaterialDialogFragment;

import javax.annotation.Nonnull;

public class MaterialDialogUtils {

    public static MaterialDialogFragment showDialog(@Nonnull AppCompatActivity context, int title, int message, int positiveButtonMsg) {
        MovieDialogBuilder builder = new MovieDialogBuilder()
                .setTitle(context.getResources().getString(title))
                .setMessage(context.getResources().getString(message))
                .setCancelable(true)
                .setPositiveButtonText(context.getResources().getString(positiveButtonMsg))
                .setLayoutResId(R.layout.custom_confirmation_dialog)
                .setCustomButton(true);

        return MaterialDialogFragment.showDialog(builder, context);
    }

    public static MaterialDialogFragment showDialog(@Nonnull AppCompatActivity context, String title, String message, String positiveButtonMsg) {
        MovieDialogBuilder builder = new MovieDialogBuilder()
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButtonText(positiveButtonMsg)
                .setLayoutResId(R.layout.custom_confirmation_dialog)
                .setCustomButton(true);

        return MaterialDialogFragment.showDialog(builder, context);
    }
}
