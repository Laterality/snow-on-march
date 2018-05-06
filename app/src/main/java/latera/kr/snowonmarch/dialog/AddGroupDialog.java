package latera.kr.snowonmarch.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import latera.kr.snowonmarch.R;

public class AddGroupDialog extends DialogFragment {
	private static final String TAG = "ADD_GROUP_DIALOG";

	private EditText etGroupName;
	private EditText etBgColor;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_group, null);
		etGroupName = v.findViewById(R.id.et_add_group_name);
		etBgColor = v.findViewById(R.id.et_add_group_bg_color);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(v)
				.setCancelable(true)
				.setTitle(R.string.text_add_group)
				.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						try {
							AddGroupDialogListener listener = (AddGroupDialogListener) getActivity();
							listener.onOk(etGroupName.getEditableText().toString(),
									etBgColor.getEditableText().toString());

						}
						catch (ClassCastException e) {
							Log.d(TAG, getActivity().toString() +
									" must implement AddGroupDialogListener");
						}
					}
				})
				.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});

		return builder.create();
	}

	public interface AddGroupDialogListener {
		public void onOk(String name, String color);
	}
}
