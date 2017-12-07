/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import keyo.utils.FileDrop;
import keyo.utils.Random;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mgalvao3
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        
        new FileDrop( System.out, jTabbedPane1, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   
                try{
                    String xls = files[0].getCanonicalPath();
                    InputStream inp = new FileInputStream(xls);
                    HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
                    
                    //String mes = wb.getSheetAt(0).getRow(0).getCell(0).toString();
                    //double primeiroDiaValue = wb.getSheetAt(0).getRow(2).getCell(0).getNumericCellValue();
                    //String primeiroDia = String.valueOf((int)primeiroDiaValue);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    //String retorno = (String) JOptionPane.showInputDialog(MainForm.this, "Primeiro dia deste cardápio (dd/MM/yyyy): ", "Data", JOptionPane.QUESTION_MESSAGE, null, null, suggestedDate(primeiroDia, mes) );
                    //System.out.println(retorno);
                    //String retorno = suggestedDate(primeiroDia, mes);
                    //Date dataInicial = sdf.parse(retorno);
                    Date dataInicial = wb.getSheetAt(0).getRow(2).getCell(0).getDateCellValue();
                    
                    //Caseira
                    Sheet sheetCaseira = wb.getSheetAt(0);
                    DefaultTableModel model1 = (DefaultTableModel) jTableCaseira.getModel();
                    Date dataAtual = dataInicial;                  
                    for(int i = 2; i < sheetCaseira.getPhysicalNumberOfRows(); i++) {
                        Row row = sheetCaseira.getRow(i);
                        
                        if(row.getCell(1) != null && row.getCell(1).toString().length() > 0) {
                            model1.addRow(
                                    new Object[]{
                                    sdf.format(dataAtual), 
                                    row.getCell(6).toString().replaceAll("\\s+"," ") + "\n" + row.getCell(7).toString().replaceAll("\\s+"," "),
                                    row.getCell(8).toString().replaceAll("\\s+"," "),
                                    row.getCell(5).toString().replaceAll("\\s+"," "),
                                    row.getCell(9).toString().replaceAll("\\s+"," ")});
                        }
                        
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dataAtual);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        dataAtual = cal.getTime();
                        
                    }
                    
                    //Bem-Estar
                    Sheet sheetBemEstar = wb.getSheetAt(1);
                    DefaultTableModel model2 = (DefaultTableModel) jTableBemEstar.getModel();
                    dataAtual = dataInicial;                  
                    for(int i = 2; i < sheetBemEstar.getPhysicalNumberOfRows(); i++) {
                        Row row = sheetBemEstar.getRow(i);
                        
                        if(row.getCell(1) != null && row.getCell(1).toString().length() > 0) {
                            model2.addRow(
                                    new Object[]{
                                    sdf.format(dataAtual), 
                                    row.getCell(6).toString().replaceAll("\\s+"," ") + "\n" + row.getCell(7).toString().replaceAll("\\s+"," "),
                                    row.getCell(5).toString().replaceAll("\\s+"," "),
                                    row.getCell(8).toString().replaceAll("\\s+"," "),
                                    row.getCell(9).toString().replaceAll("\\s+"," ")});
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dataAtual);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        dataAtual = cal.getTime();
                    }
                    
                    //Sua Escolha
                    Sheet sheetSuaEscolha = wb.getSheetAt(2);
                    DefaultTableModel model3 = (DefaultTableModel) jTableSuaEscolha.getModel();
                    dataAtual = dataInicial;                  
                    for(int i = 2; i < sheetSuaEscolha.getPhysicalNumberOfRows(); i++) {
                        Row row = sheetSuaEscolha.getRow(i);
                        
                        if(!row.getCell(1).toString().equals("")) {
                            
                            if(row.getCell(1) != null && row.getCell(1).toString().length() > 0) {
                                model3.addRow(
                                        new Object[]{ 
                                        sdf.format(dataAtual), 
                                        row.getCell(21).toString().replaceAll("\\s+"," ") + " / " + row.getCell(22).toString().replaceAll("\\s+"," ") + " / " + row.getCell(23).toString().replaceAll("\\s+"," "),
                                        row.getCell(14).toString().replaceAll("\\s+"," ") + " / " + row.getCell(15).toString().replaceAll("\\s+"," ") + " / Quiche de " + row.getCell(17).toString().replaceAll("\\s+"," "),
                                        row.getCell(24).toString().replaceAll("\\s+"," ")});
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dataAtual);
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                            dataAtual = cal.getTime();
                        }
                    }
                }
                catch(Exception ex){
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex);
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    JOptionPane.showMessageDialog(MainForm.this, ex.getMessage() + "\n\n" + sw, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }   // end filesDropped

            private String suggestedDate(String primeiroDia, String mes) {
                String mesInt = "00";
                
                switch(mes.toLowerCase()) {
                    case "janeiro":
                        mesInt = "01"; break; 
                    case "fevereiro":
                        mesInt = "02"; break; 
                    case "março":
                        mesInt = "03"; break; 
                    case "abril":
                        mesInt = "04"; break; 
                    case "maio":
                        mesInt = "05"; break; 
                    case "junho":
                        mesInt = "06"; break; 
                    case "julho":
                        mesInt = "07"; break; 
                    case "agosto":
                        mesInt = "08"; break;  
                    case "setembro":
                        mesInt = "09"; break; 
                    case "outubro":
                        mesInt = "10"; break; 
                    case "novembro":
                        mesInt = "11"; break; 
                    case "dezembro":
                        mesInt = "12"; break; 
                }
                
                Calendar cal = Calendar.getInstance();
                String ano = Integer.toString(cal.get(Calendar.YEAR));
                
                return String.format("%02d", Integer.parseInt(primeiroDia)) + "/" + mesInt + "/" + ano;
            }
        }); // end FileDrop.Listener
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCaseira = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBemEstar = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableSuaEscolha = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("get JSON");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTableCaseira.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Pratos Principais", "Guarnições", "Sopa", "Sobremesas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableCaseira);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Caseiro", jPanel1);

        jTableBemEstar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Pratos Principais", "Guarnições", "Sopa", "Sobremesas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableBemEstar);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Bem-Estar", jPanel2);

        jTableSuaEscolha.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Grelhados", "Guarnições", "Sobremesas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableSuaEscolha);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Sua Escolha", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        try {
        JSONObject days = new JSONObject();
        
        
        
        for(int i =0; i< tableWithMostRows(); i++) {
            
            
                JSONObject day = new JSONObject();
                
                if(i < jTableBemEstar.getModel().getRowCount()) {
                    JSONArray bemEstarArray = new JSONArray();
                    for(int j=1; j< jTableBemEstar.getModel().getColumnCount(); j++) {
                        JSONObject o = new JSONObject();
                        o.put("name", jTableBemEstar.getModel().getColumnName(j));
                        o.put("description", jTableBemEstar.getModel().getValueAt(i, j));
                        o.put("likes", 0);
                        o.put("dislikes", 0);
                        o.put("id", Random.randId());
                        bemEstarArray.add(o);
                    }
                    day.put("BEM-ESTAR", bemEstarArray);
                }
                
                if(i < jTableCaseira.getModel().getRowCount()) {
                    JSONArray caseiraArray = new JSONArray();
                    for(int j=1; j< jTableCaseira.getModel().getColumnCount(); j++) {

                            JSONObject o = new JSONObject();
                            o.put("name", jTableCaseira.getModel().getColumnName(j));
                            o.put("description", jTableCaseira.getModel().getValueAt(i, j));
                            o.put("likes", 0);
                            o.put("dislikes", 0);
                            o.put("id", Random.randId());
                            caseiraArray.add(o);

                    }
                    day.put("CASEIRA", caseiraArray);
                }
                
                if(i < jTableSuaEscolha.getModel().getRowCount()) {
                    JSONArray escolhaArray = new JSONArray();
                    for(int j=1; j< jTableSuaEscolha.getModel().getColumnCount(); j++) {                    
                        JSONObject o = new JSONObject();
                        o.put("name", jTableSuaEscolha.getModel().getColumnName(j));
                        o.put("description", jTableSuaEscolha.getModel().getValueAt(i, j));
                        o.put("likes", 0);
                        o.put("dislikes", 0);
                        o.put("id", Random.randId());
                        escolhaArray.add(o);
                    }   
                    day.put("SUA ESCOLHA", escolhaArray);
                }
                
                String dayString = convertDateJson(jTableBemEstar.getModel().getValueAt(i, 0).toString());
                days.put(dayString, day);
            
        }
        
        
        
        JSONObject root = new JSONObject();
        root.put("items", days);
        
        String toJSONString = root.toJSONString();
        System.out.println(toJSONString);
        
        } catch (ParseException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        
       
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableBemEstar;
    private javax.swing.JTable jTableCaseira;
    private javax.swing.JTable jTableSuaEscolha;
    // End of variables declaration//GEN-END:variables

    private String convertDateJson(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateString);
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf2.format(date);
    }

    private int tableWithMostRows() {
        int rows1 = jTableBemEstar.getModel().getRowCount();
        int rows2 = jTableCaseira.getModel().getRowCount();
        int rows3 = jTableSuaEscolha.getModel().getRowCount();
        
        return Math.max(rows1, Math.max(rows2, rows3));
    }
}
