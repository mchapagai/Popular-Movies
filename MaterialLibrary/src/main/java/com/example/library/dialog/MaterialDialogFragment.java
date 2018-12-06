package com.example.library.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.library.R;
import com.example.library.views.MaterialButton;
import com.example.library.views.MaterialTextView;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class MaterialDialogFragment extends DialogFragment {

    public static final String DATA_MODEL = "dateModel";
    private OnDialogClickListener onDialogClickListener;

    public MaterialDialogFragment() {
    }

    private static MaterialDialogFragment buildAlertDialog(DialogBuilder dialogBuilder) {
        MaterialDialogFragment fragment = new MaterialDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_MODEL, dialogBuilder);
        fragment.setArguments(args);
        return fragment;
    }

    public static MaterialDialogFragment showDialog(DialogBuilder dialogBuilder, AppCompatActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        String tag = dialogBuilder.getTag();
        MaterialDialogFragment fragment = MaterialDialogFragment.buildAlertDialog(dialogBuilder);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    public static MaterialDialogFragment showDialog(DialogBuilder builder, Fragment fragment) {
        FragmentManager manager = fragment.getFragmentManager();
        String tag = builder.getTag();
        MaterialDialogFragment dialogFragment = MaterialDialogFragment.buildAlertDialog(builder);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(dialogFragment, tag);
        transaction.commitAllowingStateLoss();
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogBuilder builder = getArguments().getParcelable(DATA_MODEL);
        return apply(builder);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        String tag = getTag();
        if (onDialogClickListener != null) {
            onDialogClickListener.onCancelEvent(tag);
        }
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    private Dialog apply(final DialogBuilder builder) {
        View view = null;
        Dialog dialog;

        if (builder.isProgressDialog()) {
            dialog = new ProgressDialog(getActivity());
            ((ProgressDialog) dialog).setIndeterminate(true);
            ((ProgressDialog) dialog).setMessage(builder.getMessage());
            setCancelable(builder.isCancelable());
        } else if (builder.isCustomWindow()) {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            view = LayoutInflater.from(getActivity()).inflate(builder.getLayoutResId(), null);
            dialog.setContentView(view);

            Window oobPreviewWindow = dialog.getWindow();
            oobPreviewWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            oobPreviewWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            oobPreviewWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCustomDialog(builder, dialog, view, false);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            if (builder.getLayoutResId() != 0) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(builder.getLayoutResId(), null);
                alertDialog.setView(view);
                if (!builder.isCustomButton()) {
                    setButtons(builder, alertDialog);
                }
                if (view.findViewById(R.id.dialog_title) != null) {
                    MaterialTextView dialogTitle = view.findViewById(R.id.dialog_title);

                    if (StringUtils.isNotEmpty(builder.getTitle())) {
                        dialogTitle.setText(builder.getTitle());
                        dialogTitle.setVisibility(View.VISIBLE);
                    } else {
                        dialogTitle.setVisibility(View.GONE);
                    }
                }
            } else {
                if (builder.getMessage() != null) {
                    alertDialog.setMessage(builder.getMessage());
                }
                setButtons(builder, alertDialog);
                if (builder.getTitle() != null) {
                    alertDialog.setTitle(builder.getTitle());
                }
            }

            if (builder.getDrawableResId() != 0) {
                alertDialog.setIcon(builder.getDrawableResId());
            }

            setCancelable(builder.isCancelable());
            dialog = alertDialog.create();
            setCustomDialog(builder, dialog, view, true);
        }
        return dialog;
    }

    private void setButtons(final DialogBuilder builder, final AlertDialog.Builder dialogBuilder) {
        if (builder.getPositiveButtonText() != null) {
            dialogBuilder.setPositiveButton(builder.getPositiveButtonText(),
                    ((dialog, which) -> {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onPositiveButtonClicked(builder.getPositiveButtonData(), builder.getTag());
                        }
                    }));
            dialogBuilder.setNegativeButton(builder.getNegativeButtonText(),
                    ((dialog, which) -> {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onNegativeButtonClicked(builder.getTag());
                        }
                    }));

            dialogBuilder.setNeutralButton(builder.getNegativeButtonText(),
                    ((dialog, which) -> {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onNeutralButtonClicked(builder.getTag());
                        }
                    }));
        }
    }

    private void setCustomDialog(final DialogBuilder builder, final Dialog dialog, View containerView, boolean isUpperCase) {
        if (containerView == null) return;

        if (builder.getMessage() != null) {
            MaterialTextView message = containerView.findViewById(R.id.dialog_message);
            message.setVisibility(View.VISIBLE);
            message.setText(builder.getMessage());
        }

        if (builder.isCustomButton()) {
            if (builder.getPositiveButtonText() != null) {
                MaterialButton button = containerView.findViewById(R.id.positive_button);
                if (isUpperCase) {
                    button.setText(builder.getPositiveButtonText().toUpperCase());
                }
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(view -> {
                    if (onDialogClickListener != null) {
                        onDialogClickListener.onPositiveButtonClicked(builder.getPositiveButtonData(), builder.getTag());
                    }
                    dialog.dismiss();
                });
            }

            if (builder.getNegativeButtonText() != null) {
                MaterialButton button = containerView.findViewById(R.id.negative_button);
                if (isUpperCase) {
                    button.setText(builder.getNegativeButtonText().toUpperCase());
                }
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    if (onDialogClickListener != null) {
                        onDialogClickListener.onNegativeButtonClicked(builder.getTag());
                    }
                    dialog.dismiss();
                });
            }

            MaterialButton neutralButton = containerView.findViewById(R.id.neutral_button);
            if (builder.getNegativeButtonText() != null && neutralButton != null) {
                if (isUpperCase) {
                    neutralButton.setText(builder.getNeutralTextButton().toUpperCase());
                }
                neutralButton.setVisibility(View.VISIBLE);
                neutralButton.setOnClickListener(v -> {
                    if (onDialogClickListener != null) {
                        onDialogClickListener.onNeutralButtonClicked(builder.getTag());
                    }
                    dialog.dismiss();
                });
            }
        }
    }

    public interface OnDialogClickListener {
        void onPositiveButtonClicked(Serializable data, String tag);

        void onNegativeButtonClicked(String tag);

        void onNeutralButtonClicked(String tag);

        void onCancelEvent(String tag);
    }
}
