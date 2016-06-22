package battleships.client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class MyButton extends JButton {

	private Menu menu;
	
    public MyButton(Menu menu) {

        super();
        this.menu=menu;
        initUI();
    }

    public MyButton(Image image, Menu menu) {

        super(new ImageIcon(image));
        this.menu=menu;
        initUI();
    }

    public MyButton(String string, Menu menu) {
		super(string);
		this.menu=menu;
        initUI();
	}

	private void initUI() {

        BorderFactory.createLineBorder(Color.gray);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.blue));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }
            
        });
    }

}

