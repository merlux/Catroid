/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.content.bricks;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.ui.ScriptTabActivity;
import at.tugraz.ist.catroid.ui.dialogs.BrickTextDialog;

public class LedOnBrick implements Brick, OnClickListener {
	private static final long serialVersionUID = 1L;
	private int insensitivitätInPercent;
	private Sprite sprite;

	private transient View view;

	public LedOnBrick(Sprite sprite, int insensitivitätInPercent) {
		this.insensitivitätInPercent = insensitivitätInPercent;
		this.sprite = sprite;
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public void execute() {
		/*
		 * long startTime = System.currentTimeMillis();
		 * int timeToWait = insensitivitätInPercent;
		 * while (System.currentTimeMillis() <= (startTime + timeToWait)) {
		 * if (!sprite.isAlive(Thread.currentThread())) {
		 * break;
		 * }
		 * if (sprite.isPaused) {
		 * timeToWait = timeToWait - (int) (System.currentTimeMillis() - startTime);
		 * while (sprite.isPaused) {
		 * if (sprite.isFinished) {
		 * return;
		 * }
		 * Thread.yield();
		 * }
		 * startTime = System.currentTimeMillis();
		 * }
		 * Thread.yield();
		 * }
		 */
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter adapter) {
		view = View.inflate(context, R.layout.brick_led_on, null);

		TextView text = (TextView) view.findViewById(R.id.brick_led_on_text_view);
		EditText edit = (EditText) view.findViewById(R.id.brick_led_on_edit_text);
		edit.setText((insensitivitätInPercent) + "");

		text.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);
		edit.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		return View.inflate(context, R.layout.brick_led_on, null);
	}

	@Override
	public Brick clone() {
		return new LedOnBrick(getSprite(), insensitivitätInPercent);
	}

	@Override
	public void onClick(View view) {
		ScriptTabActivity activity = (ScriptTabActivity) view.getContext();

		BrickTextDialog editDialog = new BrickTextDialog() {
			@Override
			protected void initialize() {
				input.setText(String.valueOf(insensitivitätInPercent));
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setSelectAllOnFocus(true);
			}

			@Override
			protected boolean handleOkButton() {
				try {
					insensitivitätInPercent = (int) (Double.parseDouble(input.getText().toString()) * 100);
				} catch (NumberFormatException exception) {
					Toast.makeText(getActivity(), R.string.error_no_number_entered, Toast.LENGTH_SHORT).show();
				}

				return true;
			}
		};

		editDialog.show(activity.getSupportFragmentManager(), "dialog_led_on_brick");
	}
}
