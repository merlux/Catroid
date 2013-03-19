/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
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
package org.catrobat.catroid.test.formulaeditor;

import java.util.LinkedList;
import java.util.List;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.InternFormulaParser;
import org.catrobat.catroid.formulaeditor.InternToken;
import org.catrobat.catroid.formulaeditor.InternTokenType;
import org.catrobat.catroid.formulaeditor.Operators;

import android.test.AndroidTestCase;

public class ParserTest extends AndroidTestCase {

	private static final float LOOK_ALPHA = 0.5f;
	private static final float LOOK_Y_POSITION = 23.4f;
	private static final float LOOK_X_POSITION = 5.6f;
	private static final float LOOK_BRIGHTNESS = 0.7f;
	private static final float LOOK_SCALE = 90.3f;
	private static final float LOOK_ROTATION = 30.7f;
	private static final int LOOK_ZPOSITION = 3;
	private Sprite testSprite;

	@Override
	protected void setUp() {
		testSprite = new Sprite("sprite");
		testSprite.look.setXPosition(LOOK_X_POSITION);
		testSprite.look.setYPosition(LOOK_Y_POSITION);
		testSprite.look.setAlphaValue(LOOK_ALPHA);
		testSprite.look.setBrightnessValue(LOOK_BRIGHTNESS);
		testSprite.look.setScaleX(LOOK_SCALE);
		testSprite.look.setScaleY(LOOK_SCALE);
		testSprite.look.setRotation(LOOK_ROTATION);
		testSprite.look.setZIndex(LOOK_ZPOSITION);
	}

	public void testNumbers() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1.0"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1.0", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, ""));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNull("Formula is not parsed correctly: <empty number> {}", parseTreeRoot);
		assertEquals("Parser error value not as expected", 0, internParser.getErrorTokenIndex());
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "."));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNull("Formula is not parsed correctly: .", parseTreeRoot);
		assertEquals("Parser error value not as expected", 0, internParser.getErrorTokenIndex());
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, ".1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNull("Formula is not parsed correctly: .1", parseTreeRoot);
		assertEquals("Parser error value not as expected", 0, internParser.getErrorTokenIndex());
		internTokenList.clear();
	}

	public void testLogicalMathematicalOperatorsNesting() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.GREATER_THAN.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.POW.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 > 2 ^ 2", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

	}

	public void testLogicalOperators() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.GREATER_THAN.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 2 > 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.GREATER_THAN.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 > 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.GREATER_OR_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 >= 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.GREATER_OR_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 >= 2", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.SMALLER_THAN.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 < 2", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.SMALLER_THAN.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 < 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.SMALLER_OR_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 <= 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.SMALLER_OR_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 2 <= 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 = 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 2 = 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.NOT_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 2 != 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.NOT_EQUAL.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: 1 != 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_NOT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_AND.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: NOT 0 AND 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_NOT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_OR.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: NOT 1 OR 0", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_NOT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_OR.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: NOT 0 OR 0", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_NOT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.LOGICAL_AND.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "0"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: NOT 0 AND 0", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0d, parseTreeRoot.interpretRecursive(testSprite));
		internTokenList.clear();
	}

	public void testUnaryMinus() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "42.42"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly: - 42.42", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", -42.42, parseTreeRoot.interpretRecursive(testSprite));
	}

	public void testOperatorPriority() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MULT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  1 - 2 x 2", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", -3.0, parseTreeRoot.interpretRecursive(testSprite));

	}

	public void testOperatorLeftBinding() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "5"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "4"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  5 - 4 - 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0.0, parseTreeRoot.interpretRecursive(testSprite));

		internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "100"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.DIVIDE.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "10"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.DIVIDE.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "10"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  100 ÷ 10 ÷ 10", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 1.0, parseTreeRoot.interpretRecursive(testSprite));

	}

	public void testOperatorChain() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.PLUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MULT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "3"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.POW.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.PLUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  1 + 2 × 3 ^ 2 + 1", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 20.0, parseTreeRoot.interpretRecursive(testSprite));

		internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.PLUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.POW.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "3"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MULT.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  1 + 2 ^ 3 * 2 ", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 17.0, parseTreeRoot.interpretRecursive(testSprite));

	}

	public void testBracket() {

		List<InternToken> internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.BRACKET_OPEN));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.PLUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_CLOSE));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MULT.name()));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_OPEN));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.PLUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_CLOSE));

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  (1+2) x (1+2)", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 9.0, parseTreeRoot.interpretRecursive(testSprite));

		internTokenList = new LinkedList<InternToken>();

		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_OPEN));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.POW.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_CLOSE));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_OPEN));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "1"));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.OPERATOR, Operators.MINUS.name()));
		internTokenList.add(new InternToken(InternTokenType.NUMBER, "2"));
		internTokenList.add(new InternToken(InternTokenType.BRACKET_CLOSE));

		internParser = new InternFormulaParser(internTokenList);
		parseTreeRoot = internParser.parseFormula();

		assertNotNull("Formula is not parsed correctly:  -(1^2)--(-1--2)", parseTreeRoot);
		assertEquals("Formula interpretation is not as expected", 0.0, parseTreeRoot.interpretRecursive(testSprite));

	}

	public void testEmptyInput() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();

		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTreeRoot = internParser.parseFormula();

		assertNull("Formula is not parsed correctly: EMPTY FORMULA {}", parseTreeRoot);
		assertEquals("Formula error value not as expected", InternFormulaParser.PARSER_NO_INPUT,
				internParser.getErrorTokenIndex());
	}

}
