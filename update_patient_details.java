package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class update_patient_details extends JFrame {

    update_patient_details(){

        JPanel panel = new JPanel();
        panel.setBounds(5,5,940,490);
        panel.setBackground(new Color(90, 156, 163));
        panel.setLayout(null);
        add(panel);

        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/updated.png"));
        Image image = imageIcon.getImage().getScaledInstance(300,300,Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(500,60,300,300);
        panel.add(label);

        JLabel label1 = new JLabel("Update Patient Details");
        label1.setBounds(124,11,260,25);
        label1.setFont(new Font("Tahoma",Font.BOLD,20));
        label1.setForeground(Color.white);
        panel.add(label1);


        JLabel label2 = new JLabel("Name :");
        label2.setBounds(25,88,100,14);
        label2.setFont(new Font("Tahoma",Font.PLAIN,14));
        label2.setForeground(Color.white);
        panel.add(label2);

        Choice choice = new Choice();
        choice.setBounds(248,85,140,25);
        panel.add(choice);

        try (conn c= new conn()) {
            try (PreparedStatement ps = c.getConnection().prepareStatement("select Name from patient_info order by Name")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()){
                        choice.add(resultSet.getString("Name"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        JLabel label3 = new JLabel("Room Number :");
        label3.setBounds(25,129,100,14);
        label3.setFont(new Font("Tahoma",Font.PLAIN,14));
        label3.setForeground(Color.white);
        panel.add(label3);

        JTextField textFieldR = new JTextField();
        textFieldR.setBounds(248,129,140,20);
        panel.add(textFieldR);

        JLabel label4 = new JLabel("In-Time  :");
        label4.setBounds(25,174,100,14);
        label4.setFont(new Font("Tahoma",Font.PLAIN,14));
        label4.setForeground(Color.white);
        panel.add(label4);

        JTextField textFieldINTIme = new JTextField();
        textFieldINTIme.setBounds(248,174,140,20);
        panel.add(textFieldINTIme);

        JLabel label5 = new JLabel("Amount Paid (Rs) :");
        label5.setBounds(25,216,150,14);
        label5.setFont(new Font("Tahoma",Font.PLAIN,14));
        label5.setForeground(Color.white);
        panel.add(label5);

        JTextField textFieldAmount = new JTextField();
        textFieldAmount.setBounds(248,216,140,20);
        panel.add(textFieldAmount);

        JLabel label6 = new JLabel("Pending Amount (Rs) :");
        label6.setBounds(25,261,150,14);
        label6.setFont(new Font("Tahoma",Font.PLAIN,14));
        label6.setForeground(Color.white);
        panel.add(label6);

        JTextField textFieldPending = new JTextField();
        textFieldPending.setBounds(248,261,140,20);
        panel.add(textFieldPending);

        JButton check = new JButton("CHECK");
        check.setBounds(281,378,89,23);
        check.setBackground(Color.black);
        check.setForeground(Color.white);
        panel.add(check);
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = choice.getSelectedItem();
                String q = "select Room_Number, Time, Deposit from patient_info where Name = ? limit 1";
                try (conn c = new conn()){
                    try (PreparedStatement ps = c.getConnection().prepareStatement(q)){
                        ps.setString(1, id);
                        try (ResultSet resultSet = ps.executeQuery()){
                            if (resultSet.next()){
                                textFieldR.setText(resultSet.getString("Room_Number"));
                                textFieldINTIme.setText(resultSet.getString("Time"));
                                textFieldAmount.setText(resultSet.getString("Deposit"));
                            } else {
                                JOptionPane.showMessageDialog(null, "No patient found for: " + id);
                                return;
                            }
                        }
                    }

                    try (PreparedStatement ps2 = c.getConnection().prepareStatement("select Price from Room where room_no = ?")) {
                        ps2.setString(1, textFieldR.getText());
                        try (ResultSet resultSet1 = ps2.executeQuery()){
                            if (resultSet1.next()){
                                String price = resultSet1.getString("Price");
                                int pending;
                                try {
                                    int priceVal = Integer.parseInt(price);
                                    int paidVal = Integer.parseInt(textFieldAmount.getText());
                                    pending = Math.max(0, priceVal - paidVal);
                                } catch (NumberFormatException nfe) {
                                    pending = 0;
                                }
                                textFieldPending.setText(""+pending);
                            }
                        }
                    }

                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });

        JButton update = new JButton("UPDATE");
        update.setBounds(56,378,89,23);
        update.setBackground(Color.black);
        update.setForeground(Color.white);
        panel.add(update);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String room = textFieldR.getText().trim();
                String time = textFieldINTIme.getText().trim();
                String amount = textFieldAmount.getText().trim();
                if (room.isEmpty() || time.isEmpty() || amount.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill room, time and amount.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try { Integer.parseInt(amount); } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Amount must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try (conn c = new conn()) {
                    String q = choice.getSelectedItem();
                    String updateSql = "update patient_info set Room_Number = ?, Time = ?, Deposit = ? where Name = ?";
                    try (PreparedStatement ps = c.getConnection().prepareStatement(updateSql)){
                        ps.setString(1, room);
                        ps.setString(2, time);
                        ps.setString(3, amount);
                        ps.setString(4, q);
                        ps.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null,"Updated Successfully");
                    setVisible(false);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });

        JButton back = new JButton("BACK");
        back.setBounds(168,378,89,23);
        back.setBackground(Color.black);
        back.setForeground(Color.white);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        setUndecorated(true);
        setSize(950,500);
        setLayout(null);
        setLocation(400,250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new update_patient_details();
        });
    }
}

