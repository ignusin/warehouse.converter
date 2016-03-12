package iglabs.warehouse.converter;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

class UI {
	public final static int CONTROLS_GAP = 5;
	
	public final static int FONT_SIZE_INCREASE = 2;
	
	public final static void applyButtonStyle(JButton button) {
		button.setMargin(new Insets(CONTROLS_GAP * 2, CONTROLS_GAP * 2, CONTROLS_GAP * 2, CONTROLS_GAP * 2));
		button.setFont(
			button.getFont().deriveFont(
				Font.PLAIN,
				button.getFont().getSize() + FONT_SIZE_INCREASE
			)
		);

	}
}
