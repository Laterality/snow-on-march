package latera.kr.snowonmarch.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import latera.kr.snowonmarch.R;

public class AddGroupDialog extends DialogFragment {
	private static final String TAG = "ADD_GROUP_DIALOG";

	private int[] colors;
	private int currentColor;

	private EditText etGroupName;
	private RadioGroup rgBgColors;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		colors = new int[6];
		if (Build.VERSION.SDK_INT >= 23) {
			colors[0] = getActivity().getColor(R.color.color_bg_1);
			colors[1] = getActivity().getColor(R.color.color_bg_2);
			colors[2] = getActivity().getColor(R.color.color_bg_3);
			colors[3] = getActivity().getColor(R.color.color_bg_4);
			colors[4] = getActivity().getColor(R.color.color_bg_5);
			colors[5] = getActivity().getColor(R.color.color_bg_6);
		}
		else {
			colors[0] = getActivity().getResources().getColor(R.color.color_bg_1);
			colors[1] = getActivity().getResources().getColor(R.color.color_bg_2);
			colors[2] = getActivity().getResources().getColor(R.color.color_bg_3);
			colors[3] = getActivity().getResources().getColor(R.color.color_bg_4);
			colors[4] = getActivity().getResources().getColor(R.color.color_bg_5);
			colors[5] = getActivity().getResources().getColor(R.color.color_bg_6);
		}
		currentColor = colors[0];

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_group, null);
		etGroupName = v.findViewById(R.id.et_add_group_name);
		rgBgColors = v.findViewById(R.id.rg_dialog_add_group_colors);

		rgBgColors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch(i) {
					case R.id.rb_dialog_add_group_bg_color_1:
						currentColor = colors[0];
						break;
					case R.id.rb_dialog_add_group_bg_color_2:
						currentColor = colors[1];
						break;
					case R.id.rb_dialog_add_group_bg_color_3:
						currentColor = colors[2];
						break;
					case R.id.rb_dialog_add_group_bg_color_4:
						currentColor = colors[3];
						break;
					case R.id.rb_dialog_add_group_bg_color_5:
						currentColor = colors[4];
						break;
					case R.id.rb_dialog_add_group_bg_color_6:
						currentColor = colors[5];
						break;
				}
				Log.d(TAG, "current color changed to: " + currentColor);
			}
		});

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
									currentColor);

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
		public void onOk(String name, int color);
	}
}
