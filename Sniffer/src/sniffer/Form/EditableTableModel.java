/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffer.Form;

import java.util.*;
import javax.swing.table.*;

/**
 * Modelo creado para una tabla dinamica que crece para abajo, se pueden agregar filas de forma dinamica
 * ya que utilizamos un ArrayList que es una lista enlazada del tipo generico<>
 * que se encuetra en el paquete java.util.*;
 * @author Mbaez
 */
public class EditableTableModel extends DefaultTableModel {

    private Object ref;
    private ArrayList<Object[]> data; //representa la lista de filas que tendremos
    private int nroColumnas; //numero de columnas
    private Class types[];    //nombre de las clases que se almacenar en la tabla
    private boolean editable[]; //si el campo es editable o no

    /**
     * Constructor de la Clase EditableTableModel
     * resive el nombre que van a tener las columnas, que tipo de dato albergaran, y si seran editables
     * @author Mbaez
     */
    public EditableTableModel(String nombreColunas[], Class classes[], boolean editable[]) {
        initComponents(nombreColunas, classes, editable);
    }

    private void initComponents(String nombreColunas[], Class classes[], boolean editable[])
            throws IllegalArgumentException {
        if (nombreColunas.length != classes.length || classes.length != editable.length) {
            throw new IllegalArgumentException("El paso de parametros deben ser del mismo tamaño");
        }
        dataVector.add(ref);
        data = new ArrayList<Object[]>();
        ref = data;
        this.nroColumnas = nombreColunas.length;
        this.setColumnIdentifiers(nombreColunas);
        this.setRowCount(data.size());
        this.types = classes;
        this.editable = editable;


    }

    /**
     * Al heredar de DefaultTableModel y al agregar nuevos atributos significa que tendremos que redefinir varios metodos
     * para que la tabla pueda manejar nuestos datos.
     * los metods a redefinir son los sigts:
     * +Class getColumnClass(int columnIndex): que debe retornar que tipo de dato alberga la columna "columnIndex"
     * +boolean isCellEditable(int rowIndex, int columnIndex): debe retornar si la columna "columnIndex" puede ser editada
     * +void addRow(Object[] row):debe agregar el objeto "row" a una nueva columna
     * +void removeRow(int row):remueve la fila row
     * +Object getValueAt(int row, int column):retorna el valor almacenado en la fila "row" y la columna "column"
     * +void setValueAt(Object aValue, int rowIndex, int columnIndex):establece el objeto "aValue" de la tabla en la fila "rowIndex" y la columna "columnIndex" 
     * @author Mbaez
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable[columnIndex];
    }

    @Override
    public void addRow(Object[] row) {
        int numRows = dataVector.size();
        this.setRowCount(numRows + 1);
        data.add(row);
        this.fireTableRowsInserted(numRows, numRows + 1);//actializamos el modelo
    }

    @Override
    public void removeRow(int row) {
        data.remove(row);
        this.setRowCount(this.getRowCount() - 1);
        this.fireTableRowsDeleted(this.getRowCount(), this.getRowCount());//actializamos el modelo
    }

    public void removeRow() {
        while (!data.isEmpty()) {
            removeRow(0);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object rowVector[] = data.get(row);
        return rowVector[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object[] columna;
        columna = getValueAt(rowIndex);
        columna[columnIndex] = aValue;
        data.set(rowIndex, columna);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Fueron agregados los sigts metodos par el mejor manejo de la tabla
     * +Object []getValueAt(int row):retorna toda la fila de la pocision "row"
     * +void addRow():añade una nueva fila al final de la tabla
     * @author Mbaez
     */
    public Object[] getValueAt(int row) {
        Object rowVector[] = data.get(row);
        return rowVector;
    }
    //podemos agregar de forma dinamica las filas con el metodo addRow 

    public void addRow() {
        int numRows = data.size();
        Object[] row = new Object[nroColumnas];
        this.setRowCount(numRows + 1);
        data.add(row);
        this.fireTableRowsInserted(numRows, numRows + 1);//actualizamos el modelo
    }
}
