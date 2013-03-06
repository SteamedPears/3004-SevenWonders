package com.steamedpears.comp3004;
import com.steamedpears.comp3004.views.*;

import static org.mockito.Mockito.*;
import org.apache.log4j.*;

import javax.swing.*;

public class ViewFrameTest {
    private ViewFrame view;

    public ViewFrameTest() {
        // create the window
        view = new ViewFrame();

        // add a test panel
        JPanel labelPanel = new JPanel();
        labelPanel.add(new JLabel("This test passes"));
        view.setView(labelPanel);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new ViewFrameTest();
    }
}
