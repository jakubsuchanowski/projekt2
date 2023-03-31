import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableEditor extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton importButton;
    private JButton exportButton;

    public TableEditor() {
        setTitle("Integracja systemów - Jakub Suchanowski");
        setSize(1000, 400);

        // Tworzenie tabeli
        String[] columnNames = {"Nazwa producenta", "Przekątna ekranu", "Rozdzielczość", "Rodzaj ekranu",
                "Czy dotykowy", "Nazwa procesora", "Liczba rdzeni", "Taktowanie MHz", "Pamięć RAM", "Pojemność dysku",
                "Rodzaj dysku", "Nazwa układu graficznego", "Pamięć układu graficznego", "Nazwa SO", "Rodzaj napędu"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // Dodawanie przycisków
        importButton = new JButton("Wczytaj dane");
        importButton.addActionListener(new ImportButtonListener());
        exportButton = new JButton("Zapisz dane");
        exportButton.addActionListener(new ExportButtonListener());

        // Dodawanie tabeli i przycisków do okna
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        table.setDefaultRenderer(Object.class, new CustomCellRendered());
    }

    // Klasa wewnętrzna obsługująca przycisk "Importuj z pliku"
    private class ImportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("C:\\Users\\kubas\\Documents\\katalog.txt");
                    // Odczytanie danych z pliku i dodanie ich do tabeli
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(";");
                        model.addRow(data);
                    }
                    br.close();
                    JOptionPane.showMessageDialog(null,"Dane zaimportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }
    // Klasa wewnętrzna obsługująca przycisk "Eksportuj do pliku"
    private class ExportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("C:\\Users\\kubas\\Documents\\katalog2.txt");
                    // Zapisanie danych z tabeli do pliku
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for(int i=0;i<model.getRowCount();i++){
                        for(int j=0;j<model.getColumnCount();j++){
                            bw.write(model.getValueAt(i,j)+";");
                        }
                        bw.newLine();
                    }
                    bw.close();
                    JOptionPane.showMessageDialog(null,"Dane wyeksportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
    private class CustomCellRendered extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Number) {
                c.setBackground(Color.GREEN);
            }
            else if (value instanceof String) {
                c.setBackground(Color.YELLOW);
            } else {
                c.setBackground(table.getBackground());
            }
            return c;
        }
    }

    public static void main(String[] args) {
        TableEditor editor = new TableEditor();
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editor.setVisible(true);
    }
}