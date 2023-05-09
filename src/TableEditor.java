import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

public class TableEditor extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton importButton;
    private JButton exportButton;

    private Pattern[] patterns;
    private boolean valid =false;

    public TableEditor() {
        setTitle("Integracja systemów - Jakub Suchanowski");
        setSize(1000, 400);

        // Tworzenie tabeli
        String[] columnNames = {"Nazwa producenta", "Przekątna ekranu", "Rozdzielczość", "Rodzaj ekranu",
                "Czy dotykowy", "Nazwa procesora", "Liczba rdzeni", "Taktowanie MHz", "Pamięć RAM", "Pojemność dysku",
                "Rodzaj dysku", "Nazwa układu graficznego", "Pamięć układu graficznego", "Nazwa SO", "Rodzaj napędu"};
        model = new DefaultTableModel(columnNames, 0);

        table = new JTable(model);

        patterns = new Pattern[]{
                Pattern.compile("([\\w\\s|BŁĄD!!!]+)?"), // nazwa producenta
                Pattern.compile("(\\d+\"|BŁĄD!!!)?"), // przekątna ekranu
                Pattern.compile("(\\d+x\\d+|BŁĄD!!!)?"), // rozdzielczosc
                Pattern.compile("((matowa|blyszczaca)|BŁĄD!!!?)"), // rodzaj ekranu
                Pattern.compile("((tak|nie)|BŁĄD!!!?)"), // czy dotykowy
                Pattern.compile("([\\w\\s]+|BŁĄD!!!)?"), // nazwa procesora
                Pattern.compile("(\\d+|BŁĄD!!!)?"), // liczba rdzeni
                Pattern.compile("(\\d+\\.?\\d*|BŁĄD!!!)?"), // taktowanie mhz
                Pattern.compile("(\\d+GB|BŁĄD!!!)?"), // RAM
                Pattern.compile("(\\d+GB|\\d+TB|BŁĄD!!!)?"), // pojemnosc dysku
                Pattern.compile("((HDD|SSD|BŁĄD!!!))?"), // typ dysku
                Pattern.compile("([\\w\\s]+|BŁĄD!!!)?"), // karta graficzna
                Pattern.compile("(\\d+GB|BŁĄD!!!)?"), // pamiec karty graficznej
                Pattern.compile("((Windows|Linux|MacOS)|BŁĄD!!!)?"), // system operacyjny
                Pattern.compile("((DVD|Blu-ray|brak)|BŁĄD!!!)?") // naped
        };

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && patterns[e.getColumn()] != null) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    String data = model.getValueAt(row, column).toString();
                    if (!Pattern.matches(String.valueOf(patterns[column]), data)) {
                        JOptionPane.showMessageDialog(null, "Błędne dane w kolumnie: " + columnNames[column], "Error", JOptionPane.ERROR_MESSAGE);
                        model.setValueAt("BŁĄD!!!", row, column);
                    }
                }
            }
        });

        importButton = new JButton("Wczytaj dane");
        importButton.addActionListener(new ImportButtonListener());
        exportButton = new JButton("Zapisz dane");
        exportButton.addActionListener(new ExportButtonListener());

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

    }

    private class ImportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(";");
                        model.addRow(data);
                    }
                    br.close();
                    JOptionPane.showMessageDialog(null, "Dane zaimportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ExportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            if(model.getValueAt(i,j)=="BŁĄD!!!"){
                                JOptionPane.showMessageDialog(null, "Sprawdź dane! ", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            else {
                                bw.write(model.getValueAt(i, j) + ";");
                            }
                        }
                        bw.newLine();
                    }
                    bw.close();
                    JOptionPane.showMessageDialog(null, "Dane wyeksportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            }
    }

    public static void main(String[] args) {
        TableEditor editor = new TableEditor();
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editor.setVisible(true);
    }
}