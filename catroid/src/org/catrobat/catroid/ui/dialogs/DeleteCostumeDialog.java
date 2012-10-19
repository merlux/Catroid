/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2012 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.ui.dialogs;

import java.util.ArrayList;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.livewallpaper.R;
import org.catrobat.catroid.ui.ScriptTabActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteCostumeDialog extends DialogFragment {

	private static final String BUNDLE_ARGUMENTS_SELECTED_POSITION = "selected_position";
	public static final String DIALOG_FRAGMENT_TAG = "dialog_delete_costume";

	public static DeleteCostumeDialog newInstance(int selectedPosition) {
		DeleteCostumeDialog dialog = new DeleteCostumeDialog();

		Bundle arguments = new Bundle();
		arguments.putInt(BUNDLE_ARGUMENTS_SELECTED_POSITION, selectedPosition);
		dialog.setArguments(arguments);

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int selectedPosition = getArguments().getInt(BUNDLE_ARGUMENTS_SELECTED_POSITION);

		Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_costume_dialog)
				.setNegativeButton(R.string.cancel_button, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				}).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handleDeleteCostume(selectedPosition);
					}
				}).create();

		dialog.setCanceledOnTouchOutside(true);

		return dialog;
	}

	private void handleDeleteCostume(int position) {
		ArrayList<CostumeData> costumeDataList = ProjectManager.getInstance().getCurrentSprite().getCostumeDataList();
		StorageHandler.getInstance().deleteFile(costumeDataList.get(position).getAbsolutePath());
		costumeDataList.remove(position);

		getActivity().sendBroadcast(new Intent(ScriptTabActivity.ACTION_COSTUME_DELETED));
	}
}
