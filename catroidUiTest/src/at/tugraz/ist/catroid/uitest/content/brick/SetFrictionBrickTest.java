package at.tugraz.ist.catroid.uitest.content.brick;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.EditText;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Script;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.StartScript;
import at.tugraz.ist.catroid.content.bricks.Brick;
import at.tugraz.ist.catroid.content.bricks.SetFrictionBrick;
import at.tugraz.ist.catroid.ui.ScriptActivity;
import at.tugraz.ist.catroid.uitest.util.UiTestUtils;

import com.jayway.android.robotium.solo.Solo;

/*
 * TODO: Test doesn' work correctly.
 */
public class SetFrictionBrickTest extends ActivityInstrumentationTestCase2<ScriptActivity> {
	private Solo solo;
	private Project project;
	private SetFrictionBrick setFrictionBrick;

	public SetFrictionBrickTest() {
		super("at.tugraz.ist.catroid", ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		createProject();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		getActivity().finish();
		super.tearDown();
	}

	@Smoke
	public void testSetFrictionBrick() {
		int childrenCount = getActivity().getAdapter().getChildCountFromLastGroup();
		int groupCount = getActivity().getAdapter().getGroupCount();

		assertEquals("Incorrect number of bricks.", 2, solo.getCurrentListViews().get(0).getChildCount());
		assertEquals("Incorrect number of bricks.", 1, childrenCount);

		ArrayList<Brick> projectBrickList = project.getSpriteList().get(0).getScript(0).getBrickList();
		assertEquals("Incorrect number of bricks.", 1, projectBrickList.size());

		assertEquals("Wrong Brick instance.", projectBrickList.get(0),
				getActivity().getAdapter().getChild(groupCount - 1, 0));
		String frictionByString = solo.getString(R.string.brick_set_friction);
		solo.waitForText(frictionByString);
		assertNotNull("TextView does not exist", solo.getText(frictionByString));

		float friction = 1.234f;

		solo.waitForView(EditText.class);
		UiTestUtils.clickEnterClose(solo, 0, Float.toString(friction));
		float actualMass = (Float) UiTestUtils.getPrivateField("friction", setFrictionBrick);
		assertEquals("Text not updated", Float.toString(friction), solo.getEditText(0).getText().toString());
		assertEquals("Value in Brick is not updated", friction, actualMass);
	}

	@Smoke
	public void testResizeInputField() {
		UiTestUtils.testDoubleEditText(solo, 0, 12345.0, 50, false);
		UiTestUtils.testDoubleEditText(solo, 0, 1.0, 50, true);
		UiTestUtils.testDoubleEditText(solo, 0, 1234.0, 50, true);
		UiTestUtils.testDoubleEditText(solo, 0, -1, 50, true);
	}

	private void createProject() {
		project = new Project(null, "testProject");
		Sprite sprite = new Sprite("cat");
		Script script = new StartScript(sprite);
		setFrictionBrick = new SetFrictionBrick(null, sprite, 0.0f);
		script.addBrick(setFrictionBrick);

		sprite.addScript(script);
		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}

}