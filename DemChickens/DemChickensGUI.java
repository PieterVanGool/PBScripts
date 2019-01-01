package scripts.demchickens;

import scripts.demchickens.tasks.KillChicken;
import scripts.demchickens.tasks.PickupLoot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DemChickensGUI extends JFrame {
    private boolean done = false;
    private boolean killChickens = true;
    private boolean pickupFeathers = true;
    private boolean buryBones = true;

    public DemChickensGUI() {
        setLayout(new GridBagLayout());
        setSize(250, 300);
        setTitle("Dem Chickens");
        JLabel label = new JLabel("Select options:");
        final JCheckBox pickupFeathersCB = new JCheckBox("Loot Feathers", true);
        final JCheckBox buryBonesCB = new JCheckBox("Loot & Bury Bones", true);
        SpinnerModel value = new SpinnerNumberModel(6, //initial value
                1, //minimum value
                28, //maximum value
                1); //step
        JSpinner spinner = new JSpinner(value);
        JLabel spinnerLbl = new JLabel("Max kills before loot");
        JButton button = new JButton("Start");
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        add(label, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        add(pickupFeathersCB, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        add(buryBonesCB, gc);
        gc.gridx = 0;
        gc.gridy = 4;
        add(spinnerLbl, gc);
        gc.gridx = 0;
        gc.gridy = 5;
        add(spinner, gc);
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 0;
        gc.gridy = 6;
        add(button, gc);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pickupFeathersCB.isSelected() && !buryBonesCB.isSelected()) {
                    System.out.print("Please select at least one task");
                    return;
                }
                if (pickupFeathersCB.isSelected() && buryBonesCB.isSelected()) {
                    PickupLoot.setActive(true, true);
                } else if (!pickupFeathersCB.isSelected() && buryBonesCB.isSelected()) {
                    PickupLoot.setActive(false, true);
                } else {
                    PickupLoot.setActive(true, false);
                }
                KillChicken.setMaxRandomKillB4Loot((int) spinner.getValue());
                System.out.println("max kill before loot is: " + spinner.getValue());
                dispose();
                done = true;
            }
        });
    }

    public boolean done() {
        return done;
    }
}
